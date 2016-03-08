package com.earnapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.earnapp.models.User;
import com.earnapp.volley.VolleyFeedController;
import com.earnapp.webservice.WebServiceAuthAdpt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vizsatiz on 29-02-2016.
 */
public class BidListAdapter extends RecyclerView.Adapter<BidListAdapter.ViewHolder> {

    private Activity activity;
    private List<BidItem> bidItems;
    private boolean renderButton;
    private String TAG = "BidListAdapter";
    ImageLoader imageLoader = VolleyFeedController.getInstance().getImageLoader();


    public BidListAdapter(Activity activity, ArrayList<BidItem> bidItems,boolean renderButton) {
        this.activity = activity;
        this.bidItems = bidItems;
        this.renderButton = renderButton;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_list_feed_layout,parent,false);
        if (imageLoader == null)
            imageLoader = VolleyFeedController.getInstance().getImageLoader();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final BidItem bid = bidItems.get(position);
        holder.txtBidderName.setText(bid.getBidder().getName());
        holder.profilePic.setImageUrl(bid.getBidder().getProfilePic(), imageLoader);
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(bid.getCreatedAt().getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.bidTime.setText(timeAgo);
        holder.bidAmount.setText("₹ " + bid.getAmount());
        if(!renderButton){
            holder.acceptButton.setVisibility(View.GONE);
            holder.comment_line.setVisibility(View.GONE);
        }else{
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptBidLogic(bid);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bidItems.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtBidderName;
        public NetworkImageView profilePic;
        public TextView bidTime;
        public TextView bidAmount;
        private Button acceptButton;
        private View comment_line;

        public ViewHolder(View v) {
            super(v);
            txtBidderName = (TextView) v.findViewById(R.id.bidder_name);
            profilePic = (NetworkImageView) v.findViewById(R.id.bid_profile_pic);
            bidTime = (TextView) v.findViewById(R.id.bid_time);
            bidAmount = (TextView) v.findViewById(R.id.bid_amount_txt);
            acceptButton = (Button) v.findViewById(R.id.accept_bid);
            comment_line = (View) v.findViewById(R.id.straight_line);
        }
    }

    public void add(int position,BidItem bid){
        bidItems.add(position,bid);
        notifyItemInserted(position);
    }

    public void add(BidItem bid){
        bidItems.add(bid);
        notifyItemInserted(bidItems.size() - 1);
    }

    public void remove(int position){
        bidItems.remove(position);
        notifyItemRemoved(position);
    }

    public void acceptBidLogic(final BidItem bid){
        String url = ApplicationConstants.DB_BASE_URL + ApplicationConstants.DB_POST_BID + "/" + bid.getId();
        Map<String,String> params = new HashMap<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sendNotificationToBidder(bid.getBidder());
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
    }

    public void sendNotificationToBidder(User bidder) throws JSONException {
        String url = ApplicationConstants.GOOGLE_MESSAGING_UPLOAD;
        JSONObject params = new JSONObject();
        params.put("to",bidder.getGMCToken());
        JSONObject data = new JSONObject();
        data.put(ApplicationConstants.ID,bidder.getId());
        data.put(ApplicationConstants.NAME,bidder.getName());
        params.put("data",data);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity,"Bidder will be notified",
                                Toast.LENGTH_LONG).show();
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
                headers.put("Authorization", ApplicationConstants.GOOGLE_MESSEGING_TOKEN);
                headers.put("Content-Type", ApplicationConstants.CONTENT_TYPE);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }
}