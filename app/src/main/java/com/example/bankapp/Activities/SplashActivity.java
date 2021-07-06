package com.example.bankapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.bankapp.Database.Room.User;
import com.example.bankapp.R;

public class SplashActivity extends BaseActivity {

    private final Handler mWaitHandler = new Handler();
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_splash);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        mWaitHandler.postDelayed(() -> {
            //User.Info user = getLoggedUserInfo();
            SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
            String email = preferences.getString("email", "");

            if (email.isEmpty()) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (email.equals("pksv@admin")) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent;
                if(email.contains("agent")){
                    intent = new Intent(this, AgentHome.class);
                }else {
                    intent = new Intent(this, UserHome.class);
                }
                startActivity(intent);
                finish();
            }

        }, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
