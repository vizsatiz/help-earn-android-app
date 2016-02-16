package com.earnapp.webservice;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.volley.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vizsatiz on 16-02-2016.
 */
public class WebServiceAuthAdpt {

    public static String xAccessToken;
    private String TAG = this.getClass().getName();
    private AppCompatActivity context;
    private RequestQueue requestQueue;


    public WebServiceAuthAdpt(AppCompatActivity context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void updateAndAuthenticateUser(final String username,final String password){
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_AUTHENTICATE;
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Authorization","basicauth");
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,url,headers, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Authentication Success !!!");
                try {
                    WebServiceAuthAdpt.xAccessToken = response.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        requestQueue.add(request);
    }
}
