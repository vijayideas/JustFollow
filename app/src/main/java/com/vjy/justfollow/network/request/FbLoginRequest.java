package com.vjy.justfollow.network.request;

import android.media.tv.TvContract;
import android.util.Log;

import com.android.volley.VolleyError;
import com.vjy.justfollow.model.FbLoginError;
import com.vjy.justfollow.model.LoginData;
import com.vjy.justfollow.network.helper.CommonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vijay Kumar on 04-05-2017.
 */

public class FbLoginRequest extends CommonRequest {
    private static final String JSON_FIELD_FB_ID = "fbId";
    private static final String JSON_FIELD_FB_TOKEN = "fbToken";


    private LoginData loginData;
    private FbLoginResponseCallback mFbLoginResponseCallback;



    public interface FbLoginResponseCallback {
        void onFbLoginResponse(ResponseCode responseCode, LoginData loginData, FbLoginError error);
    }

    public FbLoginRequest(LoginData loginData, FbLoginResponseCallback cb) {
        super(RequestType.COMMON_REQUEST_FB_LOGIN, CommonRequestMethod.COMMON_REQUEST_METHOD_POST, null);
        this.loginData = loginData;
        mFbLoginResponseCallback = cb;

        Map<String, String> mParams = new HashMap<>();
        mParams.put(JSON_FIELD_FB_ID, loginData.getFbLoginResult().getAccessToken().getUserId());
        mParams.put(JSON_FIELD_FB_TOKEN, loginData.getFbLoginResult().getAccessToken().getToken());
        setParams(mParams);


    }

    @Override
    public void onResponseHandler(JSONObject response) {
//TODO: Need to change parsing as per response from server
        try {
            JSONObject jsonObject = response.getJSONObject("data");
            loginData.setUserId(jsonObject.getString("id"));
            mFbLoginResponseCallback.onFbLoginResponse(ResponseCode.COMMON_RES_SUCCESS, loginData , null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorHandler(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 422) {
            mFbLoginResponseCallback.onFbLoginResponse(ResponseCode.COMMON_RES_FAILED_TO_CONNECT, loginData, new FbLoginError(FbLoginError.ERROR_USER_NOT_FOUND, "User not found"));
        }else {
            mFbLoginResponseCallback.onFbLoginResponse(ResponseCode.COMMON_RES_FAILED_TO_CONNECT, loginData, new FbLoginError(FbLoginError.ERROR_FB_OUTH_FAILD, "Something went wrong"));
        }
        Log.v("onErrorHandler","error is" + error);



    }
}
