package com.example.why.assignment3;

import android.app.Application;
import android.content.Context;

/**
 * Created by Why on 22/10/2017.
 */

public class WebProfile extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        WebProfile.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return WebProfile.context;
    }

}
