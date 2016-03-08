package com.earnapp.helpbucks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.earnapp.constants.ApplicationConstants;
import com.earnapp.helpbucks.R;
import com.earnapp.helpbucks.SocialLoginActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by vizsatiz on 07-03-2016.
 */
public class HelpEarnGCMListenerService  extends GcmListenerService{

    private String TAG = "gcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString(ApplicationConstants.ID);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, SocialLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_promote_icon_colored)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
