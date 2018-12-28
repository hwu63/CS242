package com.example.why.assignment3;

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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Why on 05/11/2017.
 */


    public class CustomListAdapter_search extends BaseAdapter {
        private Context context; //context
        private Activity act;
        private ArrayList<Item_search> items; //data source of the list adapter
        private Boolean is_user = true;

        //public constructor
        public CustomListAdapter_search(Context context, Activity act,  ArrayList<Item_search> items, boolean is_user) {
            this.context = context;
            this.items = items;
            this.is_user = is_user;
            this.act = act;
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
                        inflate(R.layout.list_item_search, parent, false);
            }

            // get current item to be displayed
            final Item_search currentItem = (Item_search) getItem(position);

            // get the TextView for item name and item description
            Button textViewItemName = (Button)
                    convertView.findViewById(R.id.list_item_search);


            textViewItemName.setText(currentItem.name);

            textViewItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(is_user){
                        //open profile
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("item_user", currentItem.user);
                        Log.e("profile_button", currentItem.name);
                        Profile profile = new Profile();
                        profile.setArguments(bundle);

                        act.getFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, profile)
                                .commit();
                    }
                    else{
                        //repo
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(currentItem.repo_url));
                        context.startActivity(intent);
                    }
                }
            });


            return convertView;
        }


    }

