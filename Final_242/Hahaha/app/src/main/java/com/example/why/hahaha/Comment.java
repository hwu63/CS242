package com.example.why.hahaha;

import java.util.Date;

/**
 * Created by Why on 27/11/2017.
 */

public class Comment {
    private String text;
    private String user;
    private long time;
    public String img_url;

    public Comment(String text, String user) {
        this.text = text;
        this.user = user;

        // Initialize to current time
        time = new Date().getTime();
    }

    public Comment(){

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
