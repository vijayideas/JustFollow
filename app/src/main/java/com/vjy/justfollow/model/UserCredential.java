package com.vjy.justfollow.model;

/**
 * Created by Vijay Kumar on 27-09-2017.
 */

public class UserCredential {

    private String fbId;
    private String fbAccessToken;
    private String userId;


    public UserCredential(String fbId, String fbAccessToken) {
        this.fbId = fbId;
        this.fbAccessToken = fbAccessToken;
    }

    public UserCredential(String fbId, String fbAccessToken, String userId) {
        this.fbId = fbId;
        this.fbAccessToken = fbAccessToken;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFbId() {
        return fbId;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }
}
