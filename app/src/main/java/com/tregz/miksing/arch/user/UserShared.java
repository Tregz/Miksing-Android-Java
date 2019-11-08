package com.tregz.miksing.arch.user;

import android.content.Context;
import android.content.SharedPreferences;

public class UserShared {
    //private final String TAG = UserShared.class.getSimpleName();
    private final String USERNAME = "username";
    private final String EMAIL = "email";

    private static UserShared instance;

    public static UserShared getInstance(Context context) {
        if (instance == null && context != null)
            instance = new UserShared(context.getApplicationContext());
        return instance;
    }

    private UserShared(Context context) {
        String authority = context.getPackageName() + ".pref";
        sp = context.getSharedPreferences(authority, Context.MODE_PRIVATE);
    }

    private SharedPreferences sp;

    public String getEmail() {
        return sp.getString(EMAIL, null);
    }

    public void setEmail(String email) {
        sp.edit().putString(EMAIL, email).apply();
    }

    public String getUsername() {
        return sp.getString(USERNAME, null);
    }

    public void setUsername(String username) {
        sp.edit().putString(USERNAME, username).apply();
    }
}
