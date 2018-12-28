package com.example.why.assignment3;

/**
 * Created by Why on 25/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;


/**
 * Created by Why on 22/10/2017.
 */

public class CustomListAdapter_follower extends BaseAdapter {
    private Context context; //context
    private ArrayList<Item_user> items; //data source of the list adapter
    String url = "https://api.github.com/user/following/";
    String username;
    //public constructor
    public CustomListAdapter_follower(Context context, ArrayList<Item_user> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item_follower, parent, false);
        }

        // get current item to be displayed
        final Item_user currentItem = (Item_user) getItem(position);

        // get the TextView for item name and item description
        Button textViewItemUsername = (Button)
                convertView.findViewById(R.id.list_item_follower_username);
        ImageView textViewItemAvatar = (ImageView)
                convertView.findViewById(R.id.list_item_follower_avatar);

        //sets the text for item name and item description from the current item object
        textViewItemUsername.setText(currentItem.getItemUsername());

        username = currentItem.getItemUsername();
        url += username;

        //set avatar
        Glide.with(WebProfile.getAppContext())
                .load(currentItem.getItemAvatar())
                .override(200, 200)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(textViewItemAvatar);

        textViewItemUsername.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putParcelable("item_user", currentItem);
                                                        Log.e("profile_button", currentItem.getItemUsername());
                                                        Profile profile = new Profile();
                                                        profile.setArguments(bundle);

                                                        ((Activity) context).getFragmentManager().beginTransaction()
                                                                .replace(R.id.content_frame, profile)
                                                                .commit();
                                                    }
                                                }
        );

        ImageButton follow = (ImageButton)
                convertView.findViewById(R.id.list_item_follower_follow);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {new handleFollow(currentItem.getItemUsername()).execute();
            }
        });

        ImageButton unfollow = (ImageButton)
                convertView.findViewById(R.id.list_item_follower_unfollow);
        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {new handleUnfollow(currentItem.getItemUsername()).execute();
            }
        });

        // returns the view for the current row
        return convertView;
    }

    private class handleFollow extends AsyncTask<Void, Void, Void> {
        String username;

        private handleFollow(String username) {
            this.username = username;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"cs\"\r\n\r\n3d72a47e431eec93507f14ba166dd55829b96ffe\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            Request request = new Request.Builder()
                    .url("https://api.github.com/user/following/" + username)
                    .put(body) //870583beb40212e96ae46ab7d122b9133d12f417
                    .addHeader("authorization", "Bearer 3d72a47e431eec93507f14ba166dd55829b96ffe")
                    .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .addHeader("cs", "3d72a47e431eec93507f14ba166dd55829b96ffe")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "92ae9711-f474-4f08-5257-23107fee4a47")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.e(TAG, "Response: " + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //follow_.setTextColor(Color.parseColor("black"));
            return null;
        }

       /* @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            follow_.setTextColor(Color.parseColor("black"));
        }*/
    }

    private class handleUnfollow extends AsyncTask<Void, Void, Void> {
        String username;

        private handleUnfollow(String username) {
            this.username = username;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"cs\"\r\n\r\n3d72a47e431eec93507f14ba166dd55829b96ffe\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            Request request = new Request.Builder()
                    .url("https://api.github.com/user/following/" + username)
                    .delete(body) //870583beb40212e96ae46ab7d122b9133d12f417
                    .addHeader("authorization", "Bearer 3d72a47e431eec93507f14ba166dd55829b96ffe")
                    .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .addHeader("cs", "3d72a47e431eec93507f14ba166dd55829b96ffe")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "92ae9711-f474-4f08-5257-23107fee4a47")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.e(TAG, "Response: " + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //follow_.setTextColor(Color.parseColor("black"));
            return null;
        }
    }

}
