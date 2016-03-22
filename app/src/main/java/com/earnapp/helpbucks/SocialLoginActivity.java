package com.earnapp.helpbucks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.earnapp.constants.ApplicationConstants;
import com.earnapp.webservice.WebServiceAuthAdpt;
import com.earnapp.webservice.WebServiceUserAdpt;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class SocialLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private AccessTokenTracker mAccessTokenTracker;
    private ProfileTracker mProfileTracker;
    private WebServiceAuthAdpt authAdpt;
    private String TAG = ApplicationConstants.TAG_LOGIN;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializing FaceBook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        this.mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d(TAG,"Inside OnAccessToken Changed");
            }
        };
        mAccessTokenTracker.startTracking();

        if(Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    mProfileTracker.stopTracking();
                    updateWithTokenAndProfile(profile2,false);
                }
            };
            mProfileTracker.startTracking();
        }else {
            Profile profile = Profile.getCurrentProfile();
            updateWithTokenAndProfile(profile,false);
        }



        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    private ProfileTracker mProfileTracker;
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        if(Profile.getCurrentProfile() == null) {
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    mProfileTracker.stopTracking();
                                    Log.d(TAG, "Successfull Login !!");
                                    updateWithTokenAndProfile(profile2,true);
                                }
                            };
                            mProfileTracker.startTracking();
                        }else {
                            Profile profile = Profile.getCurrentProfile();
                            Log.d(TAG, "Successfull Login !!");
                            updateWithTokenAndProfile(profile,true);
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Login cancelled onCancel!!");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                    }
                });
        setContentView(R.layout.activity_social_login);
    }

    /**
     * This function checks wether the current token is l;
     *
     * @param profile
     */
    private void updateWithTokenAndProfile(Profile profile,boolean isFirstLogin) {
        if(profile!= null){
            // creating user and authenticating webservice
            if(isFirstLogin){
               // create user
                Log.d(TAG,"User Logging in first time !!! Going to check the server !!");
                WebServiceUserAdpt userApdt = new WebServiceUserAdpt(this);
                userApdt.checkForExistingUser(profile.getId(),"password",AccessToken.getCurrentAccessToken().getToken(),
                        profile.getFirstName()+" "+profile.getLastName());
            }else{
                Log.d(TAG,"User opens the App !!! Going to web service login !!");
                authAdpt = new WebServiceAuthAdpt(this);
                authAdpt.updateAndAuthenticateUser(profile.getId(),"password",AccessToken.getCurrentAccessToken().getToken()
                        ,profile.getFirstName()+" "+profile.getLastName());
            }
        }
        else {
            Log.e(TAG,"Unable to access user profile from facebook !!!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.'
        //if(authAdpt == null){
          //  updateWithTokenAndProfile(Profile.getCurrentProfile(),false);
        //}
        //authAdpt.registerReceiver();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(authAdpt.mRegistrationBroadcastReceiver);
        //authAdpt.isReceiverRegistered = false;
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mAccessTokenTracker.stopTracking();
        super.onDestroy();
    }
}