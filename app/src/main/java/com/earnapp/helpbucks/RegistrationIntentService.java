package com.earnapp.helpbucks;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.constants.GCMConfigConstants;
import com.earnapp.webservice.WebServiceAuthAdpt;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private void sendRegistrationToServer(final String token) {
        Log.d(TAG, "Sending the token to sever !!");
        WebServiceAuthAdpt.user.setGMCToken(token);
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_UPDATED_GCM_TOKEN
                + WebServiceAuthAdpt.user.getId();
        Map<String,String> params = new HashMap<>();
        params.put(ApplicationConstants.GCM_TOKEN,token);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
            new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"GCM Token updated on the server !! " + token);
            }
             }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-access-token", WebServiceAuthAdpt.xAccessToken);
                headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                headers.put("User-agent", ApplicationConstants.USER_AGENT);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest);
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
