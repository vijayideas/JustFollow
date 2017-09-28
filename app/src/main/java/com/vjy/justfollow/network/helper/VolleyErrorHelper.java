package com.vjy.justfollow.network.helper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Vijay Kumar on 11-09-2017.
 */

public class VolleyErrorHelper {

    public static final String COMMON_NETWORK_ERROR_TIMEOUT = "timeout";
    public static final String COMMON_NETWORK_ERROR_NO_INTERNET = "no internet connection";
    public static final String COMMON_NETWORK_ERROR_UNKNOWN = "unknown";


    public static String getMessage (Object error){
        if(error instanceof TimeoutError){
            return COMMON_NETWORK_ERROR_TIMEOUT;
        }else if (isServerProblem(error)){
            return handleServerError(error);

        }else if(isNetworkProblem(error)){
            return COMMON_NETWORK_ERROR_NO_INTERNET;
        }
        return COMMON_NETWORK_ERROR_UNKNOWN;

    }


    private static String handleServerError(Object error) {

        VolleyError er = (VolleyError)error;
        NetworkResponse response = er.networkResponse;
        if(response != null){
            switch (response.statusCode){

                case 404:
                case 422:
                case 401:
                    String trimmedString;
                    try {
                        String json;
                        json = new String(response.data);
                        JSONObject obj = new JSONObject(json);
                        trimmedString = obj.getString("message");
                        return trimmedString;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                default:
                    return COMMON_NETWORK_ERROR_TIMEOUT;
            }
        }

        return COMMON_NETWORK_ERROR_UNKNOWN;
    }


    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private static boolean isNetworkProblem (Object error){
        return error instanceof NetworkError;
    }
}
