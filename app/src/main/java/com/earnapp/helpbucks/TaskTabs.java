package com.earnapp.helpbucks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.earnapp.adapters.TaskListAdapter;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.models.Bid;
import com.earnapp.models.TaskItem;
import com.earnapp.models.User;
import com.earnapp.volley.CustomJsonArrayRequest;
import com.earnapp.volley.CustomRequest;
import com.earnapp.volley.VolleyFeedController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskTabs extends Fragment {

    private static final String TAG = TaskTabs.class.getSimpleName();
    private ListView listView;
    private TaskListAdapter listAdapter;
    private List<TaskItem> taskItems;


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
     * */
    private void parseJsonFeed(JSONArray response) throws ParseException{
        try {
            JSONArray feedArray = response;
            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);
                TaskItem item = new TaskItem();
                item.setId(feedObj.getString(ApplicationConstants.ID));
                item.setTaskTitle(feedObj.getString(ApplicationConstants.TITLE));
                item.setTaskDescription(feedObj.getString(ApplicationConstants.DESCRIPTION));
                item.setLocation(feedObj.getString(ApplicationConstants.LOCATION));
                JSONObject userJson = feedObj.getJSONObject(ApplicationConstants.OWNER);
                User taskCreator = new User(userJson.getString(ApplicationConstants.ID),userJson.getString(ApplicationConstants.NAME),
                        userJson.getString(ApplicationConstants.USERNAME),userJson.getString(ApplicationConstants.FACEBOOK));
                item.setTaskOwner(taskCreator);
                item.setReward(feedObj.getInt(ApplicationConstants.REWARD));
                item.setExpiry(feedObj.getString(ApplicationConstants.EXPIRY));
                // Adding the bids for each task
                item.setBids(new ArrayList<Bid>());
                JSONArray bidsJsonList = feedObj.getJSONArray(ApplicationConstants.BIDS);
                for (int j = 0; j < bidsJsonList.length(); j++) {
                    JSONObject bidJson = bidsJsonList.getJSONObject(j);
                    JSONObject bidderJson = bidJson.getJSONObject(ApplicationConstants.BIDDER);
                    User bidderObj = new User(bidderJson.getString(ApplicationConstants.ID),bidderJson.getString(ApplicationConstants.NAME),
                            bidderJson.getString(ApplicationConstants.USERNAME),bidderJson.getString(ApplicationConstants.FACEBOOK));
                    Bid bidObj = new Bid(bidJson.getString(ApplicationConstants.ID),bidJson.getInt(ApplicationConstants.AMOUNT),bidderObj);
                    item.getBids().add(bidObj);
                }

                item.setCreatedAt(feedObj.getString(ApplicationConstants.CREATED_AT));
                item.setUpdatedAt(feedObj.getString(ApplicationConstants.UPDATED_AT));
                item.setPromotes(feedObj.getInt(ApplicationConstants.PROMOTES));

                taskItems.add(item);

               // item.setName(feedObj.getString("name"));

                // Image might be null sometimes
               // String image = feedObj.isNull("image") ? null : feedObj
                //        .getString("image");
                //item.setImge(image);
                //item.setStatus(feedObj.getString("status"));
                //item.setProfilePic(feedObj.getString("profilePic"));
                //item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                //String feedUrl = feedObj.isNull("url") ? null : feedObj
                  //      .getString("url");
                //item.setUrl(feedUrl);
                //taskItems.add(item);
            }
            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //some code
        FrameLayout layout = (FrameLayout)inflater.inflate(R.layout.fragment_task_tabs, container, false);
        //some code

        listView = (ListView) layout.findViewById(R.id.list);

        taskItems = new ArrayList<TaskItem>();

        listAdapter = new TaskListAdapter(taskItems,this.getActivity());
        listView.setAdapter(listAdapter);

        // We first check for cached request
        Cache cache = VolleyFeedController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(ApplicationConstants.DB_BASE_URL+ApplicationConstants.DB_GET_TASK);
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
            Map<String,String> headers = new HashMap<>();
            headers.put("Content-Type","application/json");
            headers.put("x-access-token","application/json");
            // making fresh volley request and getting json
            CustomJsonArrayRequest jsonReq = new CustomJsonArrayRequest(ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_GET_TASK
                    , headers, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
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
            });
            // Adding request to volley request queue
            VolleyFeedController.getInstance().addToRequestQueue(jsonReq);
        }
        return layout;
    }
}
