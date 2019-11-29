package com.vjy.justfollow.network.helper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Vijay Kumar on 11-09-2017.
 */

public class CustomRequest extends Request<JSONObject> {

    private final Map<String, String> params;
    private final Response.Listener<JSONObject> listener;


    public CustomRequest(String url, Map<String, String> params,
                         Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);

        this.listener = listener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.listener = listener;
        this.params = params;
    }


    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (params != null) {
            JSONObject obj = new JSONObject(params);
            return obj.toString().getBytes();
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    new JSONObject(json),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }




    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}
