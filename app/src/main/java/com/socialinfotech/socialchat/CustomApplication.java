package com.socialinfotech.socialchat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by viniciusthiengo on 3/14/16.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
