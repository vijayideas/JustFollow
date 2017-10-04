package com.vjy.justfollow.model;

import java.io.File;

/**
 * Created by Vijay Kumar on 03-10-2017.
 */

public class PostCreateData {

    private String userId;

    private String fbAccessToken;

    private String text;
    private String mediaType;
    private File mediaFile;
    private String privacy;


    public PostCreateData() {
    }


    public PostCreateData(String userId, String fbAccessToken, String privacy) {
        this.userId = userId;
        this.fbAccessToken = fbAccessToken;
        this.privacy = privacy;
    }

    public PostCreateData(String userId, String fbAccessToken, String text, String mediaType, File mediaFile, String privacy) {

        super();

        this.userId = userId;
        this.fbAccessToken = fbAccessToken;
        this.text = text;
        this.mediaType = mediaType;
        this.mediaFile = mediaFile;
        this.privacy = privacy;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public File getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(File mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
