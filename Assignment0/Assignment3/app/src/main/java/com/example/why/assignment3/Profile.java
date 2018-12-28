package com.example.why.assignment3;

import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Why on 22/10/2017.
 */

public class Profile extends Fragment{
    View myView;
    Item_user item;
    public static String url = "https://api.github.com/users/hwu63";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("profile", "start");
        myView = inflater.inflate(R.layout.profile,container, false);


        if(getArguments() == null){
            item = new Item_user("hwu63", "", "https://api.github.com/users/hwu63", "https://github.com/hwu63",
                    "https://api.github.com/users/hwu63/followers", "https://api.github.com/users/hwu63/following{/other_user}",
                    "https://api.github.com/users/hwu63/repos");
        }else{
            item= getArguments().getParcelable("item_user");
            Log.e("profile", item.getItemUrl());
            url = item.getItemUrl();
        }



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
        protected Void doInBackground(Void...arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {

                    JSONObject c = new JSONObject(jsonStr);

                    // looping through All Contacts

                    String Description = c.getString("bio");
                    String Email = c.getString("email");

                    item.setDescription(Description.equals("null")? "Lazy guy..." : Description);
                    item.setEmail(Email.equals("null")? "didnt_write@email.com" : Email);


                } catch (final JSONException e) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Json error follower:" , e.getMessage());
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
            setContents( myView, item);
        }

    }

    public void setContents(View view, Item_user input) {

        final Item_user item = input;
        Log.e("profile",item.print());

        if(!item.getItemAvatar().equals("")){
            ImageView avt = (ImageView) view.findViewById(R.id.profile_image);
            Glide.with(WebProfile.getAppContext())
                    .load(item.getItemAvatar())
                    .override(100, 100)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avt);
        }

        TextView name = (TextView) view.findViewById(R.id.profile_name);
        name.setText(item.getItemUsername());

        Button moreinfo = (Button) view.findViewById(R.id.profile_moreinfo);
        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getItemHtml_Url()));
                startActivity(i);
            }
        });

        Button followers = (Button) view.findViewById(R.id.profile_followers);
        followers.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Bundle bundle = new Bundle();
                                             bundle.putParcelable("item_user", item);
                                             Follow follow = new Follow();
                                             follow.setArguments(bundle);

                                             getFragmentManager().beginTransaction()
                                                     .replace(R.id.content_frame, follow)
                                                     .commit();
                                         }
        }
        );

        Button following = (Button) view.findViewById(R.id.profile_following);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item_user", item);
                Following following = new Following();
                following.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, following)
                        .commit();
            }
        }
        );

        Button repos = (Button) view.findViewById(R.id.profile_repos);
        repos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item_user", item);
                Repository repo = new Repository();
                repo.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, repo)
                        .commit();
            }
        }
        );

            TextView description = (TextView) view.findViewById(R.id.profile_description);
            description.setText(item.getItemDescription());


        TextView email = (TextView) view.findViewById(R.id.profile_text_email);
        if(item.getItemUsername().equals("hwu63")){
            email.setText("  hwu63@illinois.edu");
        }
        else{
            email.setText("  "+item.getItemEmail());}


    }


}
