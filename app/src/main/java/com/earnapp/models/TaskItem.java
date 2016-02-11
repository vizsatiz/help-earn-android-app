package com.earnapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vizsatiz on 08-02-2016.
 */
public class TaskItem {

    private String id;
    private String taskTitle;
    private String taskDescription;
    private int reward;
    private String location;
    private int promotes;
    private User taskOwner;

    private String createdAt;
    private String updatedAt;
    private String expiry;

    private ArrayList<Bid> bids;

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(User taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getPromotes() {
        return promotes;
    }

    public void setPromotes(int promotes) {
        this.promotes = promotes;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
