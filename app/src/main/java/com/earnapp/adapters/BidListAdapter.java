package com.earnapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.earnapp.helpbucks.R;
import com.earnapp.models.BidItem;
import com.earnapp.volley.VolleyFeedController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vizsatiz on 29-02-2016.
 */
public class BidListAdapter extends RecyclerView.Adapter<BidListAdapter.ViewHolder> {

    private Activity activity;
    private List<BidItem> bidItems;
    private boolean renderButton;
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
        }else{
            // provide Onclick Logic
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

        public ViewHolder(View v) {
            super(v);
            txtBidderName = (TextView) v.findViewById(R.id.bidder_name);
            profilePic = (NetworkImageView) v.findViewById(R.id.bid_profile_pic);
            bidTime = (TextView) v.findViewById(R.id.bid_time);
            bidAmount = (TextView) v.findViewById(R.id.bid_amount_txt);
            acceptButton = (Button) v.findViewById(R.id.accept_bid);
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

    public void remove(int postion){
        bidItems.remove(postion);
        notifyItemRemoved(postion);
    }
}