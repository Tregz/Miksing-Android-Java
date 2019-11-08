package com.tregz.miksing.arch.note;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tregz.miksing.R;
import com.tregz.miksing.home.HomeActivity;

public class NoteService extends FirebaseMessagingService {
    private final String TAG = NoteService.class.getSimpleName();

    private final String cId = "com.tregz.miksing.channel_id";

    @Override
    public void onCreate() {
        super.onCreate();
        // Since Android Oreo, notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Miksing notifications"; // display name
            int level = NotificationManager.IMPORTANCE_DEFAULT;
            manager().createNotificationChannel(new NotificationChannel(cId, name, level));
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            if (notification.getBody() != null && notification.getTitle() != null) {
                NotificationCompat.Builder builder;
                builder = new NotificationCompat.Builder(this, cId);
                builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                builder.setColor(ContextCompat.getColor(this, R.color.primaryDark));
                builder.setContentTitle(notification.getTitle());
                builder.setContentText(notification.getBody());
                builder.setAutoCancel(true); // auto dismiss when touched
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // close activity on top
                int once = PendingIntent.FLAG_ONE_SHOT; // show only once
                int request = 0; // code to retrieve the pending intent
                PendingIntent tap = PendingIntent.getActivity(this, request, intent, once);
                builder.setContentIntent(tap); // action when touched
                //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //builder.setSound(soundUri);
                //builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                int nId = 0; // notification id
                manager().notify(nId, builder.build());
            }
        }
    }

    private NotificationManager manager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
