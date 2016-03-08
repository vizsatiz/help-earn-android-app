package com.earnapp.models;

import com.earnapp.constants.ApplicationConstants;

/**
 * Created by vizsatiz on 11-02-2016.
 */
public class User {

    private String id;
    private String name;
    private String username;
    private String facebookToken;
    private String gmcToken;

    public User(String id,String name,String username,String facebooktoken){
        this.id = id;
        this.name = name;
        this.username = username;
        this.facebookToken = facebooktoken;
    }

    public String getGMCToken(){
        return this.gmcToken;
    }

    public void setGMCToken(String gmcToken){
        this.gmcToken = gmcToken;
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

    /**
     * The String Url to get facebook profile pic
     *
     * @return
     */
    public String getProfilePic(){
        return ApplicationConstants.FB_GRAPH_BASE_URL + ApplicationConstants.FB_PROFILE_PIC_URL+"?"
                +ApplicationConstants.ACCESS_TOKEN+"="+this.getFacebookToken();
    }
}
