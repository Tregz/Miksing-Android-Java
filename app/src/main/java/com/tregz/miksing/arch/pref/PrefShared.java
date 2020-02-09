package com.tregz.miksing.arch.pref;

import android.content.Context;
import android.content.SharedPreferences;

import static com.tregz.miksing.data.DataNotation.PK;

public class PrefShared {
    //private final String TAG = PrefShared.class.getSimpleName();
    private final String USERNAME = "username";
    private final String EMAIL = "email";
    private SharedPreferences sp;

    private static PrefShared instance;

    public static PrefShared getInstance(Context context) {
        if (instance == null && context != null)
            instance = new PrefShared(context.getApplicationContext());
        return instance;
    }

    private PrefShared(Context context) {
        String authority = context.getPackageName() + ".pref";
        sp = context.getSharedPreferences(authority, Context.MODE_PRIVATE);
    }

    public String getEmail() {
        return sp.getString(EMAIL, null);
    }

    public void setEmail(String email) {
        sp.edit().putString(EMAIL, email).apply();
    }

    public String getUid() {
        return sp.getString(PK, null);
    }

    public void setUid(String id) {
        sp.edit().putString(PK, id).apply();
    }

    public String getUsername() {
        return sp.getString(USERNAME, null);
    }

    public void setUsername(String username) {
        sp.edit().putString(USERNAME, username).apply();
    }
}
