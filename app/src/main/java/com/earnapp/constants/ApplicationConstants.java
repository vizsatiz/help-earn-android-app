package com.earnapp.constants;

/**
 * Created by vizsatiz on 11-02-2016.
 */
public interface ApplicationConstants {

    //Logs
    public static final String TAG_LOGIN  = "Login";
    public static final String TAG_DB_AUTH  = "WebAuth";

    // Basic Auth
    public static final String BASIC_AUTH = "Basic YmFzaWNhdXRoOmJhc2ljYXV0aA==";
    public static final String USER_AGENT = "HelpEarn Android Application";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

    // Base URLs
    public static final String FB_GRAPH_BASE_URL = "https://graph.facebook.com";
    public static final String DB_BASE_URL = "http://10.10.17.167:3000";

    // URL paths
    //FB
    public static final String FB_PROFILE_PIC_URL = "/me/picture";
    //Backend
    public static final String DB_CREATE_USER_URL = "/register/user";
    public static final String DB_GET_TASK  = "/api/v1/task";
    public static final String DB_AUTHENTICATE  = "/authenticate";
    public static final String DB_GET_USER_BY_USERNAME  = "/register/user/";


    // URL params
    public static final String ACCESS_TOKEN = "access_token";

    // General
    public static final String ID = "_id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    // Task
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";
    public static final String REWARD = "reward";

    public static final String EXPIRY = "expiry";
    public static final String BIDS = "bids";
    public static final String TITLE = "title";
    public static final String OWNER = "owner";
    public static final String PROMOTES = "promotes";

    // User
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FACEBOOK = "facebook";

    // Bids
    public static final String AMOUNT = "amount";
    public static final String BIDDER = "bidder";

    //Auth
    public static final String TOKEN = "token";



}
