package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.paging.PagedList;

import java.util.concurrent.Executors;

public class AppViewModel extends AndroidViewModel {


    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public <T> void delayed(CompletableListener<T> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CompletionListener.dispatchSuccess(listener, listener.run());
            }catch (Throwable throwable){
                CompletionListener.dispatchError(listener, throwable);
            }
        });
    }

    public <T> void delayedOnUiThread(Activity activity, CompletableListener<T> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CompletionListener.dispatchSuccessOnUIThread(activity, listener, listener.run());
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    protected static PagedList.Config defaultPageConfig() {
        return (new PagedList.Config.Builder())
                .setPageSize(10)
                .build();
    }

}
