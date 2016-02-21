package com.earnapp.webservice;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.volley.CustomJsonArrayRequest;
import com.earnapp.volley.CustomJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vizsatiz on 15-02-2016.
 */
public class WebServiceUserAdpt {

    private AppCompatActivity context;
    private RequestQueue requestQueue;
    private String TAG = ApplicationConstants.TAG_DB_AUTH;

    public WebServiceUserAdpt(AppCompatActivity context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

     public void createUser(final String username, final String name,final String password, final String facebook) throws JSONException {
         String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_CREATE_USER_URL;
         Map<String,String> params = new HashMap<>();
         params.put("username",username);
         params.put("password",password);
         params.put("name",name);
         Log.d(TAG,"User facebook token : "+facebook);
         params.put("facebook",facebook);
         JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(params), new Response.Listener<JSONObject>(){
             @Override
             public void onResponse(JSONObject response) {
                 Log.d(TAG,"User creation completed successfully");
                 WebServiceAuthAdpt authAdpt = new WebServiceAuthAdpt(context);
                 authAdpt.updateAndAuthenticateUser(username, password,facebook,name);
             }
         },new Response.ErrorListener(){

             @Override
             public void onErrorResponse(VolleyError error) {
                 error.printStackTrace();
             }
         }) {
             @Override
             public Map<String,String> getHeaders(){
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                 headers.put("Authorization",ApplicationConstants.BASIC_AUTH);
                 headers.put("User-agent", ApplicationConstants.USER_AGENT);
                 return headers;
             }
         };
         requestQueue.add(request);
     }

     public void checkForExistingUser(final String username,final String password,final String facebook,final String name){
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_GET_USER_BY_USERNAME + username;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() != 0){
                    Log.d(TAG,"The user already exists !!! Welome Back");
                    Toast.makeText(context, "Welcome Back",
                            Toast.LENGTH_SHORT).show();
                    WebServiceAuthAdpt authAdpt = new WebServiceAuthAdpt(context);
                    authAdpt.updateAndAuthenticateUser(username,password,facebook,name);
                }else{
                    try {
                        createUser(username,name,password,facebook);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String,String> getHeaders(){
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                headers.put("Authorization",ApplicationConstants.BASIC_AUTH);
                headers.put("User-agent", ApplicationConstants.USER_AGENT);
                return headers;
            }
        };
        requestQueue.add(request);
     }
}
