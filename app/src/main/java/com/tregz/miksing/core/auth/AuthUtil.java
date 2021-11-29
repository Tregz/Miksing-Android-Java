package com.tregz.miksing.core.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tregz.miksing.R;
import com.tregz.miksing.core.note.NoteUtil;
import com.tregz.miksing.core.pref.PrefShared;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.song.SongListener;
import com.tregz.miksing.data.tube.TubeCreate;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.user.UserDelete;
import com.tregz.miksing.data.user.UserListener;
import com.tregz.miksing.data.user.song.UserSongListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.tregz.miksing.data.Data.UNDEFINED;
import com.tregz.miksing.home.HomeView;

public final class AuthUtil {

    private final static String TAG = AuthUtil.class.getSimpleName();

    public final static int SIGN_IN = 101;

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

    public static void login(HomeView view) {
        AuthUI.SignInIntentBuilder ui = AuthUI.getInstance().createSignInIntentBuilder();
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.EmailBuilder().build());
        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        // if Facebook dev account id is set in Firebase console

        // TODO restore Facebook dev account
        // providers.add(new AuthUI.IdpConfig.FacebookBuilder().build());

        // release build only: if app certificate is set in Firebase console
        providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());
        ui.setIsSmartLockEnabled(false); // smart lock not well supported by FirebaseUI
        ui.setAvailableProviders(providers);
        ui.setLogo(R.mipmap.ic_launcher_logo);
        ui.setTheme(R.style.LoginTheme);
        // optional terms of use (tos) and privacy policy
        String terms = "http://www.tregz.com/miksing/aide/en/privacy.pdf"; // TODO: terms
        String policy = "http://www.tregz.com/miksing/aide/en/privacy.pdf";
        ui.setTosAndPrivacyPolicyUrls(terms, policy);
        view.startActivityForResult(ui.build(), SIGN_IN);
    }

    public static FirebaseUser user() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String userId() {
        FirebaseUser user = user();
        return user != null ? user.getUid() : null;
    }

    public static void onUserLogin(final Context context) {
        // TODO wipe on user changed only
        //new SongDelete(context).wipe();
        new UserDelete(context).wipe();


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String fUid = firebaseUser != null ? firebaseUser.getUid() : PrefShared.defaultUser;
        //new TubeCreate(context, fUid, null); // init the main list
        new TubeCreate(context, UNDEFINED, null); // init the prepare list

        new UserListener(context, fUid);
        new UserSongListener(context, fUid);


        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            PrefShared.getInstance(context).setUid(uid);
            PrefShared.getInstance(context).setUsername(firebaseUser.getDisplayName());
            PrefShared.getInstance(context).setEmail(firebaseUser.getEmail());
            //new UserListener(context, uid);
            //new UserSongListener(context, uid);
            new NoteUtil(); // Retrieve fcm token for testing (result printed to Logcat)
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                DataReference.getInstance(context).accessSong().songList().subscribeOn(Schedulers.io()).subscribe(maybeObserver);
                new SongListener(context, fUid);
            }
        }, 2000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                DataReference.getInstance(context).accessTubeSong().whereTube(fUid).subscribeOn(Schedulers.io()).subscribe(maybeTubeObserver);
                new SongListener(context, fUid);
            }
        }, 2000);
    }

    private static final MaybeObserver<List<Song>> maybeObserver = new MaybeObserver<List<Song>>() {
        @Override
        public void onComplete() {
            // do nothing
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onSubscribe(Disposable d) {
            // do nothing
        }
        @Override
        public void onSuccess(List<Song> result) {
            Log.d(TAG, "count: " + result.size());
        }
    };

    private static final MaybeObserver<List<TubeSongRelation>> maybeTubeObserver = new MaybeObserver<List<TubeSongRelation>>() {
        @Override
        public void onComplete() {
            // do nothing
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onSubscribe(Disposable d) {
            // do nothing
        }
        @Override
        public void onSuccess(List<TubeSongRelation> result) {
            Log.d(TAG, "TubeSongRelation count: " + result.size());
        }
    };
}
