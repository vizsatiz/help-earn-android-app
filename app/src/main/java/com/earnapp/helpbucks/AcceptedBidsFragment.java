package com.earnapp.helpbucks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.earnapp.adapters.ChatListAdapter;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.helper.ResponseParser;
import com.earnapp.models.Chat;
import com.earnapp.models.ChatItem;
import com.earnapp.webservice.WebServiceAuthAdpt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AcceptedBidsFragment extends Fragment {

    private String TAG = ApplicationConstants.TAG_TASK;
    private ChatListAdapter chatObjectListAdpt;

    public AcceptedBidsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_accepted_chats, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.chats_object_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        ArrayList<ChatItem> chatItems = new ArrayList<>();

        getAcceptedBidsAndBidderAndMapItToTheListView(fragmentView);

        chatObjectListAdpt = new ChatListAdapter(getActivity(),chatItems);
        recyclerView.setAdapter(chatObjectListAdpt);
        return  fragmentView;
    }

    public void getAcceptedBidsAndBidderAndMapItToTheListView(View fragmentView){
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_GET_CHAT + WebServiceAuthAdpt.user.getId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseChatsResponseAndBindToUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                headers.put("x-access-token", WebServiceAuthAdpt.xAccessToken);
                headers.put("User-agent", ApplicationConstants.USER_AGENT);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void parseChatsResponseAndBindToUI(JSONArray response){
        ArrayList<ChatItem> chatItems = new ArrayList<>();
        for(int i = 0;i< response.length();i++){
            try {
                    ArrayList<Chat> chatObj = new ArrayList<>();
                    JSONObject chatItemJson = response.getJSONObject(i);
                    chatItems.add(ResponseParser.chatItemParser(chatItemJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("------>", "Setting chat Item" + chatItems.size());
        chatObjectListAdpt.setChatItems(chatItems);
        chatObjectListAdpt.notifyDataSetChanged();
    }

}
