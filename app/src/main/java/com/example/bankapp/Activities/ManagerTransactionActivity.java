package com.example.bankapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.TransactionAdapter;
import com.example.bankapp.Database.Room.Transaction;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ManagerTransactionActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView logout;
    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private static final String TAG = "ManagerTransactionActiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_manage_transaction);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.managerTransactionHistory);
        initLogout(logout);

        User.Info user = getLoggedUserInfo();
        List<Transaction> data;
        if (user.getId().isEmpty()){
            getTransactionDAO().findBySenderReceiver("ADMIN").observe(this, data1 -> {
                TransactionAdapter tAdapter = new TransactionAdapter(ManagerTransactionActivity.this, data1);
                recyclerView.setAdapter(tAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ManagerTransactionActivity.this));
            });
        }else {
            getTransactionDAO().findBySenderReceiver(user.getId()).observe(this, data1 -> {
                TransactionAdapter tAdapter = new TransactionAdapter(ManagerTransactionActivity.this, data1);
                recyclerView.setAdapter(tAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ManagerTransactionActivity.this));
            });
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}