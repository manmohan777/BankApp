package com.example.bankapp.Database.SharedPreferences;

public enum AdminPreferencesKey {
    BSR_RATE("bsr_rate"), BAL("bal");
    private final String key;

    AdminPreferencesKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
