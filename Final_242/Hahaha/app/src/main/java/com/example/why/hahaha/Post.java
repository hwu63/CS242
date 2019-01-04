package com.example.why.hahaha;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Why on 04/12/2017.
 */

public class Post implements Parcelable {
        public String Title;
        public String Question;
        public String Owner;
        public String post_no;
        public String comment_no;

        public Post(String Title, String Content, String Owner, Integer post_no) {
            this.Title = Title;
            this.Question = Content;
            this.Owner = Owner;
            this.post_no = post_no.toString();
            comment_no = "0";
        }

    // Parcelling part
    public Post(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Title = data[0];
        this.Question = data[1];
        this.Owner = data[2];
        this.post_no = data[3];
        this.comment_no = data[4];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(
                new String[] {
                        this.Title,
                        this.Question,
                        this.Owner,
                        this.post_no,
                        this.comment_no
                });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
