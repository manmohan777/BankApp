package com.example.bankapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.NotificationAdapter;
import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.Database.ViewModel.NotificationViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class NotificationActivity extends BaseActivity implements  NotificationAdapter.Listener{

    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private NotificationViewModel notificationViewModel;
    private NotificationAdapter notificationAdapter;
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_notification);
        initViews();
        setSupportActionBar(toolbar);
        notificationViewModel = NotificationViewModel.of(this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        notificationAdapter = new NotificationAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        notificationViewModel.paginate(getLoggedUserInfo().getId()).observe(this, data -> {
            if (data != null) {
                notificationAdapter.submitList(data);
                recyclerView.setAdapter(notificationAdapter);
                notificationAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected void onPause() {
        notificationViewModel.markAllAsRead();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoClick(Notification notification) {
        Intent intent;

        if(isAdmin() || isAgent()){
            switch (notification.getNotificationType()){
                case Notification.TYPE_INSURANCE:
                    intent = new Intent(this, ManageInsuranceActivity.class);
                    break;
                case Notification.TYPE_LOAN:
                    intent = new Intent(this, ManageLoanActivity.class);
                    break;
                case Notification.TYPE_TRANSACTION:
                    intent = new Intent(this, ManagerTransactionActivity.class);
                    break;
                case Notification.TYPE_MESSAGE:
                    intent = new Intent(this, ChatActivity.class);
                    break;
                default: return;
            }
        }else {

            switch (notification.getNotificationType()){
                case Notification.TYPE_INSURANCE:
                    intent = new Intent(this, InsuranceActivity.class);
                    break;
                case Notification.TYPE_LOAN:
                    intent = new Intent(this, LoanActivity.class);
                    break;
                case Notification.TYPE_TRANSACTION:
                    intent = new Intent(this, TransactionActivity.class);
                    break;
                case Notification.TYPE_MESSAGE:
                    intent = new Intent(this, ChatActivity.class);
                    break;
                default: return;
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", notification.getReference());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}