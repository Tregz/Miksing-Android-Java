package com.tregz.miksing.arch.note;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class NoteUtil implements OnCompleteListener<InstanceIdResult>, OnFailureListener {
    private final static String TAG = NoteUtil.class.getSimpleName();

    public void fcmTokenLog() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "getInstanceId failed", task.getException());
            return;
        }
        if (task.getResult() != null) {

            // Get new Instance ID token
            String token = task.getResult().getToken();

            // Log token
            Log.d(TAG, token);
        }
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
