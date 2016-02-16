package com.earnapp.webservice;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private String TAG = this.getClass().getName();

    public WebServiceUserAdpt(AppCompatActivity context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

     public void createUser(final String username, final String name,final String password, final String facebook) throws JSONException {
         String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_CREATE_USER_URL;
         Map<String,String> headers = new HashMap<>();
         headers.put("Content-Type","application/json");
         headers.put("Authorization","basicauth");
         CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,url,headers, new Response.Listener<JSONObject>(){
             @Override
             public void onResponse(JSONObject response) {
                 Log.d(TAG,"User creation completed successfully");
                 // Go to next activity
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
                 params.put("name",name);
                 params.put("facebook",facebook);
                 return params;
             }
         };
         requestQueue.add(request);
     }

     public void checkForExistingUser(final String username,final String password,final String facebook,final String name){
        String url = "";
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Authorization","basicauth");
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.GET,url,headers,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() != 0){
                    Toast.makeText(context, "Syncing your data)",
                            Toast.LENGTH_SHORT).show();

                    // Go to next activity
                }else{
                    try {
                        createUser(username,password,name,facebook);
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
        });
        requestQueue.add(request);
     }
}
