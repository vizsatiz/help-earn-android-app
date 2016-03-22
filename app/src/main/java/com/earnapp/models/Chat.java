package com.earnapp.models;

import com.earnapp.constants.ApplicationConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vizsatiz on 19-03-2016.
 */
public class Chat {

    private String message;
    private User chatowner;
    private Date createdAt;
    private Date updatedAt;

    public Chat(User chatowner, String createdAt, String message, String  updatedAt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        this.chatowner = chatowner;
        this.message = message;
        try {
            this.createdAt = dateFormat.parse(createdAt);
            this.updatedAt = dateFormat.parse(updatedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public User getChatowner() {
        return chatowner;
    }

    public void setChatowner(User chatowner) {
        this.chatowner = chatowner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
