package com.vjy.justfollow.model;

import java.io.File;

/**
 * Created by Vijay Kumar on 22-09-2017.
 */

public class FbSignUpData {


    private String name;
    private String fbId;
    private String email;
    private String gender;
    private String birthDay;
    private File imgFile;


    public FbSignUpData() {
    }

    public FbSignUpData(String name, String fbId, String email, String gender, File imgFile) {
        this.name = name;
        this.fbId = fbId;
        this.email = email;
        this.gender = gender;
        this.imgFile = imgFile;
    }


    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
}
