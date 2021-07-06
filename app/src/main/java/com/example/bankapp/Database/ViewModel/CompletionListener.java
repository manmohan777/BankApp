package com.example.bankapp.Database.ViewModel;


import android.app.Activity;

public interface CompletionListener<T> {

    void onCompletion(T result,  Throwable error, boolean successful);

    static <E> void dispatchSuccess(CompletionListener<E> listener, E result){
        if(listener!=null){
            listener.onCompletion(result, null, true);
        }
    }

    static <E> void dispatchSuccess(CompletionListener<E> listener){
        if(listener!=null){
            listener.onCompletion(null, null, true);
        }
    }

    static <E> void dispatchError(CompletionListener<E> listener, Throwable error){
        if(listener!=null){
            listener.onCompletion(null, error, false);
        }
    }

    static <E> void dispatchSuccessOnUIThread(Activity activity, CompletionListener<E> listener, E result){
        if(listener!=null){
            activity.runOnUiThread(() -> listener.onCompletion(result, null, true));
        }
    }

    static <E> void dispatchSuccessOnUIThread(Activity activity, CompletionListener<E> listener){
        if(listener!=null){
            activity.runOnUiThread(() -> listener.onCompletion(null, null, true));
        }
    }

    static <E> void dispatchErrorOnUIThread(Activity activity, CompletionListener<E> listener, Throwable error){
        if(listener!=null){
            if(listener!=null){
                activity.runOnUiThread(() -> listener.onCompletion(null, error, false));
            }
        }
    }

    interface Task{
        void run();
    }
}
