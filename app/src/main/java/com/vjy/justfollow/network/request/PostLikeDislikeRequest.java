package com.vjy.justfollow.network.request;

import android.util.Log;

import com.android.volley.VolleyError;
import com.vjy.justfollow.app.AppPrefs;
import com.vjy.justfollow.network.helper.CommonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vijay Kumar on 29-09-2017.
 */

public class PostLikeDislikeRequest extends CommonRequest {



    public PostLikeDislikeRequest(String postId, boolean like) {
        super(like ? RequestType.COMMON_REQUEST_LIKE_POST : RequestType.COMMON_REQUEST_DISLIKE_POST, CommonRequestMethod.COMMON_REQUEST_METHOD_POST, null);

        Map<String, String> mParams = new HashMap<>();
        mParams.put("fbAccessToken", AppPrefs.getInstance().getUserDetails().getFbAccessToken());
        mParams.put("postId", postId);


        super.setParams(mParams);
    }

    @Override
    public void onResponseHandler(JSONObject response) {

        Log.i("LikeDisLike", "Success");
    }

    @Override
    public void onErrorHandler(VolleyError error) {
        Log.e("LikeDisLike", "Failed");
    }
}
