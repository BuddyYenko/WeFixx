package com.example.s215087038.wefixx.manager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.s215087038.wefixx.R;


public class Notify extends AppCompatActivity {
Button b1, b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        int NOTIFICATION_ID = 234;
        Context context = this;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(mChannel);
        }
        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Notify.this);
                    builder.setSmallIcon(R.drawable.alert);
                    Intent intent = new Intent(Notify.this, ProviderDelayed.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(Notify.this, 0, intent, 0);
                    builder.setContentIntent(pendingIntent);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
                    builder.setContentTitle("Delayed Requests");
                    builder.setContentText("Your notification content here.");
                    builder.setSubText("Tap to see delayed requests.");

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    // Will display the notification in the notification bar
                    notificationManager.notify(1, builder.build());

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
                    nMgr.cancel(1);



            }
        });
    }

//
//    @OnClick(R.id.button)
//    public void sendNotification() {
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.journaldev.com/"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//        builder.setContentTitle("Notifications Title");
//        builder.setContentText("Your notification content here.");
//        builder.setSubText("Tap to view the website.");
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        // Will display the notification in the notification bar
//        notificationManager.notify(1, builder.build());
//    }

//    @OnClick(R.id.button2)
//    public void cancelNotification() {
//
//        String ns = Context.NOTIFICATION_SERVICE;
//        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
//        nMgr.cancel(1);
//
//
//    }

}

