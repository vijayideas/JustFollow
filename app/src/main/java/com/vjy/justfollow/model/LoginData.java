package com.vjy.justfollow.model;

import com.facebook.login.LoginResult;

/**
 * Created by Vijay Kumar on 26-09-2017.
 */

public class LoginData {

    private String mailId;
    private String password;
    private String userId;
    private LoginResult fbLoginResult;

    public LoginData(String mailId, String password) {
        this.mailId = mailId;
        this.password = password;
    }

    public LoginData(LoginResult fbLoginResult) {
        this.fbLoginResult = fbLoginResult;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginResult getFbLoginResult() {
        return fbLoginResult;
    }

    public void setFbLoginResult(LoginResult fbLoginResult) {
        this.fbLoginResult = fbLoginResult;
    }
}
