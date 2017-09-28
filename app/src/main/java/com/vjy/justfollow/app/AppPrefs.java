package com.vjy.justfollow.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.vjy.justfollow.model.UserCredential;
import com.vjy.justfollow.ui.LoginActivity;

/**
 * Created by Vijay Kumar on 27-09-2017.
 */

public class AppPrefs {

    private static AppPrefs sInstance;
    private SharedPreferences mPrefs;


    // All Shared Preferences Keys
    private static final String KEY_IS_LOGIN = "IsLoggedIn";
    private static final String KEY_LOGIN_DATA = "LoginData";
    private static final String KEY_APP_PREF = "LoginData";


    private AppPrefs(Context paramContext) {
        this.mPrefs = paramContext.getSharedPreferences(KEY_APP_PREF, Context.MODE_PRIVATE);
    }

    public synchronized static AppPrefs getInstance() {
        if (sInstance == null) {
            sInstance = new AppPrefs(AppController.getAppContext());
        }
        return sInstance;
    }

    /**
     * Create login session
     * */
    public void createLoginSession(UserCredential credential){
        SharedPreferences.Editor editor = this.mPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(credential);
        // Storing login value as TRUE
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_LOGIN_DATA, json);
        editor.apply();

    }

    public UserCredential getUserDetails() {
        Gson gson = new Gson();

        String json = this.mPrefs.getString(KEY_LOGIN_DATA, "");

        return gson.fromJson(json, UserCredential.class);
    }

    /**
     * Clear session details
     * */
    public void logOutUser(Activity context){
        SharedPreferences.Editor editor = this.mPrefs.edit();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
        context.finish();
    }
}
