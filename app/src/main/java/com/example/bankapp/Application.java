package com.example.bankapp;

public class Application extends android.app.Application {
    private static Application mInstance;

    public static synchronized Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}