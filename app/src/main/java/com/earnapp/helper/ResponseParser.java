package com.earnapp.helper;

import android.util.Log;

import com.earnapp.constants.ApplicationConstants;
import com.earnapp.models.BidItem;
import com.earnapp.models.Chat;
import com.earnapp.models.ChatItem;
import com.earnapp.models.TaskItem;
import com.earnapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vizsatiz on 20-03-2016.
 */
public class ResponseParser {

    public static User userJsonParser(JSONObject userJson) throws JSONException {
        User user = new User(userJson.getString(ApplicationConstants.ID),
                userJson.getString(ApplicationConstants.NAME),
                userJson.getString(ApplicationConstants.USERNAME), userJson.getString(ApplicationConstants.FACEBOOK));
        user.setGMCToken(userJson.getString(ApplicationConstants.GCM_TOKEN));
        return user;
    }

    public static Chat chatJsonParser(JSONObject chatJson) throws JSONException {
        return  new Chat(ResponseParser.userJsonParser(chatJson.getJSONObject("chatowner")),chatJson.getString(ApplicationConstants.CREATED_AT),
                chatJson.getString("message"),chatJson.getString(ApplicationConstants.UPDATED_AT));
    }

    public static TaskItem taskJsonParser(JSONObject taskJson) throws JSONException {
        ArrayList<BidItem> bidItems = new ArrayList<>();
        JSONArray bidsJsonList = taskJson.getJSONArray(ApplicationConstants.BIDS);
        try {
            for (int j = 0; j < bidsJsonList.length(); j++) {
                bidItems.add(ResponseParser.bidJsonParser(bidsJsonList.getJSONObject(j)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new TaskItem(userJsonParser(taskJson.getJSONObject(ApplicationConstants.OWNER)),taskJson.getString(ApplicationConstants.TITLE),taskJson.getString(ApplicationConstants.DESCRIPTION),
              taskJson.getInt(ApplicationConstants.REWARD),taskJson.getJSONArray(ApplicationConstants.PROMOTES),taskJson.getString(ApplicationConstants.ID),
              taskJson.getString(ApplicationConstants.EXPIRY),taskJson.getString(ApplicationConstants.CREATED_AT),taskJson.getString(ApplicationConstants.UPDATED_AT),bidItems);
    }

    public static BidItem bidJsonParser(JSONObject bidJson) throws JSONException {
        JSONObject bidderJson = bidJson.getJSONObject(ApplicationConstants.BIDDER);
        boolean bidStatus = false;
        try {
             bidStatus = bidJson.getBoolean(ApplicationConstants.BID_STATUS);
        }catch (Exception e){
            //e.printStackTrace();
        }
        BidItem bidObj = new BidItem(bidJson.getString(ApplicationConstants.ID),
                bidJson.getInt(ApplicationConstants.AMOUNT), ResponseParser.userJsonParser(bidderJson),bidStatus
                ,bidJson.getString(ApplicationConstants.CREATED_AT),
                bidJson.getString(ApplicationConstants.UPDATED_AT));
        return bidObj;
    }

    public static ChatItem chatItemParser(JSONObject chatItemJson) throws JSONException {
        ArrayList<Chat> chatObj = new ArrayList<>();
        JSONArray chats = chatItemJson.getJSONArray("chats");
        // Create array List of chats
        for (int k = 0;k < chats.length(); k++){
            chatObj.add(ResponseParser.chatJsonParser(chats.getJSONObject(k)));
        }
        ChatItem chatItem = new ChatItem(ResponseParser.userJsonParser(chatItemJson.getJSONObject("accepter")),ResponseParser.userJsonParser(chatItemJson.getJSONObject("chatter")),
                chatObj,ResponseParser.taskJsonParser(chatItemJson.getJSONObject("task")),chatItemJson.getString(ApplicationConstants.CREATED_AT),
                chatItemJson.getString(ApplicationConstants.UPDATED_AT));
        Log.d("--->","Returning chatItem");
        return chatItem;
    }
}
