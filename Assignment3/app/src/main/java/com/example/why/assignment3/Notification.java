package com.example.why.assignment3;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Why on 04/11/2017.
 */

public class Notification extends Fragment {
    View myView;
    static String Access_Token = "3d72a47e431eec93507f14ba166dd55829b96ffe";
    public static String url = "https://api.github.com/notifications?all=true&access_token=" + Access_Token;
    public static String toget_url;
    private ArrayList<Item_note> data_note= new ArrayList<>();
    CustomListAdapter_note adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.notification, container, false);

        Item_note item;

        ListView lv = (ListView) myView.findViewById(R.id.listview_note);
        // Setup the data source


        ArrayList<Item_note> itemsArrayList = data_note;// generateListContent(); // calls function to get items list

        // instantiate the custom list adapter
        adapter = new CustomListAdapter_note(this.getActivity(), itemsArrayList);

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

                    JSONArray array = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);

                        String id = c.getString("id");
                        String time = c.getString("updated_at");
                        String reason = c.getString("reason");
                        String unread = c.getBoolean("unread") ? "T":"F";


                        JSONObject subject  = c.getJSONObject("subject");

                        String title = subject.getString("title");
                        String url= subject.getString("url");


                        // tmp item
                        Item_note temp = new Item_note(title, time, url, reason, unread,id);


                        // adding item to list
                        data_note.add(temp);
                    }
                } catch (final JSONException e) {

                    getActivity().runOnUiThread(new Runnable() {
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
                getActivity().runOnUiThread(new Runnable() {
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

            //for(int i=0; i<data.size(); i++)
            //  Log.e("Data", data.get(i).getItemDescription() +"  "+data.get(i).getItemName()+"   "+data.get(i).getItemUrl());

            adapter.notifyDataSetChanged();
        }

    }



}



