package com.earnapp.helpbucks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.webservice.WebServiceAuthAdpt;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vizsatiz on 19-02-2016.
 */
public class CreateTaskTab extends Fragment {

    private String TAG = ApplicationConstants.TAG_TASK;


    public CreateTaskTab() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_create_task, container, false);

        Log.d(TAG, "Setting On Click Listener !!!");

        FloatingActionButton saveButton = (FloatingActionButton) fragmentView.findViewById(R.id.saveTask);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(validateInputData(fragmentView)){
                    JSONObject payload = new JSONObject();

                    EditText titletxt = (EditText) fragmentView.findViewById(R.id.titlebox);
                    String title = titletxt.getText().toString();
                    try {
                        payload.put("title", title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    EditText descriptiontxt = (EditText) fragmentView.findViewById(R.id.descriptionbox);
                    String description = descriptiontxt.getText().toString();
                    try {
                        payload.put("description", description);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    EditText rewardtxt = (EditText) fragmentView.findViewById(R.id.rewardbox);
                    int reward = Integer.parseInt(rewardtxt.getText().toString());
                    try {
                        payload.put("reward", reward);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String userId = WebServiceAuthAdpt.user.getId();

                    try {
                        payload.put("owner", userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Date date = new Date();
                    String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
                    SimpleDateFormat currentDateTime = new SimpleDateFormat(format);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, 2);
                    String expiry = currentDateTime.format(cal.getTime());
                    try {
                        payload.put("expiry", expiry);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.d(TAG, "Going to make POST call to server !!!");
                    String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_POST_TASK;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "Saving task success !!!");
                                    ViewPager vPager = ((TaskListActivity) getActivity()).getViewPager();
                                    vPager.setCurrentItem(0);
                                    TaskTabs.requestServerAndGetTasks();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("x-access-token", WebServiceAuthAdpt.xAccessToken);
                            headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                            headers.put("User-agent", ApplicationConstants.USER_AGENT);
                            return headers;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(request);
                }else{
                    Toast.makeText(getActivity(), "Fill all the above fields for a better response",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return fragmentView;
    }

    public boolean validateInputData(View form){
        boolean isValid = true;
        AutoCompleteTextView titleView = (AutoCompleteTextView)form.findViewById(R.id.titlebox);
        EditText descriptionView = (EditText)form.findViewById(R.id.rewardbox);
        EditText rewardView = (EditText) form.findViewById(R.id.rewardbox);

        String title = titleView.getText().toString();
        String description = titleView.getText().toString();
        String reward = rewardView.getText().toString();
        if((title.equals(""))  || description.equals("") || reward.equals("")){
            isValid = false;
        }
        return isValid;
    }
}
