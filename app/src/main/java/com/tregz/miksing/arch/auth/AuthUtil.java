package com.tregz.miksing.arch.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tregz.miksing.arch.note.NoteUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.data.user.UserListener;

public final class AuthUtil {

    private final static String TAG = AuthUtil.class.getSimpleName();

    public static boolean hasNetwork(@Nullable Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (response != null) {
            FirebaseUiException exception = response.getError();
            if (exception != null) {
                if (exception.getMessage() != null) Log.e(TAG, exception.getMessage());
                return exception.getErrorCode() == ErrorCodes.NO_NETWORK;
            }
        }
        return true;
    }

    public static boolean isUser(String uid) {
        FirebaseUser user = user();
        return user != null && uid != null && user.getUid().equals(uid);
    }

    public static boolean logged() {
        return user() != null;
    }

    public static FirebaseUser user() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void onUserLogin(Context context) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            PrefShared.getInstance(context).setUid(firebaseUser.getUid());
            PrefShared.getInstance(context).setUsername(firebaseUser.getDisplayName());
            PrefShared.getInstance(context).setEmail(firebaseUser.getEmail());
            new UserListener(context, firebaseUser.getUid());
            new NoteUtil(context); // Retrieve fcm token for testing (result printed to Logcat)
        }
    }
}
