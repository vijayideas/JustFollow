package com.vjy.justfollow.ui.fragnents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.vjy.justfollow.R;
import com.vjy.justfollow.app.AppController;
import com.vjy.justfollow.app.AppPrefs;
import com.vjy.justfollow.custom_view.CircularNetworkImageView;
import com.vjy.justfollow.model.User;
import com.vjy.justfollow.model.UserCredential;
import com.vjy.justfollow.network.helper.CommonRequest;
import com.vjy.justfollow.network.request.GetMyProfileRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements GetMyProfileRequest.GetMyProfileResponseCallback{


    @BindView(R.id.profilePic)
    CircularNetworkImageView profileImg;

    @BindView(R.id.name)
    TextView nameTv;



    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {

        return new ProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        myProfileRequest();

        // Inflate the layout for this fragment
        return view;
    }


    @OnClick(R.id.editProfileLayout)
    public void editProfile() {
        Toast.makeText(this.getActivity(), "edit Profile", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.logout_layout)
    public void logOut() {
        LoginManager.getInstance().logOut();
        AppPrefs.getInstance().logOutUser(getActivity());

        /*startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();*/
    }

    private void myProfileRequest() {
        UserCredential credential = AppPrefs.getInstance().getUserDetails();
        GetMyProfileRequest request = new GetMyProfileRequest(credential, this);
        request.executeRequest();
    }

    private void setProfileData(User user) {
        nameTv.setText(user.getName());
        profileImg.setImageUrl(user.getPicUrl(), AppController.getInstance().getImageLoader());
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


}
