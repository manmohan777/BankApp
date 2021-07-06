package com.example.bankapp.Database.SharedPreferences;

public enum LoginPreferenceKey {
    USER_ID("user_id"), NAME("name"), EMAIL("email"), PHONE_NUMBER("phoneNumber"), ACCOUNT_NO("accountNo");
    private final String key;

    LoginPreferenceKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
