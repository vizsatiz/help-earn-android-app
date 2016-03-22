package com.earnapp.models;

import com.earnapp.constants.ApplicationConstants;
import com.earnapp.webservice.WebServiceAuthAdpt;
import com.google.android.gms.gcm.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vizsatiz on 19-03-2016.
 */
public class ChatItem {

    private User chatter;
    private User acceptor;
    private TaskItem relationalTask;
    private ArrayList<Chat> chats;
    private User chatFriend;

    private Date createdAt;
    private Date updatedAt;

    public ChatItem(User acceptor, User chatter,ArrayList<Chat> chats, TaskItem relationalTask, String createdAt, String updatedAt) {
        this.acceptor = acceptor;
        this.chats = chats;
        this.chatter = chatter;
        this.relationalTask = relationalTask;

        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT);
        try {
            this.createdAt = dateFormat.parse(createdAt);
            this.updatedAt = dateFormat.parse(updatedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.chatFriend = setChatFriend();
    }

    private User setChatFriend(){
        if(this.getChatter().getId().equals(WebServiceAuthAdpt.user.getId())){
            return getAcceptor();
        }else {
            return getChatter();
        }
    }

    public User getChatFriend(){
        return this.chatFriend;
    }

    public User getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(User acceptor) {
        this.acceptor = acceptor;
    }

    public TaskItem getRelationalTask() {
        return relationalTask;
    }

    public void setRelationalTask(TaskItem relationalTask) {
        this.relationalTask = relationalTask;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public User getChatter() {
        return chatter;
    }

    public void setChatter(User chatter) {
        this.chatter = chatter;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
