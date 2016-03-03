package com.earnapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.helpbucks.R;
import com.earnapp.models.BidItem;
import com.earnapp.models.TaskItem;
import com.earnapp.volley.VolleyFeedController;
import com.earnapp.webservice.WebServiceAuthAdpt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vizsatiz on 08-02-2016.
 */
public class TaskListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<TaskItem> feedItems;
    private BidListAdapter bidAdapter;
    ImageLoader imageLoader = VolleyFeedController.getInstance().getImageLoader();

    public TaskListAdapter(List<TaskItem> feedItems, Activity currentActivity) {
        this.activity = currentActivity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item_layout, null);

        if (imageLoader == null)
            imageLoader = VolleyFeedController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView title = (TextView) convertView
                .findViewById(R.id.txtTitle);
        TextView description = (TextView) convertView.findViewById(R.id.txtDescription);
        TextView reward = (TextView) convertView.findViewById(R.id.rewardtxt);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        TaskItem item = feedItems.get(position);
        name.setText(item.getTaskOwner().getName());
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(item.getCreatedAt().getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Check for empty status message
        if (!TextUtils.isEmpty(item.getTaskTitle())) {
            title.setText(item.getTaskTitle());
            title.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            title.setVisibility(View.GONE);
        }

        // Check for empty status message
        if (!TextUtils.isEmpty(item.getTaskDescription())) {
            description.setText(item.getTaskDescription());
            description.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            description.setVisibility(View.GONE);
        }

        // Check for empty status message
        reward.setText("" + item.getReward());
        reward.setVisibility(View.VISIBLE);
        this.setRewardColorCode(convertView, item.getReward());


        TextView promotes = (TextView) convertView.findViewById(R.id.txtPromotes);
        promotes.setText(item.getPromotes().length() + "  " + ApplicationConstants.PROMOTES);
        boolean isPromoted = false;
        for (int k = 0; k < item.getPromotes().length(); k++) {
            try {
                if (item.getPromotes().getString(k).equals(WebServiceAuthAdpt.user.getId())) {
                    isPromoted = true;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (isPromoted) {
            ImageView promote_button_image = (ImageView) convertView.findViewById(R.id.promote_image);
            promote_button_image.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_promote_icon_colored));
        } else {
            ImageView promote_button_image = (ImageView) convertView.findViewById(R.id.promote_image);
            promote_button_image.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_single_heart_uncolored));
        }
        // user profile pic
        profilePic.setImageUrl(item.getTaskOwner().getProfilePic(), imageLoader);

        //Set Button Logics
        setPromoteButtonLogic(convertView, item, isPromoted);

        // Set Bid Button Logic
        setBidButtonLogic(convertView,item);

        return convertView;
    }


    public void setRewardColorCode(View view, int amount) {
        ImageView rewardIcon = (ImageView) view.findViewById(R.id.ruppeeSymbol);
        if (amount <= ApplicationConstants.SLAB_1) {
            rewardIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rupee));
        } else if (amount <= ApplicationConstants.SLAB_2) {
            rewardIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rupee_green));
        } else {
            rewardIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rupee_red));
        }
    }

    public void setPromoteButtonLogic(final View feedView, final TaskItem item, final boolean isPromoted) {
        final String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_POST_TASK + "/" + item.getId();
        LinearLayout promoteButton = (LinearLayout) feedView.findViewById(R.id.promote_button);
        promoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isPromoted) {
                    Toast.makeText(activity, "You have already promoted this task",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(ApplicationConstants.PROMOTER, WebServiceAuthAdpt.user.getId());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ApplicationConstants.TAG_TASK, "The Task is liked !!!");
                            TextView txtPromote = (TextView) feedView.findViewById(R.id.txtPromotes);
                            ImageView promote_button_image = (ImageView) feedView.findViewById(R.id.promote_image);
                            promote_button_image.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_promote_icon_colored));
                            txtPromote.setText("" + (item.getPromotes().length() + 1)+"  promotes");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("x-access-token", WebServiceAuthAdpt.xAccessToken);
                            headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                            headers.put("User-agent", ApplicationConstants.USER_AGENT);
                            return headers;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(request);
                }
            }
        });
    }

    public void setBidButtonLogic(View feedView, final TaskItem item){
        LinearLayout bidButton = (LinearLayout) feedView.findViewById(R.id.bid_button);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View inflatedView = layoutInflater.inflate(R.layout.popup_bid_layout, null, false);

                Display display = activity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                final PopupWindow popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 500, true );
                popWindow.setFocusable(true);
                popWindow.setTouchable(true);
                popWindow.setOutsideTouchable(true);

                RecyclerView bidListView = (RecyclerView) inflatedView.findViewById(R.id.my_recycler_view);
                bidListView.setHasFixedSize(true);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                bidListView.setLayoutManager(mLayoutManager);

                bidAdapter = new BidListAdapter(activity,item.getBids());
                bidListView.setAdapter(bidAdapter);

                setCloseButtonLogic(inflatedView, popWindow);

                ImageView bid_button = (ImageView)inflatedView.findViewById(R.id.send_bid);

                bid_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            sendBidImplementation(item,inflatedView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                TextView promotesText = (TextView)inflatedView.findViewById(R.id.pop_up_no_of_promotes);
                promotesText.setText(item.getPromotes().length() + " " + ApplicationConstants.PROMOTES);
                popWindow.showAtLocation(inflatedView, Gravity.CENTER, 0, 80);
            }
        });
    }

    public void setCloseButtonLogic(View popUpView,final PopupWindow popUp){
        ImageView closeButton = (ImageView) popUpView.findViewById(R.id.close_bid_pop_up);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
    }

    public void sendBidImplementation(TaskItem task,View popUp) throws JSONException {
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_POST_BID;
        Map<String,String> params = new HashMap<>();
        TextView amountTxt = (TextView) popUp.findViewById(R.id.bid_amount);
        String bidAmountAsString = "" + amountTxt.getText();
        if(bidAmountAsString != null && !bidAmountAsString.equals("")){
            int bidAmountAsInt = Integer.parseInt(bidAmountAsString);
            params.put("bidder",WebServiceAuthAdpt.user.getId());
            params.put("task",task.getId());
            JSONObject jsonPayload = new JSONObject(params);
            jsonPayload.put("amount",bidAmountAsInt);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,jsonPayload,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                BidItem bid = new BidItem(response.getString(ApplicationConstants.ID),
                                        response.getInt(ApplicationConstants.AMOUNT),WebServiceAuthAdpt.user,
                                        response.getString(ApplicationConstants.CREATED_AT),response.getString(ApplicationConstants.UPDATED_AT) );
                                bidAdapter.add(bid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-access-token", WebServiceAuthAdpt.xAccessToken);
                    headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                    headers.put("User-agent", ApplicationConstants.USER_AGENT);
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(request);
        }else {
            Toast.makeText(activity, "Bid amount should be an amount",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

