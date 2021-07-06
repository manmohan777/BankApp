package com.example.bankapp.Database.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bankapp.Application;

public class AdminPreferences {
    private static final String PREFERENCE_NAME = "admin_preferences";
    private static AdminPreferences mInstance = null;

    public static synchronized AdminPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new AdminPreferences();
        }
        return mInstance;
    }

    public double getBSRRate() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getFloat(AdminPreferencesKey.BSR_RATE.getKey(), 100);
    }

    public void setBSRRate(double rate) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putFloat(AdminPreferencesKey.BSR_RATE.getKey(), (float) rate).apply();
    }

    public double getBal() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getFloat(AdminPreferencesKey.BAL.getKey(), 1000);
    }

    public void setBal(double bal) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putFloat(AdminPreferencesKey.BAL.getKey(), (float) bal).apply();
    }
}
