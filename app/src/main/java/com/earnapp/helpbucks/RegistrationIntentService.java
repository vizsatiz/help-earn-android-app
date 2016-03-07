package com.earnapp.helpbucks;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.earnapp.constants.GCMConfigConstants;
import com.earnapp.helpbucks.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by vizsatiz on 07-03-2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String[] TOPICS = {"global"};
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandle Register Intent !!");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);
            subscribeTopics(token);
            sharedPreferences.edit().putBoolean(GCMConfigConstants.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (IOException e) {
            e.printStackTrace();
            sharedPreferences.edit().putBoolean(GCMConfigConstants.SENT_TOKEN_TO_SERVER, false).apply();
        }
        Intent registrationComplete = new Intent(GCMConfigConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("Send token to server", token);
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
