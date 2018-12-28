package com.example.why.assignment3;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Why on 05/11/2017.
 */

public class SearchResultsActivity extends Activity {

    private ArrayList<Item_search> data_search = new ArrayList<>();
    CustomListAdapter_search adapter;
    public static String user_url = "https://api.github.com/search/users?q=";//tom&fire=followers
    public static String repo_url = "https://api.github.com/search/repositories?q=";
    public static String access_token = "&access_token=3d72a47e431eec93507f14ba166dd55829b96ffe";
    public static Boolean sort = false;
    Boolean is_user = true;
    Activity activity = this;
    String query;
    View myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());

        setContentView(R.layout.search_result);

        final ListView lv = (ListView) findViewById(R.id.list_view_search);
        // Setup the data source

        final ArrayList<Item_search> itemsArrayList = data_search;

        final ToggleButton user_repo = (ToggleButton)
                findViewById(R.id.user_repo);

        user_repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                is_user = !user_repo.isChecked();
                Intent in = new Intent();
                in.putExtra("query", query);
                in.putExtra("isUser", is_user);
                in.putExtra("sort", sort);
                handleIntent(in);
                adapter.notifyDataSetChanged();

            }
        }) ;

        ImageView tosort = (ImageView)
                findViewById(R.id.sort);
        tosort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = !sort;
                Intent in = new Intent();
                in.putExtra("query", query);
                in.putExtra("isUser", is_user);
                in.putExtra("sort", sort);
                handleIntent(in);
                adapter.notifyDataSetChanged();
            }

        });



        // instantiate the custom list adapter
        adapter = new CustomListAdapter_search(this.getApplicationContext(), this, itemsArrayList, is_user);

        lv.setAdapter(adapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);}
        else{
            query = intent.getStringExtra("query");
        }

            is_user = intent.getBooleanExtra("isUser", true);
            sort = intent.getBooleanExtra("sort", false);
            //use the query to search your data somehow

            //sets the text for item name and item description from the current item object

            Log.e("isUser 70", is_user.toString());

            if(is_user){
                new Search_User(query, sort).execute();
            }else{
                new Search_Repo(query, sort).execute();
            }

        }

    /**
     * Async task class to get json by making HTTP call
     */
    public class Search_User extends AsyncTask<Void, Void, Void> {


        String q;
        Boolean sort;

        private Search_User(String query, Boolean sort) {
            this.q = query;
            this.sort = sort;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String tosort = sort? "&sort=followers" : "" ;
            String url = user_url+ q + tosort + access_token;

            data_search.clear();
            Log.e("user url",url);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject page = new JSONObject(jsonStr);

                    JSONArray items = page.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < Math.min(items.length(),20); i++) {
                        JSONObject c = items.getJSONObject(i);

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

                        Item_search tp = new Item_search();
                        tp.user = temp;
                        // adding item to list
                        tp.name = username;
                        Log.e("user", username);
                        data_search.add(tp);
                    }
                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
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
                runOnUiThread(new Runnable() {
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
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * Async task class to get json by making HTTP call
     */
    public class Search_Repo extends AsyncTask<Void, Void, Void> {


        String q;
        Boolean sort;

        private Search_Repo(String query, Boolean sort) {
            this.q = query;
            this.sort = sort;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String tosort = sort? "&sort=stars" : "" ;
            String url = repo_url+ q + tosort + access_token;

            data_search.clear();
            Log.e("repo url",url);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject page = new JSONObject(jsonStr);

                    JSONArray items = page.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < Math.min(items.length(),20); i++) {
                        JSONObject c = items.getJSONObject(i);

                        String name = c.getString("name");

                        String repo_Url = c.getString("html_url");


                        // tmp item
                        Item_search temp = new Item_search();

                        temp.name = name;
                        temp.repo_url = repo_Url;
                        Log.e("Repo", name);

                        // adding item to list
                        data_search.add(temp);
                    }
                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
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
                runOnUiThread(new Runnable() {
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
            adapter.notifyDataSetChanged();
        }
    }
    }



    /* Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("sort", true);
                b.putBoolean("isUser", is_user);
                intent.putExtras(b); //Put your id to your next Intent
                finish();
                startActivity(intent);




                Intent b = new Intent(getApplicationContext(), SearchResultsActivity.class);
                Bundle bundle = new Bundle();
                if(user_repo.isChecked()){
                    //repo
                    is_user = false;
                    bundle.putBoolean("isUser", false);
                }else{
                    //user
                    is_user = true;
                    bundle.putBoolean("isUser", true);
                }
                b.putExtras(bundle);
                startActivity(b);
                finish();*/