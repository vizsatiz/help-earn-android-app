package com.earnapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.earnapp.helpbucks.R;
import com.earnapp.models.ChatItem;
import com.earnapp.volley.VolleyFeedController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vizsatiz on 19-03-2016.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<ChatItem> chatItems;
    private String TAG = "ChatListAdapter";
    private Activity activity;
    ImageLoader imageLoader = VolleyFeedController.getInstance().getImageLoader();

    public List<ChatItem> getChatItems() {
        return chatItems;
    }

    public void setChatItems(List<ChatItem> chatItems) {
        this.chatItems = chatItems;
    }

    public ChatListAdapter(Activity activity, ArrayList<ChatItem> chatItems){
        this.activity = activity;
        this.chatItems = chatItems;
    }

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_chat_list_layout, parent, false);
        if (imageLoader == null)
            imageLoader = VolleyFeedController.getInstance().getImageLoader();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.ViewHolder holder, int position) {
        final ChatItem item = chatItems.get(position);
        holder.txtChatterName.setText(item.getChatFriend().getName());
        holder.profilePic.setImageUrl(item.getChatFriend().getProfilePic(), imageLoader);
        holder.chat_subject.setText(item.getRelationalTask().getTaskTitle());

    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtChatterName;
        public NetworkImageView profilePic;
        public TextView chat_subject;
        private LinearLayout chatButton;

        public ViewHolder(View v) {
            super(v);
            txtChatterName = (TextView) v.findViewById(R.id.chatter_name);
            profilePic = (NetworkImageView) v.findViewById(R.id.chat_profile_pic);
            chat_subject = (TextView) v.findViewById(R.id.chat_subject);
            chatButton = (LinearLayout) v.findViewById(R.id.start_chat);
        }
    }

    public void add(int position,ChatItem chatItem){
        chatItems.add(position,chatItem);
        notifyItemInserted(position);
    }

    public void add(ChatItem chatItem) {
        chatItems.add(chatItem);
        notifyItemInserted(chatItems.size() - 1);
    }

    public void remove(int position){
        chatItems.remove(position);
        notifyItemRemoved(position);
    }
}