package com.earnapp.helpbucks;

import android.content.Intent;

import com.earnapp.helpbucks.RegistrationIntentService;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by vizsatiz on 07-03-2016.
 */
public class HelpEarnInstanceIDService extends InstanceIDListenerService{

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
