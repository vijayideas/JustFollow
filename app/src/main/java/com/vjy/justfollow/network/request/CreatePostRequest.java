package com.vjy.justfollow.network.request;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vjy.justfollow.model.PostCreateData;
import com.vjy.justfollow.network.helper.CommonFileUpload;
import com.vjy.justfollow.network.helper.CommonRequest;
import com.vjy.justfollow.network.helper.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vijay Kumar on 03-10-2017.
 */

public class CreatePostRequest extends CommonRequest {

    private PostCreateData postCreateData;
    private CreatePostResponseCb responseCb;
    private CommonFileUpload mFileUpload;
    private Context mContext;

    private Map<String , String> mParams;



    public interface CreatePostResponseCb {
        void onCreatePostResponse(ResponseCode responseCode, PostCreateData postCreateData);
    }

    public CreatePostRequest(Context mContext, PostCreateData postCreateData, CreatePostResponseCb createPostResponseCb) {
        super(RequestType.COMMON_REQUEST_POST_CREATE, CommonRequestMethod.COMMON_REQUEST_METHOD_POST, null);

        this.mContext = mContext;

        this.responseCb = createPostResponseCb;

        this.postCreateData = postCreateData;

        mParams = new HashMap<>();
        mParams.put("userId", postCreateData.getUserId());
        mParams.put("fbAccessToken", postCreateData.getFbAccessToken());
        mParams.put("text", postCreateData.getText());
        mParams.put("mediaType", "0");
        mParams.put("privacy", postCreateData.getPrivacy());

        super.setParams(mParams);
    }

    @Override
    public void onResponseHandler(JSONObject response) {
        responseCb.onCreatePostResponse(ResponseCode.COMMON_RES_SUCCESS, postCreateData);
    }

    @Override
    public void onErrorHandler(VolleyError error) {

        responseCb.onCreatePostResponse(ResponseCode.COMMON_RES_FAILED_TO_CONNECT, postCreateData);
    }

    @Override
    public void executeRequest() {

        if (postCreateData.getMediaFile() == null) {
            super.executeRequest();
            return;
        }

        final String url = CommonRequest.DOMAIN + "/api/user/post/createWithMedia";//url for registration with pic


        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String jsonStr = new String(response.data);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonStr);

                    onResponseHandler(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = VolleyErrorHelper.getMessage(error);
                onErrorHandler(error);
            }
        };


        mFileUpload = new CommonFileUpload(mContext, postCreateData.getMediaFile(), CommonFileUpload.FileType.COMMON_UPLOAD_FILE_TYPE_IMAGE,
                "file",url,null,listener,errorListener);


        mParams.put("mediaType", "1");
        mFileUpload.setParam(mParams);
        mFileUpload.uploadFile();

    }
}
