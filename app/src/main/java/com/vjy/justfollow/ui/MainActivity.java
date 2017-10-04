package com.vjy.justfollow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.vjy.justfollow.R;

public class MainActivity extends AppCompatActivity{


    private String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateWithToken(AccessToken.getCurrentAccessToken());




    }


    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }else {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    finish();
                }
            },500);
        }else {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }*/
    }


}
