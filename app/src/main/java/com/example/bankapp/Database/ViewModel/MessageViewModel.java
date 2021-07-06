package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.MessageDAO;
import com.example.bankapp.Database.DAO.NotificationDAO;
import com.example.bankapp.Database.Room.Message;
import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.R;

import java.util.concurrent.Executors;

public class MessageViewModel extends AppViewModel {

    private final MessageDAO messageDAO;
    private final NotificationDAO notificationDAO;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageDAO = AppDatabase.getDatabase(application).getMessageDAO();
        notificationDAO = AppDatabase.getDatabase(application).getNotificationDAO();
    }

    public static MessageViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(MessageViewModel.class);
    }

    public LiveData<PagedList<Message>> paginate(String chatId) {
        return new LivePagedListBuilder<>(
                messageDAO.paginateByChat(chatId), defaultPageConfig())
                .build();
    }

    public LiveData<PagedList<Message>> paginate() {
        return new LivePagedListBuilder<>(
                messageDAO.paginateChat(), defaultPageConfig())
                .build();
    }

    public void sendMessage(Activity activity, Message message, String sendTo, CompletionListener<Message> listener) {

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                messageDAO.createOrUpdate(message);

                Notification notification = new Notification(activity.getString(R.string.new_message), sendTo, Notification.TYPE_MESSAGE);
                notification.setReference(message.getId());
                notificationDAO.createOrUpdate(notification);

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, message);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void markAllAsRead(String chatId, String userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                messageDAO.markAllAsRead(chatId, userId);
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
        });
    }
}
