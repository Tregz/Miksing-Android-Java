package com.tregz.miksing.arch.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class AuthUtil {

    public static FirebaseUser user() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean logged() {
        return user() != null;
    }
}
