package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.NotificationDAO;
import com.example.bankapp.Database.Room.Notification;

import java.util.concurrent.Executors;

public class NotificationViewModel extends AppViewModel {

    private final NotificationDAO notificationDAO;


    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationDAO = AppDatabase.getDatabase(application).getNotificationDAO();
    }

    public static NotificationViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(NotificationViewModel.class);
    }

    public LiveData<PagedList<Notification>> paginate(String userId) {
        return new LivePagedListBuilder<>(
                notificationDAO.paginateByUserId(userId), defaultPageConfig())
                .build();
    }

    public LiveData<Integer> countNotRead(String userId){
        return notificationDAO.countNotReadByUserId(userId);
    }

    public void markAllAsRead() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                notificationDAO.markAllAsRead();
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
        });
    }
}
