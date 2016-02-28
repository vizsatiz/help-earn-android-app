package com.earnapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
                if (item.getPromotes().getString(k).equals(WebServiceAuthAdpt.userId)) {
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
                    params.put(ApplicationConstants.PROMOTER, WebServiceAuthAdpt.userId);
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
}

