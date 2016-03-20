package com.earnapp.constants;

/**
 * Created by vizsatiz on 11-02-2016.
 */
public interface ApplicationConstants {


    //Tokens
    public static final String GOOGLE_MESSEGING_TOKEN  = "key=AIzaSyCe6dwSPR-AA5cKXcp7WXvkYxqyrUVjGgo";

    //Logs
    public static final String TAG_LOGIN  = "Login";
    public static final String TAG_DB_AUTH  = "WebAuth";
    public static final String TAG_TASK = "Task";


    // Basic Auth
    public static final String BASIC_AUTH = "Basic YmFzaWNhdXRoOmJhc2ljYXV0aA==";
    public static final String USER_AGENT = "HelpEarn Android Application";
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";

    // Base URLs
    public static final String FB_GRAPH_BASE_URL = "https://graph.facebook.com";
    public static final String GOOGLE_MESSAGING_UPLOAD = "https://gcm-http.googleapis.com/gcm/send";
    public static final String DB_BASE_URL = "http://192.168.56.1:3000";

    // URL paths
    //FB
    public static final String FB_PROFILE_PIC_URL = "/me/picture";
    //Backend
    public static final String DB_CREATE_USER_URL = "/register/user";
    public static final String DB_GET_TASK  = "/api/v1/task";
    public static final String DB_POST_BID  = "/api/v1/bids";
    public static final String DB_POST_TASK  = "/api/v1/task";
    public static final String DB_GET_MY_TASK  = "/api/v2/task";
    public static final String DB_AUTHENTICATE  = "/authenticate";
    public static final String DB_GET_USER_BY_USERNAME  = "/register/user/";
    public static final String DB_UPDATED_GCM_TOKEN  = "/api/v1/users/";
    public static final String DB_GET_CHAT  = "/api/v1/chats/";


    // URL params
    public static final String ACCESS_TOKEN = "access_token";
    public static final String GCM_TOKEN = "gmctoken";

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
    public static final String PROMOTER = "promoter";

    // User
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FACEBOOK = "facebook";

    // Bids
    public static final String AMOUNT = "amount";
    public static final String BIDDER = "bidder";
    public static final String BID_STATUS = "bidstatus";

    //Auth
    public static final String TOKEN = "token";

    //Reward Slabs
    public static final int SLAB_1 = 400;
    public static final int SLAB_2 = 1000;
    public static final int SLAB_3 = 2000;

}
