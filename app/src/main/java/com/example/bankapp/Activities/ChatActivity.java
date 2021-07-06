package com.example.bankapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.ChatAdapter;
import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.Database.ViewModel.ChatViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class ChatActivity extends BaseActivity implements  ChatAdapter.Listener{

    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private ChatViewModel chatViewModel;
    private ChatAdapter chatAdapter;
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_chat);
        initViews();
        setSupportActionBar(toolbar);
        chatViewModel = ChatViewModel.of(this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        chatAdapter = new ChatAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        chatViewModel.paginate(getLoggedUserInfo().getId()).observe(this, data -> {
            if (data != null) {
                chatAdapter.submitList(data);
                recyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
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
    public void onChatSelected(Chat chat) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("chatId", chat.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}