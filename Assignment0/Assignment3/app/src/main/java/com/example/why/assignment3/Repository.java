package com.example.why.assignment3;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Why on 22/10/2017.
 */

public class Repository extends Fragment{
    View myView;
    private static String url = "https://api.github.com/users/hwu63/repos";
    private ArrayList<Item> data = new ArrayList<Item>();
    CustomListAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.repositories,container, false);

        ListView lv =  (ListView) myView.findViewById(R.id.listview);
        // Setup the data source

        Item_user item;

        if(getArguments()!= null){
            item = getArguments().getParcelable("item_user");
            Log.e("repo of", item.getItemUsername());
            url = item.getItemRepo_Url();
        }

        ArrayList<Item> itemsArrayList = data;// generateListContent(); // calls function to get items list

        // instantiate the custom list adapter
        adapter = new CustomListAdapter(this.getActivity(), itemsArrayList);

        lv.setAdapter(adapter);

        new GetContents().execute();

        return myView;
    }

        /**
         * Async task class to get json by making HTTP call
         */
        private class GetContents extends AsyncTask<Void, Void, Void> {

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

                        JSONArray repos = new JSONArray(jsonStr);

                        // looping through All Contacts
                        for (int i = 0; i < repos.length(); i++) {
                            JSONObject c = repos.getJSONObject(i);

                            String name = c.getString("name");
                            JSONObject owner = c.getJSONObject("owner");
                            String username = owner.getString("login");
                            String url = c.getString("html_url");
                            String description = c.getString("description");

                            if (description.equals("null"))
                                description = "No description, website, or topics provided";


                            // tmp item
                            Item temp = new Item(name, username,  description, url);
                            Log.e("output", name+"  "+username+"  "+url+"  "+description);


                            // adding item to list
                            data.add(temp);
                        }
                    } catch (final JSONException e) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WebProfile.getAppContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                }
                else {
                    Log.e(TAG, "Couldn't get json from server.");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WebProfile.getAppContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
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

                //for(int i=0; i<data.size(); i++)
                //  Log.e("Data", data.get(i).getItemDescription() +"  "+data.get(i).getItemName()+"   "+data.get(i).getItemUrl());

                adapter.notifyDataSetChanged();
            }

        }

}
