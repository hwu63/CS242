package com.example.why.assignment3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Why on 24/10/2017.
 **/

public class Item_note implements Parcelable {

    public String title;
    public String time;
    public String url;
    public String reason;
    public String unread;
    public String id;


    public Item_note(      String title,
                           String time,
                           String url,
                           String reason,
                           String unread,
                           String id){
        this.title = title;
        this.time = time;
        this.url = url;
        this.reason = reason;
        this.unread = unread;
        this.id = id;

    }

    // Parcelling part
    public Item_note(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.title = data[0];
        this.time = data[1];
        this.url = data[2];
        this.reason = data[3];
        this.unread = data[4];
        this.id = data[5];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(
                new String[] {
                        this.title,
                        this.time,
                        this.url,
                        this.reason,
                        this.unread,
                        this.id
                });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item_note createFromParcel(Parcel in) {
            return new Item_note(in);
        }

        public Item_note[] newArray(int size) {
            return new Item_note[size];
        }
    };
}