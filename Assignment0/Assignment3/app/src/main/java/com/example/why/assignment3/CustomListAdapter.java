package com.example.why.assignment3;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


/**
 * Created by Why on 22/10/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Item> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, ArrayList<Item> items) {
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
                    inflate(R.layout.list_item, parent, false);
        }

        // get current item to be displayed
        final Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
        Button textViewItemName = (Button)
                convertView.findViewById(R.id.list_item_name);
        Button textViewItemUsername = (Button)
                convertView.findViewById(R.id.list_item_username);
        Button textViewItemDescription = (Button)
                convertView.findViewById(R.id.list_item_description);
        Button textViewItemButton = (Button)
                convertView.findViewById(R.id.list_item_btn);
        Button textViewItemSpace = (Button)
                convertView.findViewById(R.id.list_item_space);

        ToggleButton star=(ToggleButton)
                convertView.findViewById(R.id.list_item_toggle);

        if(star.getText().equals("Star")){
        star.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        new handleStar(currentItem.getItemName(), currentItem.getItemUsername());
                                    }
        } );
        }

        if(star.getText().equals("Unstar")){
            star.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    new handleUnstar(currentItem.getItemName(), currentItem.getItemUsername());
                }
            } );
        }

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getItemName());
        textViewItemUsername.setText(currentItem.getItemUsername());
        textViewItemDescription.setText(currentItem.getItemDescription());
        textViewItemButton
                .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent();
                                            intent.setAction(Intent.ACTION_VIEW);
                                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                            intent.setData(Uri.parse(currentItem.getItemUrl()));
                                            context.startActivity(intent);
                                        }});
        // returns the view for the current row
        return convertView;
    }


    private class handleStar extends AsyncTask<Void, Void, Void> {
        String projname;
        String username;

        private handleStar(String projname, String username) {
            this.projname = projname;
            this.username = username;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"cs\"\r\n\r\n3d72a47e431eec93507f14ba166dd55829b96ffe\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            Request request = new Request.Builder()
                    .url("https://api.github.com/user/starred/" + username + "/" + projname)
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

    private class handleUnstar extends AsyncTask<Void, Void, Void> {
        String projname;
        String username;

        private handleUnstar(String projname, String username) {
            this.projname = projname;
            this.username = username;
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"cs\"\r\n\r\n3d72a47e431eec93507f14ba166dd55829b96ffe\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            Request request = new Request.Builder()
                    .url("https://api.github.com/user/starred/" + username + "/" + projname)
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
