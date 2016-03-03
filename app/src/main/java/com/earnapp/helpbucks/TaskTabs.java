package com.earnapp.helpbucks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.earnapp.adapters.TaskListAdapter;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.models.BidItem;
import com.earnapp.models.TaskItem;
import com.earnapp.models.User;
import com.earnapp.volley.VolleyFeedController;
import com.earnapp.webservice.WebServiceAuthAdpt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskTabs extends Fragment {

    private static final String TAG = TaskTabs.class.getSimpleName();
    private ListView listView;
    private static TaskListAdapter listAdapter;
    private static List<TaskItem> taskItems;


    public TaskTabs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_task_tabs);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private static void parseJsonFeed(JSONArray response) throws ParseException {
        try {
            JSONArray feedArray = response;
            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);
                TaskItem item = new TaskItem();
                item.setId(feedObj.getString(ApplicationConstants.ID));
                item.setTaskTitle(feedObj.getString(ApplicationConstants.TITLE));
                item.setTaskDescription(feedObj.getString(ApplicationConstants.DESCRIPTION));
                //item.setLocation(feedObj.getString(ApplicationConstants.LOCATION));
                JSONObject userJson = feedObj.getJSONObject(ApplicationConstants.OWNER);

                User taskCreator = new User(userJson.getString(ApplicationConstants.ID), userJson.getString(ApplicationConstants.NAME),
                        userJson.getString(ApplicationConstants.USERNAME), userJson.getString(ApplicationConstants.FACEBOOK));
                item.setTaskOwner(taskCreator);
                item.setReward(feedObj.getInt(ApplicationConstants.REWARD));
                item.setExpiry(feedObj.getString(ApplicationConstants.EXPIRY));
                // Adding the bids for each task
                item.setBids(new ArrayList<BidItem>());
                JSONArray bidsJsonList = feedObj.getJSONArray(ApplicationConstants.BIDS);
                for (int j = 0; j < bidsJsonList.length(); j++) {
                    JSONObject bidJson = bidsJsonList.getJSONObject(j);
                    JSONObject bidderJson = bidJson.getJSONObject(ApplicationConstants.BIDDER);
                    User bidderObj = new User(bidderJson.getString(ApplicationConstants.ID),
                            bidderJson.getString(ApplicationConstants.NAME),
                            bidderJson.getString(ApplicationConstants.USERNAME), bidderJson.getString(ApplicationConstants.FACEBOOK));
                    BidItem bidObj = new BidItem(bidJson.getString(ApplicationConstants.ID),
                            bidJson.getInt(ApplicationConstants.AMOUNT), bidderObj,bidJson.getString(ApplicationConstants.CREATED_AT),
                            bidJson.getString(ApplicationConstants.UPDATED_AT));
                    item.getBids().add(bidObj);
                }

                item.setCreatedAt(feedObj.getString(ApplicationConstants.CREATED_AT));
                item.setUpdatedAt(feedObj.getString(ApplicationConstants.UPDATED_AT));
                JSONArray promoters = feedObj.getJSONArray(ApplicationConstants.PROMOTES);
                item.setPromotes(promoters);
                taskItems.add(item);
            }
            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //some code
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_task_tabs, container, false);

        FloatingActionButton createButton = (FloatingActionButton) layout.findViewById(R.id.navigateToCreateUser);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button clicked ! Going to create task !!");
                ((TaskListActivity) getActivity()).getViewPager().setCurrentItem(1);
            }
        });

        listView = (ListView) layout.findViewById(R.id.list);

        taskItems = new ArrayList<TaskItem>();

        listAdapter = new TaskListAdapter(taskItems, this.getActivity());
        listView.setAdapter(listAdapter);

        requestServerAndGetTasks();

        return layout;
    }

    public static void requestServerAndGetTasks() {
        // We first check for cached request
        Cache cache = VolleyFeedController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_GET_TASK);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONArray(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_GET_TASK;
            JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response != null) {
                        try {
                            parseJsonFeed(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                    headers.put("x-access-token", WebServiceAuthAdpt.xAccessToken);
                    headers.put("User-agent", ApplicationConstants.USER_AGENT);
                    return headers;
                }
            };
            // Adding request to volley request queue
            VolleyFeedController.getInstance().addToRequestQueue(jsonReq);
        }
    }

}
