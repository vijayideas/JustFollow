package com.vjy.justfollow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.vjy.justfollow.R;
import com.vjy.justfollow.ui.fragnents.HomeFragment;
import com.vjy.justfollow.ui.fragnents.NearbyFragment;
import com.vjy.justfollow.ui.fragnents.ProfileFragment;

public class HomeActivity extends AppCompatActivity{

    private static final String HOME_FRAGMENT = "home_fragment";
    private static final String NEARBY_FRAGMENT = "nearby_fragment";
    private static final String PROFILE_FRAGMENT = "profile_fragment";


    private BottomNavigationView navigation;
    private TextView toolbarTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(HOME_FRAGMENT);
                    break;
                case R.id.navigation_dashboard:
                    showFragment(NEARBY_FRAGMENT);
                    break;
                case R.id.navigation_notifications:
                    showFragment(PROFILE_FRAGMENT);

                    break;
            }

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.app_name);

        if (savedInstanceState != null) {
            //TODO: work on arg for landscape mood
        }

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showFragment(HOME_FRAGMENT);

    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = navigation.getMenu().getItem(0);
        if (homeItem.getItemId() != navigation.getSelectedItemId()) {
            showFragment(HOME_FRAGMENT);
            homeItem.setChecked(true);
        }else {
            super.onBackPressed();
        }



    }

    void showFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (tag) {
            case HOME_FRAGMENT:{
                if (fm.findFragmentByTag(HOME_FRAGMENT) != null) {
                    ft.show(fm.findFragmentByTag(HOME_FRAGMENT));
                }else {
                    ft.add(R.id.content, HomeFragment.newInstance(), HOME_FRAGMENT);
                }

                toolbarTitle.setText(R.string.app_name);
            }
            break;
            case NEARBY_FRAGMENT:{
                if (fm.findFragmentByTag(NEARBY_FRAGMENT) != null) {
                    ft.show(fm.findFragmentByTag(NEARBY_FRAGMENT));
                }else {
                    ft.add(R.id.content, NearbyFragment.newInstance(), NEARBY_FRAGMENT);
                }

                toolbarTitle.setText("Nearby");
            }
            break;
            case PROFILE_FRAGMENT:{
                if (fm.findFragmentByTag(PROFILE_FRAGMENT) != null) {
                    ft.show(fm.findFragmentByTag(PROFILE_FRAGMENT));
                }else {
                    ft.add(R.id.content, ProfileFragment.newInstance(), PROFILE_FRAGMENT);
                }

                toolbarTitle.setText("Profile");
            }
            break;
        }

        if (!tag.equals(HOME_FRAGMENT) && fm.findFragmentByTag(HOME_FRAGMENT) != null){
            ft.hide(fm.findFragmentByTag(HOME_FRAGMENT));
        }

        if (!tag.equals(NEARBY_FRAGMENT) && fm.findFragmentByTag(NEARBY_FRAGMENT) != null){
            ft.hide(fm.findFragmentByTag(NEARBY_FRAGMENT));
        }

        if (!tag.equals(PROFILE_FRAGMENT) && fm.findFragmentByTag(PROFILE_FRAGMENT) != null){
            ft.hide(fm.findFragmentByTag(PROFILE_FRAGMENT));
        }


        ft.commit();
    }

}
