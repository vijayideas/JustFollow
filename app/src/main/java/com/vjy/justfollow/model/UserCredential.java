package com.vjy.justfollow.model;

/**
 * Created by Vijay Kumar on 27-09-2017.
 */

public class UserCredential {

    private String fbId;
    private String fbAccessToken;


    public UserCredential(String fbId, String fbAccessToken) {
        this.fbId = fbId;
        this.fbAccessToken = fbAccessToken;
    }


    public String getFbId() {
        return fbId;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }
}
