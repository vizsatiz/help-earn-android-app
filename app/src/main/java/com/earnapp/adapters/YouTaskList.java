package com.earnapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.earnapp.constants.ApplicationConstants;
import com.earnapp.helpbucks.R;

import com.earnapp.models.TaskItem;
import com.earnapp.volley.VolleyFeedController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vizsatiz on 03-03-2016.
 */
public class YouTaskList extends RecyclerView.Adapter<YouTaskList.ViewHolder> {

    private Activity activity;
    private List<TaskItem> taskItem;
    private BidListAdapter bidAdapter;
    ImageLoader imageLoader = VolleyFeedController.getInstance().getImageLoader();

    public void setTaskItem(List<TaskItem> taskItem){
        this.taskItem = taskItem;
    }

    public YouTaskList(Activity activity, ArrayList<TaskItem> bidItems) {
        this.activity = activity;
        this.taskItem = bidItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.you_bid_item_layout,parent,false);
        if (imageLoader == null)
            imageLoader = VolleyFeedController.getInstance().getImageLoader();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TaskItem task = taskItem.get(position);
        holder.txtBidderName.setText(task.getTaskOwner().getName());
        holder.profilePic.setImageUrl(task.getTaskOwner().getProfilePic(), imageLoader);
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(task.getCreatedAt().getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.bidTime.setText(timeAgo);
        holder.title.setText(task.getTaskTitle());

        holder.see_bids_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View inflatedView = layoutInflater.inflate(R.layout.popup_bid_layout, null, false);

                LinearLayout bid_comment_line = (LinearLayout)inflatedView.findViewById(R.id.add_bids_comment_line);
                bid_comment_line.setVisibility(View.GONE);

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

                bidAdapter = new BidListAdapter(activity,task.getBids(),true);
                bidListView.setAdapter(bidAdapter);

                setCloseButtonLogic(inflatedView, popWindow);

                // Number of likes in the popup
                TextView promotesText = (TextView)inflatedView.findViewById(R.id.pop_up_no_of_promotes);
                promotesText.setText(task.getPromotes().length() + " " + ApplicationConstants.PROMOTES);
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

    @Override
    public int getItemCount() {
        return taskItem.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtBidderName;
        public NetworkImageView profilePic;
        public TextView bidTime;
        public TextView title;

        public Button see_bids_button;

        public ViewHolder(View v) {
            super(v);
            txtBidderName = (TextView) v.findViewById(R.id.bidder_name);
            profilePic = (NetworkImageView) v.findViewById(R.id.bid_profile_pic);
            bidTime = (TextView) v.findViewById(R.id.bid_time);
            title = (TextView) v.findViewById(R.id.titletxt);
            see_bids_button = (Button) v.findViewById(R.id.see_all_bids);
        }
    }

    public void add(int position,TaskItem task){
        taskItem.add(position,task);
        notifyItemInserted(position);
    }

    public void add(TaskItem task){
        taskItem.add(task);
        notifyItemInserted(taskItem.size() - 1);
    }

    public void remove(int postion){
        taskItem.remove(postion);
        notifyItemRemoved(postion);
    }
}
