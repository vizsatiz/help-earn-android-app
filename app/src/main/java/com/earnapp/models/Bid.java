package com.earnapp.models;

/**
 * Created by vizsatiz on 11-02-2016.
 */
public class Bid {

    private String id;
    private int amount;
    private User bidder;


    public Bid(String id,int amount,User user) {
        this.id = id;
        this.amount = amount;
        this.bidder = bidder;
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
