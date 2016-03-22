package com.earnapp.helpbucks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.earnapp.adapters.YouTaskList;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.models.BidItem;
import com.earnapp.models.TaskItem;
import com.earnapp.models.User;
import com.earnapp.webservice.WebServiceAuthAdpt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YouTab extends Fragment {

    private YouTaskList youBidAdpt;
    private View youView;

    public YouTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        youView =  inflater.inflate(R.layout.fragment_profile_tab, container, false);
        RecyclerView youBidListView = (RecyclerView) youView.findViewById(R.id.you_bid_list);
        youBidListView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        youBidListView.setLayoutManager(mLayoutManager);
        ArrayList<TaskItem> tasks = new ArrayList<>();

        getBidsOfCurrentUserAndPutOnToList(youBidListView, tasks);

        youBidAdpt = new YouTaskList(getActivity(),tasks);
        youBidListView.setAdapter(youBidAdpt);
        return youView;
    }

    public void getBidsOfCurrentUserAndPutOnToList(final RecyclerView bidList,final ArrayList<TaskItem> bids){
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_GET_MY_TASK
                   +"/"+ WebServiceAuthAdpt.user.getId();
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    try {
                        parseJsonFeedAndPutToView(response,bidList,bids);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonReq);
    }

    public void parseJsonFeedAndPutToView(JSONArray response,RecyclerView bidList,ArrayList bids) throws ParseException {
        if(response.length() > 0){
            TextView do_not_have_tasks = (TextView)youView.findViewById(R.id.no_task);
            do_not_have_tasks.setVisibility(View.GONE);
        }
        ArrayList<TaskItem> tasks = parseJsonFeed(response);
        youBidAdpt.setTaskItem(tasks);
        youBidAdpt.notifyDataSetChanged();
    }

    private static ArrayList<TaskItem> parseJsonFeed(JSONArray response) throws ParseException {
        ArrayList<TaskItem> tasks = new ArrayList<>();
        try {
            JSONArray feedArray = response;
            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);
                TaskItem item = new TaskItem();
                item.setId(feedObj.getString(ApplicationConstants.ID));
                item.setTaskTitle(feedObj.getString(ApplicationConstants.TITLE));
                item.setTaskDescription(feedObj.getString(ApplicationConstants.DESCRIPTION));
                item.setTaskOwner(WebServiceAuthAdpt.user);
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
                    bidderObj.setGMCToken(bidderJson.getString(ApplicationConstants.GCM_TOKEN));
                    boolean bidstatus = false;
                    try {
                        bidstatus = bidJson.getBoolean(ApplicationConstants.BID_STATUS);
                    }catch (Exception e){
                        //e.printStackTrace();
                    }
                    BidItem bidObj = new BidItem(bidJson.getString(ApplicationConstants.ID),
                            bidJson.getInt(ApplicationConstants.AMOUNT), bidderObj, bidstatus,bidJson.getString(ApplicationConstants.CREATED_AT),
                            bidJson.getString(ApplicationConstants.UPDATED_AT));
                    item.getBids().add(bidObj);
                }

                item.setCreatedAt(feedObj.getString(ApplicationConstants.CREATED_AT));
                item.setUpdatedAt(feedObj.getString(ApplicationConstants.UPDATED_AT));
                JSONArray promoters = feedObj.getJSONArray(ApplicationConstants.PROMOTES);
                item.setPromotes(promoters);
                tasks.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
