package com.tregz.miksing.arch.note;

import android.util.Log;

import androidx.annotation.NonNull;

import android.content.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.data.song.SongRealtime;
import com.tregz.miksing.data.user.tube.UserTubeRealtime;

public class NoteUtil implements OnCompleteListener<InstanceIdResult>, OnFailureListener {
    private final static String TAG = NoteUtil.class.getSimpleName();

    private Context context;

    public NoteUtil(Context context) {
        this.context = context;
        Task<InstanceIdResult> task = FirebaseInstanceId.getInstance().getInstanceId();
        task.addOnCompleteListener(this);
        task.addOnFailureListener(this);
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
            new SongRealtime(context);
            new UserTubeRealtime(context, PrefShared.getInstance(context).getUid());
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
