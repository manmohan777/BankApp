package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.ChatDAO;
import com.example.bankapp.Database.Room.Chat;

import java.util.concurrent.Executors;

public class ChatViewModel extends AppViewModel {

    private final ChatDAO chatDAO;


    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatDAO = AppDatabase.getDatabase(application).getChatDAO();
    }

    public static ChatViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(ChatViewModel.class);
    }

    public LiveData<PagedList<Chat>> paginate(String userId) {
        return new LivePagedListBuilder<>(
                chatDAO.paginateByUserId(userId), defaultPageConfig())
                .build();
    }

    public void getById(Activity activity, String chatId, CompletionListener<Chat> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CompletionListener.dispatchSuccessOnUIThread(activity, listener, chatDAO.findById(chatId));
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

}
