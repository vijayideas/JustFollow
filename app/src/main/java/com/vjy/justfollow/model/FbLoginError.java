package com.vjy.justfollow.model;

/**
 * Created by Vijay Kumar on 04-05-2017.
 */

public class FbLoginError {
    public static final int ERROR_USER_NOT_FOUND = 0;
    public static final int ERROR_FB_OUTH_FAILD = 1;





    private int statusCode;
    private String errorMessage;

    public FbLoginError(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
