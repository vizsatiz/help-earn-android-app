package com.earnapp.helpbucks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        // Initializing FaceBook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        this.mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("login","onCurrentAccessTokenChanged");
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
                                    Log.d("login", "login Success inside ");
                                    updateWithTokenAndProfile(profile2,true);
                                }
                            };
                            mProfileTracker.startTracking();
                        }else {
                            Profile profile = Profile.getCurrentProfile();
                            Log.d("login", "login Success outside ");
                            updateWithTokenAndProfile(profile,true);
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
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
                WebServiceUserAdpt userApdt = new WebServiceUserAdpt(this);
                userApdt.checkForExistingUser(profile.getId());
            }else{
                WebServiceAuthAdpt authAdpt = new WebServiceAuthAdpt(this);
                authAdpt.updateAndAuthenticateUser(profile.getId(),"password");
            }
        }
        else {
            Log.d("login","Error profile is null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
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