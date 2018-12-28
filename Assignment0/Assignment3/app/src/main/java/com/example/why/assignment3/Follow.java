package com.example.why.assignment3;

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

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static android.view.View.*;


/**
 * Created by Why on 22/10/2017.
 */
public class Follow extends Fragment {
    View myView;
    public static String url = "https://api.github.com/users/hwu63/followers";
    private ArrayList<Item_user> data_follower = new ArrayList<>();
    CustomListAdapter_follower adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.follow, container, false);

        Item_user item;

        if(getArguments()!= null){
            item = getArguments().getParcelable("item_user");
            Log.e("follow of", item.getItemUsername());
            url = item.getItemFollow_Url();
        }

        ListView lv = (ListView) myView.findViewById(R.id.listview_follow);
        // Setup the data source


        ArrayList<Item_user> itemsArrayList = data_follower;// generateListContent(); // calls function to get items list

        // instantiate the custom list adapter
        adapter = new CustomListAdapter_follower(this.getActivity(), itemsArrayList);

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

                        String username = c.getString("login");
                        String avatar = c.getString("avatar_url");
                        String user_url = c.getString("url");
                        String html_url = c.getString("html_url");
                        String follow_url = c.getString("followers_url");
                        String following_url = c.getString("following_url");
                        String repo_Url = c.getString("repos_url");


                        // tmp item
                        Item_user temp = new Item_user(username, avatar, user_url, html_url,
                                follow_url, following_url, repo_Url);
                        Log.e("follower_output", username+"  "+url);


                        // adding item to list
                        data_follower.add(temp);
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


