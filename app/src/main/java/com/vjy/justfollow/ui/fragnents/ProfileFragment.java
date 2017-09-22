package com.vjy.justfollow.ui.fragnents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import com.vjy.justfollow.R;
import com.vjy.justfollow.app.AppController;
import com.vjy.justfollow.custom_view.CircularNetworkImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    String p = "https://s3-us-west-1.amazonaws.com/com.localapp.profile.image/5908362ac44dc510a4cfe1bc1502333400497";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        CircularNetworkImageView imageView = (CircularNetworkImageView) view.findViewById(R.id.profilePic);

        imageView.setImageUrl(p, AppController.getInstance().getImageLoader());
        return view;
    }

}
