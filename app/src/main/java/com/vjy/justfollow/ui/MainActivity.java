package com.vjy.justfollow.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import com.facebook.AccessToken;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vjy.justfollow.R;
import com.vjy.justfollow.model.FbSignUpData;
import com.vjy.justfollow.utlis.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GraphRequest.GraphJSONObjectCallback{


    private String TAG = MainActivity.class.getSimpleName();

    LoginButton loginButton;
    CallbackManager fbCallbackManager;

    AccessTokenTracker fbTokenTracker;
    ProfileTracker fbProfileTracker;

    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbLoginConfig();

        /*startActivity(new Intent(this,HomeActivity.class));
        finish();*/
    }




    private void fbLoginConfig() {

        loginButton = (LoginButton) findViewById(R.id.login_button);

        List<String> fbPermissions = new ArrayList<>();
        fbPermissions.add(Constants.FB_PERMISSION_PROFILE);
        fbPermissions.add(Constants.FB_PERMISSION_EMAIL);
        fbPermissions.add(Constants.FB_PERMISSION_ABOUT);
        fbPermissions.add(Constants.FB_PERMISSION_BIRTHDAY);
        fbPermissions.add(Constants.FB_PERMISSION_LOCATION);
        fbPermissions.add(Constants.FB_PERMISSION_RELATIONSHIP);
        fbPermissions.add(Constants.FB_PERMISSION_WORK_HISTORY);

        loginButton.setReadPermissions(fbPermissions);



        fbCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(fbCallbackManager, fbCallback);
        LoginManager.getInstance().registerCallback(fbCallbackManager, fbCallback);
        fbTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(com.facebook.AccessToken oldAccessToken, com.facebook.AccessToken currentAccessToken) {
                com.facebook.AccessToken.setCurrentAccessToken(currentAccessToken);
            }
        };

        fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(com.facebook.Profile oldProfile, com.facebook.Profile currentProfile) {
                com.facebook.Profile.setCurrentProfile(currentProfile);

            }
        };
    }



    FacebookCallback<LoginResult> fbCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("facebook","userId: "+loginResult.getAccessToken().getUserId());
            Log.d("facebook","token: "+loginResult.getAccessToken().getToken());

            if (!fbTokenTracker.isTracking()) {
                fbTokenTracker.startTracking();
            }

            fbGraphRequest(loginResult.getAccessToken());

//            onFbLogin(loginResult);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
            Log.e(TAG,error.getMessage());
        }
    };


    private void fbGraphRequest(AccessToken accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday, location, about, relationship_status, work"); // Parámetros que pedimos a facebook
        GraphRequest request = GraphRequest.newMeRequest(accessToken, this);
        request.setParameters(parameters);
        request.executeAsync();
    }




    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {

        String fbId = AccessToken.getCurrentAccessToken().getUserId();
        String picUrl = "https://graph.facebook.com/" + fbId + "/picture?width=1080";

        try {

            FbSignUpData signUpData = new FbSignUpData();



            signUpData.setFbId(fbId);

            if (object.has("name")) {
                signUpData.setName(object.getString("name"));
            }

            if (object.has("email")) {
                signUpData.setEmail(object.getString("email"));
            }

            if (object.has("gender")) {
                signUpData.setGender(object.getString("gender"));
            }

            if (object.has("birthday")) {
                signUpData.setBirthDay(object.getString("birthday"));
            }





            String companyName = null;
            String workLocation;
            String workPosition = null;
//            String fbId = com.facebook.AccessToken.getCurrentAccessToken().getUserId();
//            String fbToken = com.facebook.AccessToken.getCurrentAccessToken().getToken();
//            String fbBirthaday = object.getString("birthday");
//            String fbGender = object.getString("gender");

//            String fbAbout = object.getString("about");
//            String fbRelationship_status = object.getString("relationship_status");



            try {
                JSONArray workArray = object.getJSONArray("work");
                if (workArray.length() != 0){
                    companyName = workArray.getJSONObject(0).getJSONObject("employer").getString("name");
                    workLocation = workArray.getJSONObject(0).getJSONObject("location").getString("name");
                    workPosition = workArray.getJSONObject(0).getJSONObject("position").getString("name");

                }
            }catch (JSONException e){
                e.printStackTrace();
            }


            new DownloadFileFromURL(signUpData).execute(picUrl);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }





    /**
     * Background Async Task to download file
     * */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String fileName;

        private FbSignUpData mSignUpData;

        DownloadFileFromURL(FbSignUpData mSignUpData) {
            this.mSignUpData = mSignUpData;
            this.fileName = System.currentTimeMillis() + "_fb.jpg";
        }


        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output =  openFileOutput(fileName, Context.MODE_PRIVATE);//FileOutputStream("/sdcard/downloadedfile.jpg");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
//            mProgressDialog.dismiss();
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            // Displaying downloaded image into image view
            // Reading image path from sdcard
//            String imagePath = Environment.get + "/downloadedfile.jpg";

            imgFile =  new File(getFilesDir(), fileName);

//            imgFile = new File(imagePath);

            mSignUpData.setImgFile(imgFile);

//            fbSignUpRequest(mSignUpData);


//            Toast.makeText(getContext(), ""+imagePath, Toast.LENGTH_SHORT).show();
            // setting downloaded into image view
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }



}
