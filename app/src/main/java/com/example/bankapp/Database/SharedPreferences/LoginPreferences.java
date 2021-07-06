package com.example.bankapp.Database.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bankapp.Application;
import com.example.bankapp.Database.Room.User;

public class LoginPreferences {
    private static final String PREFERENCE_NAME = "login_preference";
    private static LoginPreferences mInstance = null;

    public static synchronized LoginPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new LoginPreferences();
        }
        return mInstance;
    }

    private String getUserId() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LoginPreferenceKey.USER_ID.getKey(), "");
    }

    private void setUserId(String userId) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(LoginPreferenceKey.USER_ID.getKey(), userId).apply();
    }

    private String getName() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LoginPreferenceKey.NAME.getKey(), "");
    }

    private void setName(String userId) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(LoginPreferenceKey.NAME.getKey(), userId).apply();
    }

    private void setEmail(String value) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(LoginPreferenceKey.EMAIL.getKey(), value).apply();
    }

    private String getEmail() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LoginPreferenceKey.EMAIL.getKey(), "");
    }

    private String getPhoneNumber() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LoginPreferenceKey.PHONE_NUMBER.getKey(), "");
    }

    private void setPhoneNumber(String value) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(LoginPreferenceKey.PHONE_NUMBER.getKey(), value).apply();
    }

    private String getAccountNo() {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LoginPreferenceKey.ACCOUNT_NO.getKey(), "");
    }

    private void setAccountNo(String value) {
        SharedPreferences preferences = Application.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(LoginPreferenceKey.ACCOUNT_NO.getKey(), value).apply();
    }

    public void saveUserInfo(User user){
        setUserId(user.getId());
        setName(user.getName());
        setEmail(user.getEmail());
        setPhoneNumber(user.getPhoneNumber());
        setAccountNo(user.getAccountNo());
    }

    public User.Info getLoggedUserInfo(){
        return new User.Info(
                getUserId(),
                getName(),
                getEmail(),
                getPhoneNumber(),
                getAccountNo()
        );
    }

    public void saveUserInfo(String email, String name) {
        setName(name);
        setEmail(email);
    }

    public void clear() {
        setUserId(null);
        setName(null);
        setEmail(null);
        setPhoneNumber(null);
        setAccountNo(null);
    }
}
