package com.example.why.assignment3;

/**
 * Created by Why on 25/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by Why on 22/10/2017.
 */

public class CustomListAdapter_note extends BaseAdapter {
    private Context context; //context
    private ArrayList<Item_note> items; //data source of the list adapter
    static String Access_Token = "3d72a47e431eec93507f14ba166dd55829b96ffe";
    String url, setted;
    //public constructor
    public CustomListAdapter_note(Context context, ArrayList<Item_note> items) {
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
                    inflate(R.layout.list_item_note, parent, false);
        }

        // get current item to be displayed
        final Item_note currentItem = (Item_note) getItem(position);

        // get the TextView for item name and item description
        Button title = (Button)
                convertView.findViewById(R.id.list_item_note_title);
        Button reason = (Button)
                convertView.findViewById(R.id.list_item_note_reason);
        Button time = (Button)
                convertView.findViewById(R.id.list_item_note_time);

        final ImageButton check = (ImageButton)
                convertView.findViewById(R.id.list_item_note_check);

        //sets the text for item name and item description from the current item object
        title.setText(currentItem.title);
        time.setText(currentItem.time);
        reason.setText(currentItem.reason);
        url=currentItem.url;

        new getUrl().execute();
        int resource = currentItem.unread.equals("T")? android.R.drawable.checkbox_off_background:android.R.drawable.checkbox_on_background;
        check.setImageResource(resource);

        if(resource==android.R.drawable.checkbox_off_background){
        check.setOnClickListener(new View.OnClickListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                    @Override
                                                    public void onClick(View v) {
                                                        new mark_read(currentItem.id).execute();
                                                        check.setImageResource(android.R.drawable.checkbox_on_background);

                                                    }
                                                }
        );}

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(setted));
                context.startActivity(intent);
                new mark_read(currentItem.id).execute();
                check.setImageResource(android.R.drawable.checkbox_on_background);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(setted));
                context.startActivity(intent);
                new mark_read(currentItem.id).execute();
                check.setImageResource(android.R.drawable.checkbox_on_background);
            }
        });

        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(setted));
                context.startActivity(intent);

                check.setImageResource(android.R.drawable.checkbox_on_background);
            }
        });

        // returns the view for the current row
        return convertView;
    }

    private class mark_read extends AsyncTask<Void, Void, Void> {

            String id;

            private mark_read(String id) {
                this.id = id;
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
                RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"cs\"\r\n\r\n3d72a47e431eec93507f14ba166dd55829b96ffe\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
                Request request = new Request.Builder()
                        .url("https://api.github.com/notifications/threads/" + id)
                        .patch(body) //870583beb40212e96ae46ab7d122b9133d12f417
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


    /**
     * Async task class to get json by making HTTP call
     */
    private class getUrl extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);

                    setted = c.getString("html_url");

                } catch (final JSONException e) {

                    ((Activity)WebProfile.getAppContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WebProfile.getAppContext(),
                                    "Json parsing error: follower " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                ((Activity)WebProfile.getAppContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WebProfile.getAppContext(),
                                "Follower Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }
}
