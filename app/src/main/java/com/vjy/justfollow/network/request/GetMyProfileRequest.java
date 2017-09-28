package com.vjy.justfollow.network.request;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.vjy.justfollow.app.AppController;
import com.vjy.justfollow.model.User;
import com.vjy.justfollow.model.UserCredential;
import com.vjy.justfollow.network.helper.CommonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Vijay Kumar on 27-09-2017.
 */

public class GetMyProfileRequest extends CommonRequest {

    public interface GetMyProfileResponseCallback {
        void GetMyProfileResponse(ResponseCode responseCode, User user);
    }

    private GetMyProfileResponseCallback responseCallback;
    private String url;

    public GetMyProfileRequest(UserCredential credential, GetMyProfileResponseCallback cb) {
        super(RequestType.COMMON_REQUEST_MY_PROFILE, CommonRequestMethod.COMMON_REQUEST_METHOD_GET, null);
        this.responseCallback = cb;

        url = getRequestTypeURL(RequestType.COMMON_REQUEST_MY_PROFILE);
        url += "fbAccessToken=" + credential.getFbAccessToken();

        setURL(url);


    }


    private User getCache() {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry == null) return null;

        // fetch the data from cache
        try {
            String data = new String(entry.data, "UTF-8");
            try {
                return parseResponse(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();

                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return null;
        }
    }


    private User parseResponse(JSONObject response) {
        JSONObject object;
        User user;
        try {
            object = response.getJSONObject("data");
            user = new User();

            if (object.has("id")) {
                user.setId(object.getString("id"));
            }

            if (object.has("name")) {
                user.setName(object.getString("name"));
            }

            if (object.has("mailId")) {
                user.setMailId(object.getString("mailId"));
            }

            if (object.has("mobileNumber") && !object.getString("mobileNumber").equals("null")) {
                user.setMobileNumber(object.getString("mobileNumber"));
            }

            if (object.has("fbId")) {
                user.setFbId(object.getString("fbId"));
            }

            if (object.has("picUrl")) {
                user.setPicUrl(object.getString("picUrl"));
            }

            if (object.has("dateOfBirth")) {
                user.setDateOfBirth(object.getString("dateOfBirth"));
            }


            return user;

        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void executeRequest() {
        super.executeRequest();
    }



    @Override
    public void onResponseHandler(JSONObject response) {

        User user = parseResponse(response);

        if (user != null) {
            responseCallback.GetMyProfileResponse(ResponseCode.COMMON_RES_SUCCESS, user);
        }else {
            responseCallback.GetMyProfileResponse(ResponseCode.COMMON_RES_JSON_PARSING_ERROR, null);
        }
    }

    @Override
    public void onErrorHandler(VolleyError error) {
        responseCallback.GetMyProfileResponse(ResponseCode.COMMON_RES_FAILED_TO_CONNECT, getCache());
    }
}
