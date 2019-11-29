package com.vjy.justfollow.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.vjy.justfollow.R;
import com.vjy.justfollow.app.AppPrefs;
import com.vjy.justfollow.model.PostCreateData;
import com.vjy.justfollow.model.UserCredential;
import com.vjy.justfollow.network.helper.CommonRequest;
import com.vjy.justfollow.network.request.CreatePostRequest;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePostActivity extends AppCompatActivity implements CreatePostRequest.CreatePostResponseCb{

    private static final int REQUEST_CODE_PICKER = 122;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.privacy_spinner)
    Spinner privacySpinner;

    @BindView(R.id.status_et)
    EditText statusEditText;

    @BindView(R.id.fab_picker)
    FloatingActionButton picker;

    @BindView(R.id.post_iv)
    ImageView postImageView;

    private ProgressDialog mProgressDialog;

    private PostCreateData postCreateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ButterKnife.bind(this);

        UserCredential credential = AppPrefs.getInstance().getUserDetails();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait");

        postCreateData = new PostCreateData(credential.getUserId(), credential.getFbAccessToken(), "0");

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (postCreateData != null) {
                    postCreateData.setPrivacy(""+position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @OnClick(R.id.btn_close)
    public void clowsWindow() {
        finish();
    }

    @OnClick(R.id.fab_picker)
    public void picker () {
        imagePicker();
    }


    @OnClick(R.id.btn_post)
    public void postCreastRequest() {
        if (statusEditText.getText().toString().isEmpty() && postCreateData.getMediaFile() == null) {
            statusEditText.setHintTextColor(getResources().getColor(R.color.btn_context_menu_text_red));
            return;
        }

        statusEditText.setHintTextColor(getResources().getColor(R.color.white));
        postCreateData.setText(statusEditText.getText().toString());

        CreatePostRequest createPostRequest = new CreatePostRequest(this, postCreateData, this);
        createPostRequest.executeRequest();
        mProgressDialog.show();
    }

    @Override
    public void onCreatePostResponse(CommonRequest.ResponseCode responseCode, PostCreateData postCreateData) {

        mProgressDialog.dismiss();

        if (responseCode == CommonRequest.ResponseCode.COMMON_RES_SUCCESS) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("post", 1);
            setResult(200,returnIntent);
            finish();
        }




    }


    private void imagePicker() {

        ImagePicker imagePicker = ImagePicker.create(this)
                .theme(R.style.AppTheme_NoActionBar)
                .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select"); // image selection title


        imagePicker.single().start(REQUEST_CODE_PICKER);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);

            if (images != null && !images.isEmpty()){
                Image image =  images.get(0);
                postImageView.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
                postCreateData.setPrivacy("1");
                postCreateData.setMediaFile(new File(image.getPath()));

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
