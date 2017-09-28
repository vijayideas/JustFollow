package com.vjy.justfollow.ui.fragnents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vjy.justfollow.R;
import com.vjy.justfollow.adapter.FeedRecyclerAdapter;
import com.vjy.justfollow.app.AppController;
import com.vjy.justfollow.app.AppPrefs;
import com.vjy.justfollow.custom_view.CircularNetworkImageView;
import com.vjy.justfollow.custom_view.FeedContextMenu;
import com.vjy.justfollow.custom_view.FeedContextMenuManager;
import com.vjy.justfollow.model.FeedItem;
import com.vjy.justfollow.model.User;
import com.vjy.justfollow.model.UserCredential;
import com.vjy.justfollow.network.helper.CommonRequest;
import com.vjy.justfollow.network.request.GetMyProfileRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements FeedRecyclerAdapter.OnFeedItemClickListener,
        GetMyProfileRequest.GetMyProfileResponseCallback, FeedContextMenu.OnFeedContextMenuItemClickListener{

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.recycler)
    RecyclerView listView;

    @BindView(R.id.profilePic)
    CircularNetworkImageView profilePic;


    private FeedRecyclerAdapter listAdapter;
    private List<FeedItem> feedItems;
//    private String URL_FEED = "https://api.androidhive.info/feed/feed.json";
    private String URL_FEED = CommonRequest.DOMAIN + "/api/post/allPost";


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        myProfileRequest();
        initUi();
        // Inflate the layout for this fragment
        return view;
    }


    void initUi() {


        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setItemAnimator(new DefaultItemAnimator());

        listView.setNestedScrollingEnabled(false);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedRecyclerAdapter(feedItems);
        listAdapter.setOnFeedItemClickListener(this);
        listView.setAdapter(listAdapter);




        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null ) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }


            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);

    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        /*try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImage(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        try {
            JSONArray feedArray = response.getJSONArray("data");
            feedItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getString("id"));
                item.setName(feedObj.getString("userName"));

                // Image might be null sometimes
                String image = feedObj.isNull("mediaFile") ? null : feedObj
                        .getString("mediaFile");
                item.setImage(image);
                item.setStatus(feedObj.getString("text"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("createdDate"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void myProfileRequest() {
        UserCredential credential = AppPrefs.getInstance().getUserDetails();
        GetMyProfileRequest request = new GetMyProfileRequest(credential, this);
        request.executeRequest();
    }

    private void setProfileData(User user) {
        profilePic.setImageUrl(user.getPicUrl(), AppController.getInstance().getImageLoader());
    }

    @Override
    public void onCommentsClick(View v, int position) {

    }

    @Override
    public void onMoreClick(View v, int position) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, position, this);
    }

    @Override
    public void onProfileClick(View v) {

    }

    @Override
    public void GetMyProfileResponse(CommonRequest.ResponseCode responseCode, User user) {
        if (responseCode == CommonRequest.ResponseCode.COMMON_RES_SUCCESS) {
            setProfileData(user);
        }else {
            if (user != null) {
                setProfileData(user);
            }
        }
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }
}
