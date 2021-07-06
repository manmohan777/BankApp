package com.example.bankapp.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapp.Adaptors.MessageAdapter;
import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.Database.Room.Message;
import com.example.bankapp.Database.ViewModel.ChatViewModel;
import com.example.bankapp.Database.ViewModel.MessageViewModel;
import com.example.bankapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class MessageActivity extends BaseActivity implements  MessageAdapter.Listener{

    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private MessageAdapter messageAdapter;
    private EditText editMessage;
    private Button buttonSend;
    private Chat chat;
    private MessageViewModel messageViewModel;
    private ChatViewModel chatViewModel;
    private String chatId;
    private static final String TAG = "MessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_message);
        initViews();
        setSupportActionBar(toolbar);

        chatId = getIntent().getStringExtra("chatId");
        Log.e("chatId", ">> "+chatId);

        messageViewModel = MessageViewModel.of(this);
        chatViewModel = ChatViewModel.of(this);
        chatViewModel.getById(this, chatId, (result0, error0, successful0) -> {

            if(!successful0){
                error0.printStackTrace();
            }

            if(result0==null){
                showToast("Chat not found");
                finish();
                return;
            }
            chat = result0;


            if (chatId != null) {
                messageViewModel.paginate(chatId).observe(MessageActivity.this, data -> {
                    if (data != null) {
                        messageAdapter.submitList(data);
                        messageAdapter.notifyDataSetChanged();
                    }
                });
            }else {
                messageViewModel.paginate().observe(MessageActivity.this, data -> {
                    if (data != null) {
                        messageAdapter.submitList(data);
                        messageAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        buttonSend.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(editMessage.getText())){
                Message message = new Message(chatId, editMessage.getText().toString(), getLoggedUserInfo().getId());
                String sentTo = getLoggedUserInfo().getId().equals(chat.getUser1Id())?chat.getUser2Id():chat.getUser1Id();
                messageViewModel.sendMessage(MessageActivity.this, message, sentTo, (result, error, successful) -> {
                    if(successful){
                        editMessage.setText("");
                        recyclerView.postDelayed(() -> recyclerView.getLayoutManager().scrollToPosition(messageAdapter.getItemCount()-1), 100);
                    }else {
                        showToast("Error");
                    }
                });
            }
        });

        messageAdapter = new MessageAdapter(MessageActivity.this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(null);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(messageAdapter);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        editMessage = findViewById(R.id.edit_message);
        buttonSend = findViewById(R.id.button_send);
    }

    @Override
    protected void onPause() {
        messageViewModel.markAllAsRead(chatId, getLoggedUserInfo().getId());
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public String getUserId() {
        return getLoggedUserInfo().getId();
    }
}