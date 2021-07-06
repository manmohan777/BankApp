package com.example.bankapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.AdminPreferences;
import com.example.bankapp.Database.ViewModel.CompletionListener;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends BaseActivity implements View.OnClickListener {

    private EditText etNewRate, etAmount, etAccountNumber;
    private Button btnAddMoney, btnRateUpdate, btnDeductMoney,seeUsersOrAgentsBtn;
    private MaterialToolbar toolbar;
    private ImageView logout;
    private TextView adminBal;
    private BottomNavigationView bottomNavigationView;
    UserViewModel userViewModel;
    private static final String TAG = "AdminActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_admin);
        initViews();
        initBottomNavMenu(bottomNavigationView, R.id.adminHome);
        setSupportActionBar(toolbar);

        userViewModel = UserViewModel.of(this);

        btnRateUpdate.setOnClickListener(v -> {
            String rate = etNewRate.getText().toString();
            if (rate.isEmpty()) {
                showToast("Enter New Rate");
                return;
            }
            AdminPreferences.getInstance().setBSRRate(Double.parseDouble(rate));
            etNewRate.setText("");
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("New Rate Updated")
                    .setPositiveButton("Okay", (dialogInterface, i) -> {
                    });
            builder.create().show();
        });

        btnDeductMoney.setOnClickListener(this);
        btnAddMoney.setOnClickListener(this);
        seeUsersOrAgentsBtn=findViewById(R.id.seeUsersOrAgentsBtn);
        seeUsersOrAgentsBtn.setOnClickListener(this);

        initLogout(logout);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logout);
        btnAddMoney = findViewById(R.id.btnAddMoney);
        btnRateUpdate = findViewById(R.id.btnRateUpdate);
        btnDeductMoney = findViewById(R.id.btnDeductMoney);
        etNewRate = findViewById(R.id.etNewRate);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etAmount = findViewById(R.id.etAmount);
        adminBal = findViewById(R.id.adminBal);
        adminBal.setText(String.format("Balance : %s", AdminPreferences.getInstance().getBal()));
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void onBackPressed() {
        showToast("Please Logout");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAddMoney.getId() || v.getId() == btnDeductMoney.getId()) {
            String accNo = etAccountNumber.getText().toString();
            String stringAmt = etAmount.getText().toString();
            if (accNo.isEmpty()) {
                showToast("Enter Account Number");
                return;
            }
            userViewModel.userByAccount(this, accNo, new CompletionListener<User>() {
                @Override
                public void onCompletion(User user, Throwable error, boolean successful) {
                    if (user == null) {
                        showToast("No Such User");
                        return;
                    }
                    if (stringAmt.isEmpty()) {
                        showToast("Please Enter Amount");
                        return;
                    }
                    double amt = Double.parseDouble(stringAmt);

                    if(v.getId() == btnDeductMoney.getId()){
                        amt = -amt;
                    }

                    userViewModel.addOrDeduceMoney(AdminActivity.this, user, amt, (result, error1, successful1) -> {
                        if(successful1){

                            adminBal.setText(String.format("Balance : %s", AdminPreferences.getInstance().getBal()));

                            etAccountNumber.setText("");
                            etAmount.setText("");
                            adminBal.setText(String.format("Balance : %s", AdminPreferences.getInstance().getBal()));

                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this)
                                    .setTitle("Alert")
                                    .setMessage("Transaction Completed")
                                    .setPositiveButton("Okay", (dialogInterface, i) -> {
                                    });
                            builder.create().show();
                        }else {
                            showToast(error1.getMessage());
                        }
                    });
                }
            });
        }else if (v.getId()==seeUsersOrAgentsBtn.getId()){

            startActivity(new Intent(AdminActivity.this,UsersListActivity.class));

        }
    }
}