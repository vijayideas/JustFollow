package com.vjy.justfollow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.vjy.justfollow.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }
}
