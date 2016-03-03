package com.earnapp.webservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.helpbucks.TaskListActivity;
import com.earnapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vizsatiz on 16-02-2016.
 */
public class WebServiceAuthAdpt {


    private AppCompatActivity context;
    private RequestQueue requestQueue;
    private String TAG = ApplicationConstants.TAG_DB_AUTH;

    public static String xAccessToken;
    public static User user;


    public WebServiceAuthAdpt(AppCompatActivity context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void updateAndAuthenticateUser(final String username,final String password,final String facebook,final String name){
        Log.d(TAG,"Going for authenticate and update call");
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_AUTHENTICATE;
        Map<String,String> params = new HashMap<>();
        params.put(ApplicationConstants.USERNAME,username);
        params.put(ApplicationConstants.PASSWORD,password);
        params.put(ApplicationConstants.NAME,name);
        params.put(ApplicationConstants.FACEBOOK,facebook);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(params), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Authencation Success !!");
                try {
                    // Getting the token from Authentication Response
                    WebServiceAuthAdpt.xAccessToken = response.getString(ApplicationConstants.TOKEN);
                    JSONArray userArray = response.getJSONArray("user");
                    String userId = userArray.getJSONObject(0).getString("_id");
                    user = new User(userId,name,username,facebook);
                    Intent intent = new Intent(context, TaskListActivity.class);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Auth Failure  !! "+ error.getMessage());
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String,String> getHeaders(){
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization",ApplicationConstants.BASIC_AUTH);
                headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                headers.put("User-agent", ApplicationConstants.USER_AGENT);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
