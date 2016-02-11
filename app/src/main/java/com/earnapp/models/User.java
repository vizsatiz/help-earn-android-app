package com.earnapp.models;

/**
 * Created by vizsatiz on 11-02-2016.
 */
public class User {

    private String id;
    private String name;
    private String username;
    private String facebookToken;

    public User(String id,String name,String username,String facebooktoken){
        this.id = id;
        this.name = name;
        this.username = username;
        this.facebookToken = facebooktoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getProfilePic(){
        return null;
    }
}
