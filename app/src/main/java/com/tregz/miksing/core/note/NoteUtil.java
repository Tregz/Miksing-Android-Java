package com.tregz.miksing.core.note;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class NoteUtil implements OnCompleteListener<String>, OnFailureListener {
    private final static String TAG = NoteUtil.class.getSimpleName();

    public NoteUtil() {
        Task<String> task = FirebaseMessaging.getInstance().getToken();
        task.addOnCompleteListener(this);
        task.addOnFailureListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<String> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            return;
        }

        // Get new Instance ID token
        String token = task.getResult();

        // Log token
        Log.d(TAG, token);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e.getMessage() != null) Log.e(TAG, e.getMessage());
    }

    /* Upstream notification to server
    FirebaseMessaging fm = FirebaseMessaging.getInstance();
    String senderId = getString(R.string.fcm_sender_id);
    AtomicInteger msgId = new AtomicInteger();
    fm.send(new RemoteMessage.Builder(senderId + "@fcm.googleapis.com")
    .setMessageId(String.valueOf(msgId.get())) // random, will be sent back
    .addData("hello", "world")
    .build()); */

}
