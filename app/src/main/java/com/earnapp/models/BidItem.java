package com.earnapp.models;

import com.earnapp.constants.ApplicationConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vizsatiz on 11-02-2016.
 */
public class BidItem {

    private String id;
    private int amount;
    private boolean bidStatus;
    private User bidder;

    private Date createdAt;
    private Date updatedAt;


    public BidItem(String id,int amount,User user,boolean bidStatus,String createdAt,String updatedAt){
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        this.id = id;
        this.amount = amount;
        this.bidder = user;
        this.bidStatus = bidStatus;
        try {
            this.createdAt = dateFormat.parse(createdAt);
            this.updatedAt = dateFormat.parse(updatedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public boolean isBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(boolean bidStatus) {
        this.bidStatus = bidStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
