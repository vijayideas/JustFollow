package com.vjy.justfollow.model;

/**
 * Created by Vijay Kumar on 27-09-2017.
 */

public class User {

    private String id;
    private String name ;
    private String mailId;
    private String mobileNumber;
    private String userId ;
    private String fbId ;
    private String picUrl;
    private String dateOfBirth;


    public User() {
    }


    public User(String id, String name, String mailId, String mobileNumber, String userId, String fbId, String picUrl, String dateOfBirth) {
        this.id = id;
        this.name = name;
        this.mailId = mailId;
        this.mobileNumber = mobileNumber;
        this.userId = userId;
        this.fbId = fbId;
        this.picUrl = picUrl;
        this.dateOfBirth = dateOfBirth;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String  getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String  dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
