package com.earnapp.adapters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.models.User;
import com.earnapp.volley.CustomJsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vizsatiz on 12-02-2016.
 */
public class WebServiceAuthAdapter {

    private String xAccessToken;
    private User authUser;

    private static WebServiceAuthAdapter authObj = null;

    private WebServiceAuthAdapter(){
        authenticateAndGetServiceToken();
    }

    public static WebServiceAuthAdapter getInstance(){
        if(authObj == null){
           authObj = new WebServiceAuthAdapter();
        }
        return authObj;
    }

    public void authenticateAndGetServiceToken() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token", "application/json");
        CustomJsonObjectRequest response = new CustomJsonObjectRequest(ApplicationConstants.DB_BASE_URL +
                ApplicationConstants.DB_AUTHENTICATE, headers, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    try {
                        parseAuthResponse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
    }
        private void parseAuthResponse(JSONObject response){
            try {
                this.xAccessToken = response.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
}

