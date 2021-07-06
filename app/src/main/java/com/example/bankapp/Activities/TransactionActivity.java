package com.example.bankapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.TransactionAdapter;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransactionActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView logout;
    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private static final String TAG = "TransactionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_transaction);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.transactionHistory);
        initLogout(logout);

        User.Info user = getLoggedUserInfo();

        getTransactionDAO().findBySenderReceiver(user.getId()).observe(this, transactions -> {
            TransactionAdapter tAdapter = new TransactionAdapter(TransactionActivity.this, transactions);
            recyclerView.setAdapter(tAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

}