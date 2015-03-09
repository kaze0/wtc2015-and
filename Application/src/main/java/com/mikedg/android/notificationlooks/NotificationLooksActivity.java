package com.mikedg.android.notificationlooks;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

import com.example.android.wearable.watchface.R;

public class NotificationLooksActivity extends Activity {

    private static final String EXTRA_EVENT_ID = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_looks);

        createBasicNotification(1, "Wearables Tech Con", "Santa Clara");
    }

    public void createBasicNotification(int eventId, String title, String location) {
        int notificationId = 001;
// Build intent for notification content
        Intent viewIntent = new Intent(this, NotificationLooksActivity.class);
        viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle(title)
//                        .setContentText(location)
                        .setContent(new RemoteViews("com.example.android.wearable.watchface", R.layout.noti_custom))
                        .setContentIntent(viewPendingIntent);

        addActionButtons(notificationBuilder, "Action 1", viewPendingIntent);
        addActionButtons(notificationBuilder, "Action 2", viewPendingIntent);
        addActionButtons(notificationBuilder, "Action 3", viewPendingIntent);

        //FIXME: since both of these use WearableExtender, it's one or the other in this sample
        //The last one takes precedence
        addWearableOnlyActions(notificationBuilder, "Wear 1", viewPendingIntent);
        addPage(notificationBuilder);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public NotificationCompat.Builder addActionButtons(NotificationCompat.Builder builder, String title, PendingIntent pendingIntent) {
        return builder.addAction(R.drawable.ic_launcher, title, pendingIntent);
    }

    public NotificationCompat.Builder addWearableOnlyActions(NotificationCompat.Builder builder, String title, PendingIntent pendingIntent) {
        //These wipe out the non-wearable specific actions on wear devices
        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        title, pendingIntent)
                        .build();

        return builder.extend(new NotificationCompat.WearableExtender().addAction(action));
    }

    public NotificationCompat.Builder addPage(NotificationCompat.Builder builder) {
// Create a big text style for the second page
        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle("Page 2")
                .bigText("A lot of text...");

// Create second page notification
        Notification secondPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(secondPageStyle)
                        .build();

        builder.extend(new NotificationCompat.WearableExtender().addPage(secondPageNotification));
        return builder;
    }
}