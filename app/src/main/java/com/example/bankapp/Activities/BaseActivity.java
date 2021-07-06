package com.example.bankapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.InsuranceDataDAO;
import com.example.bankapp.Database.DAO.LoanDataDAO;
import com.example.bankapp.Database.DAO.TransactionDAO;
import com.example.bankapp.Database.DAO.UserDAO;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.LoginPreferences;
import com.example.bankapp.Database.ViewModel.NotificationViewModel;
import com.example.bankapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BaseActivity extends AppCompatActivity {

    public static final String ADMIN_EMAIL = "pksv@admin";
    private NotificationViewModel notificationViewModel;
    private int notificationCount = 0;
    private static final String TAG = "BaseActivity";

    public User.Info getLoggedUserInfo() {
        return LoginPreferences.getInstance().getLoggedUserInfo();
    }

    private AppDatabase getDatabase() {
        return AppDatabase.getDatabase(getApplicationContext());
    }

    public InsuranceDataDAO getInsuranceDAO() {
        return getDatabase().getInsuranceDataDAO();
    }

    public TransactionDAO getTransactionDAO() {
        return getDatabase().getTransactionDAO();
    }

    public UserDAO getUserDAO() {
        return getDatabase().getUserDAO();
    }

    public LoanDataDAO getLoanDAO() {
        return getDatabase().getLoanDataDAO();
    }

    public boolean isAdmin() {
        String email = getLoggedUserInfo().getEmail();
        return email.equals(ADMIN_EMAIL);
    }

    public boolean isAgent() {
        return getLoggedUserInfo().isAgent();
    }

    protected void initLogout(ImageView logout) {
        if (logout != null) {
            logout.setOnClickListener(v -> {
                SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                LoginPreferences.getInstance().clear();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        notificationViewModel = NotificationViewModel.of(this);
        notificationViewModel.countNotRead(getLoggedUserInfo().getId()).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                notificationCount = integer;
                invalidateOptionsMenu();
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    protected void initBottomNavMenu(BottomNavigationView bottomNavigationView, int menuId) {
        bottomNavigationView.setSelectedItemId(menuId);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Log.e(TAG, "initBottomNavMenu: "+item.getItemId() + " admin id "+ R.id.adminHome);
            switch (item.getItemId()) {
                case R.id.adminHome:
                    Intent adminHomeIntent = new Intent(this, AdminActivity.class);
                    adminHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(adminHomeIntent);
                    break;
                case R.id.insurance:
                    Intent insuranceIntent = new Intent(this, InsuranceActivity.class);
                    insuranceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(insuranceIntent);
                    break;
                case R.id.transactionHistory:
                    Intent sendMoneyIntent = new Intent(this, TransactionActivity.class);
                    sendMoneyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sendMoneyIntent);
                    break;
                case R.id.loan:
                    Intent loanIntent = new Intent(this, LoanActivity.class);
                    loanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loanIntent);
                    break;
                case R.id.home:
                    Intent homeIntent = new Intent(this, UserHome.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    break;
                case R.id.managerInsurance:
                    Intent managerInsuranceIntent = new Intent(this, ManageInsuranceActivity.class);
                    managerInsuranceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(managerInsuranceIntent);
                    break;
                case R.id.managerTransactionHistory:
                    Intent managerTransactionHistoryIntent = new Intent(this, ManagerTransactionActivity.class);
                    managerTransactionHistoryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(managerTransactionHistoryIntent);
                    break;
                case R.id.managerLoan:
                    Intent managerLoanIntent = new Intent(this, ManageLoanActivity.class);
                    managerLoanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(managerLoanIntent);
                    break;

                case R.id.agentHome:
                    if (getLoggedUserInfo().getEmail() == ADMIN_EMAIL) {
                        Intent managerHomeIntent = new Intent(this, AdminActivity.class);
                        managerHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(managerHomeIntent);
                        return true;
                    }
//                    } else {
//                        Intent managerHomeIntent = new Intent(this, AgentHome.class);
//                        managerHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(managerHomeIntent);
//                    }
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = null;
        SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
//        if(preferences.getString("email","")!="") {
        if(true){
            if (preferences.getBoolean("isAdmin",false)) {
                homeIntent = new Intent(this, AdminActivity.class);
            } else if (preferences.getBoolean("isAgent",false)) {
                homeIntent = new Intent(this, AgentHome.class);
            } else {
                homeIntent = new Intent(this, UserHome.class);
            }
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manager_context_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notification);
        View actionView = menuItem.getActionView();

        setupBadge(actionView.findViewById(R.id.cart_badge), notificationCount);

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_notification: {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_inquiries: {
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge(TextView textCartItemCount, int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                if (mCartItemCount > 9) {
                    textCartItemCount.setText("9+");
                } else {
                    textCartItemCount.setText(String.valueOf(mCartItemCount));
                }
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}