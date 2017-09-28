package com.vjy.justfollow.network.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vjy.justfollow.model.FbSignUpData;
import com.vjy.justfollow.network.helper.CommonFileUpload;
import com.vjy.justfollow.network.helper.CommonRequest;
import com.vjy.justfollow.network.helper.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vijay Kumar on 04-05-2017.
 */

public class FbSignUpRequest {

    private static final String JSON_FIELD_FB_ID = "fbId";
    private static final String JSON_FIELD_FB_TOKEN = "fbToken";

    private Context mContext;

    private Map<String, String> mParams;
    private FbSignUpData mSignUpData;

    private CommonFileUpload mFileUpload;

    public interface FbSignUpResponseCallback {
        void onFbSignUpResponse(CommonRequest.ResponseCode res, FbSignUpData data);
    }
    private FbSignUpResponseCallback mSignUpResponseCallback;


    public FbSignUpRequest(Context context, FbSignUpData data, FbSignUpResponseCallback cb) {

        mContext = context;
        mSignUpData = data;
        mParams = new HashMap<>();
        /*mParams.put("Content-Type", "multipart/form-data");*/
        mParams.put("name", data.getName());
        mParams.put("mailId", data.getEmail());
        mParams.put(JSON_FIELD_FB_ID, data.getFbId());
        mParams.put("mobileNumber", data.getMobileNumber());
        mParams.put("dob", data.getBirthDay());
        /*try {
            mParams.put(SignUpRequest.JSON_FIELD_PROFESSION, data.getProfession());
            mParams.put(SignUpRequest.JSON_FIELD_SPECIALITY,data.getmSpeciality());
        }catch (Exception e) {
            e.printStackTrace();
        }*/

//        mParams.put(JSON_FIELD_TYPE, data.getmType());

        mSignUpResponseCallback = cb;
    }


    public void executeRequest() {

        final String url = CommonRequest.DOMAIN + "/api/user/registerWithFacebook";//url for registration with pic

        Log.d("registerWithFacebook", "registerWithFacebook is called");

        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String jsonStr = new String(response.data);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonStr);
                    JSONObject dataObject = new JSONObject(jsonObject.getString("data"));
                    String userID = dataObject.getString("UserId");
                    mSignUpData.setUserId(userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSignUpResponseCallback.onFbSignUpResponse(CommonRequest.ResponseCode.COMMON_RES_SUCCESS, mSignUpData);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               mSignUpResponseCallback.onFbSignUpResponse(CommonRequest.ResponseCode.COMMON_RES_INTERNAL_ERROR, mSignUpData);

            }
        };

        mFileUpload = new CommonFileUpload(mContext, mSignUpData.getImgFile(), CommonFileUpload.FileType.COMMON_UPLOAD_FILE_TYPE_IMAGE,
                "file",url,null,listener,errorListener);


        mFileUpload.setParam(mParams);
        mFileUpload.uploadFile();
    }
}
