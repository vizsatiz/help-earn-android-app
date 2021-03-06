package com.earnapp.models;

import com.earnapp.constants.ApplicationConstants;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vizsatiz on 08-02-2016.
 */
public class TaskItem {

    private String id;
    private String taskTitle;
    private String taskDescription;
    private int reward;
    private JSONArray promotes;

    private User taskOwner;

    private Date createdAt;
    private Date updatedAt;
    private Date expiry;

    private ArrayList<BidItem> bids;

    public TaskItem(){

    }

    public TaskItem(User taskOwner, String taskTitle, String taskDescription, int reward, JSONArray promotes,String id, String expiry, String createdAt, String updatedAt,
                    ArrayList<BidItem> bids) {
        this.taskOwner = taskOwner;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.reward = reward;
        this.promotes = promotes;
        this.id = id;
        this.bids = bids;
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        try {
            this.createdAt = dateFormat.parse(createdAt);
            this.updatedAt = dateFormat.parse(updatedAt);
            this.expiry = dateFormat.parse(expiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<BidItem> getBids() {
        return bids;
    }

    public void setBids(ArrayList<BidItem> bids) {
        this.bids = bids;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        this.expiry = dateFormat.parse(expiry);
    }

    public User getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(User taskOwner) {
        this.taskOwner = taskOwner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        this.createdAt = dateFormat.parse(createdAt);
    }

    public JSONArray getPromotes() {
        return promotes;
    }

    public void setPromotes(JSONArray promotes) {
        this.promotes = promotes;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        this.updatedAt = dateFormat.parse(updatedAt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int amount) {
        this.reward = amount;
    }
}
