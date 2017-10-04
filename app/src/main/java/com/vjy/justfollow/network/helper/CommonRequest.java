package com.vjy.justfollow.network.helper;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vjy.justfollow.app.AppController;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Vijay Kumar on 11-09-2017.
 */

public abstract class CommonRequest {

    public static final String DOMAIN = "http://192.172.4.122:8080";//localhost


    private static final String LOGIN_REQUEST_URL = DOMAIN + "/login";
    private static final String FB_LOGIN_REQUEST_URL = DOMAIN + "/api/user/loginWithFacebook";
    private static final String GET_MY_PROFILE_REQUEST_URL = DOMAIN + "/api/user/me?";
    private static final String POST_LIKE_REQUEST_URL = DOMAIN + "/api/post/like";
    private static final String POST_DISLIKE_REQUEST_URL = DOMAIN + "/api/post/disLike";
    private static final String POST_CREATE_REQUEST_URL = DOMAIN + "/api/user/post/create";




    public enum RequestType {
        COMMON_REQUEST_LOGIN,
        COMMON_REQUEST_FB_LOGIN,
        COMMON_REQUEST_MY_PROFILE,
        COMMON_REQUEST_LIKE_POST,
        COMMON_REQUEST_DISLIKE_POST,
        COMMON_REQUEST_POST_CREATE,

        COMMON_REQUEST_END
    }


    public enum ResponseCode  {
        COMMON_RES_SUCCESS,
        COMMON_RES_INTERNAL_ERROR,
        COMMON_RES_CONNECTION_TIMEOUT,
        COMMON_RES_FAILED_TO_CONNECT,
        COMMON_RES_IMAGE_NOT_FOUND,
        COMMON_RES_SERVER_ERROR_WITH_MESSAGE,
        COMMON_RES_FAILED_TO_UPLOAD,
        COMMON_RES_JSON_PARSING_ERROR,

        COMMON_REQUEST_END // WARNING: Add all request types above this line only
    }

    public enum CommonRequestMethod {
        COMMON_REQUEST_METHOD_GET,
        COMMON_REQUEST_METHOD_POST,

        COMMON_REQUEST_METHOD_END,
        COMMON_REQUEST_METHOD_PUT
    }


    /*---------------------------- Member variables -----------------------------------*/
    private Context mContext;
    private String mURL;
    private CommonRequestMethod mMethod;
    private Map<String, String> mParams;
    private Map<String, String> mPostHeader;
    private JSONObject mJSONParams;
    private RequestType mRequestType;


    public CommonRequest(RequestType type, CommonRequestMethod reqMethod,
                         Map<String, String> mParams) {


        this.mRequestType = type;
        this.mMethod = reqMethod;
        this.mParams = mParams;

        this.mURL = getRequestTypeURL(mRequestType);
    }


    public CommonRequest(Context mContext, RequestType type, CommonRequestMethod reqMethod,
                         Map<String, String> mParams) {


        this.mContext = mContext;
        this.mRequestType = type;
        this.mMethod = reqMethod;
        this.mParams = mParams;
    }


    public abstract void onResponseHandler(JSONObject response);

    public abstract void onErrorHandler(VolleyError error);


    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setParams(Map<String, String> mParams) {
        this.mParams = mParams;
    }

    public void setPostHeader(Map<String, String> mPostHeader) {
        this.mPostHeader = mPostHeader;
    }

    public void setURL(String mURL) {
        this.mURL = mURL;
    }

    public void setMethod(CommonRequestMethod mMethod) {
        this.mMethod = mMethod;
    }

    public void setJSONParams(JSONObject mJSONParams) {
        this.mJSONParams = mJSONParams;
    }

    public void setRequestType(RequestType mRequestType) {
        this.mRequestType = mRequestType;
    }



    protected String getRequestTypeURL(RequestType type) {


        switch (type) {

            case COMMON_REQUEST_LOGIN: return LOGIN_REQUEST_URL;
            case COMMON_REQUEST_FB_LOGIN: return FB_LOGIN_REQUEST_URL;
            case COMMON_REQUEST_MY_PROFILE: return GET_MY_PROFILE_REQUEST_URL;
            case COMMON_REQUEST_LIKE_POST: return POST_LIKE_REQUEST_URL;
            case COMMON_REQUEST_DISLIKE_POST: return POST_DISLIKE_REQUEST_URL;
            case COMMON_REQUEST_POST_CREATE: return POST_CREATE_REQUEST_URL;

            default: return null;
        }
    }




    public void executeRequest() {

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onResponseHandler(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorHandler(error);
            }
        };


//        RequestQueue requestQueue = Volley.newRequestQueue(AppController.getAppContext()); //we recommend to use application context or singleton
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();

        CustomRequest customRequest;

        if (mMethod == CommonRequestMethod.COMMON_REQUEST_METHOD_GET) {
            customRequest = new CustomRequest(mURL, null, listener, errorListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return mPostHeader != null? mPostHeader : super.getHeaders();
                }
            };

            requestQueue.add(customRequest);
        }else {
            int method = mMethod == CommonRequestMethod.COMMON_REQUEST_METHOD_POST ? Request.Method.POST : Request.Method.PUT;

            customRequest = new CustomRequest(method, mURL, mParams, listener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return mPostHeader != null? mPostHeader : super.getHeaders();
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    return mJSONParams != null ? mJSONParams.toString().getBytes() : super.getBody();
                }
            };

            requestQueue.add(customRequest);
        }
    }

}
