package com.example.why.hahaha;

import android.app.Application;
import android.content.Context;

/**
 * Created by Why on 04/12/2017.
 */

public class final_242 extends Application {

        private static Context context;


        public void onCreate() {
            super.onCreate();
            final_242.context = getApplicationContext();
        }

        public static Context getAppContext() {
            return final_242.context;
        }

    private int post_no;

    public int getPost_no() {
        return post_no;
    }

    public void setPost_no(int post_no) {
        this.post_no=post_no;
    }

    public void incrementPost_no() {
        post_no++;
    }

    }

