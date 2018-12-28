package com.example.why.assignment3;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.view.View.OnClickListener;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


import static android.content.ContentValues.TAG;

public class Login extends Fragment{
    String us, pw;
    String CLIENT_ID="0e031a0fe6fd19294a79";
    String CLIENT_SECRET = "e2ca80a5e2c8554e1c27cd625af5777f495dbae7";
    private static final String NETWORK_NAME = "GitHub";
    private static final String URL = "https://github.com/login/oauth/authorize?";

    public static String url = "";
    public ArrayList<String> scopeList = new ArrayList<>();
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.login,container, false);

            super.onCreate(savedInstanceState);

            TextView username = (TextView)
                myView.findViewById(R.id.username);
            us = username.getText().toString();

            TextView password= (TextView)
                myView.findViewById(R.id.password);
            pw = password.getText().toString();

        scopeList.add("public_repo");
        scopeList.add("user:follow");
        Button login= (Button)
                myView.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.e("login", "success");

            }
        });


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
            Log.e("login", "success");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}






