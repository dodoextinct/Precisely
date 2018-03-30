package com.pankaj.maukascholars.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.database.DBHandler;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sendNotification(remoteMessage.getData());
        Intent intents=new Intent();
        intents.setAction("MyReceiver");
        intents.putExtra("PostId", remoteMessage.getData().get("PostId"));
        intents.putExtra("SenderId", remoteMessage.getData().get("SenderId"));
        intents.putExtra("SenderName", remoteMessage.getData().get("SenderName"));
        intents.putExtra("PosterName", remoteMessage.getData().get("PosterName"));
        intents.putExtra("Message", remoteMessage.getData().get("Message"));
        intents.putExtra("CreatedAt", remoteMessage.getData().get("CreatedAt"));
        getBaseContext().sendBroadcast(intents);
    }



    private void sendNotification(Map<String, String> messageBody) {
        Intent intent = new Intent(this, ParticularChat.class);
        DBHandler db = new DBHandler(this);
        db.addChat(new ChatTable(2, messageBody.get("PostId"), messageBody.get("SenderId"), messageBody.get("PosterName"), messageBody.get("SenderName"), messageBody.get("Message"), messageBody.get("CreatedAt")));
        intent.putExtra("post_id", messageBody.get("PostId"));
        intent.putExtra("uid_r", messageBody.get("SenderId"));
        intent.putExtra("remote_name", messageBody.get("SenderName"));
        intent.putExtra("poster_name", messageBody.get("PosterName"));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logomauka123)
                .setContentTitle("1 Mile Notification")
                .setContentText(messageBody.get("Title"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
