package com.example.why.assignment3;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Why on 22/10/2017.
 */

public class Visualization extends Fragment{
    View myView;
    private static String url = "https://api.github.com/repos/k88hudson/git-flight-rules/commits";
    public static String url2 = "https://api.github.com/repos/thedaviddias/Front-End-Checklist/comments";
    private ArrayList<Item_visual> data = new ArrayList<Item_visual>();
    List<Entry> entries= new ArrayList<Entry>();
    List<Entry> entries2= new ArrayList<Entry>();
    public HashMap<Integer,Integer> data2;
    int times[] = new int[31];


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.visualization,container, false);


        //BubbleChart chart2 = (BubbleChart) myView.findViewById(R.id.chart2);

        new GetContents().execute();
            // turn your data into Entry objects
        new GetData().execute();

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

                            JSONObject commit = c.getJSONObject("commit");
                            JSONObject author = commit.getJSONObject("author");
                            String date = author.getString("date");

                            String[] seperated = date.split("T");
                            String time = seperated[0];

                            //Log.e(TAG, time);
                            if(!data.isEmpty()){
                                Item_visual last = data.get(data.size()-1);

                                if(time.equals(last.time)) {
                                    last.num++;
                                }else{
                                    Item_visual temp= new Item_visual(time, 1);
                                    data.add(temp);
                                }
                            }else{
                                Item_visual temp= new Item_visual(time, 1);
                                data.add(temp);
                            }

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

                for (int i = 0; i<data.size(); i++) {
                    // turn your data into Entry objects
                    entries.add(new Entry(i, data.get(i).num));
                   // Log.e(TAG, ""+i+"  "+data.get(i).num);
                }


                LineDataSet dataSet = new LineDataSet(entries, "commits"); // add entries to dataset
                dataSet.setColor(R.color.green);
                dataSet.setValueTextColor(R.color.colorPrimaryDark);

                LineChart chart1 = (LineChart) myView.findViewById(R.id.chart1);
                LineData Data = new LineData(dataSet);
                chart1.setData(Data);
                chart1.setScaleEnabled(true);
                chart1.invalidate();
            }

        }


    private class GetData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url2);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray projects = new JSONArray(jsonStr);
                    // looping through All projects
                    int length = projects.length();

                    for (int i = 0; i < length; i++) {
                        JSONObject c = projects.getJSONObject(i);
                        String create = c.getString("created_at");
                        String check = create.substring(0,7);

                        if (check.equals("2017-10")){
                            int date_ = Integer.parseInt(create.substring(8,10)); ;

                            times[date_-1] = times[date_-1]+1;

                        }
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
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
            // Dismiss the progress dialog
            entries2 = new ArrayList<Entry>();

            for (int i = 0; i < 31; i++) {
                // turn your data into Entry objects
                entries2.add(new Entry(i+1, times[i]));
            }

            LineDataSet dataSet = new LineDataSet(entries2, "Pull requests"); // add entries to dataset
            dataSet.setColor(R.color.green);
            dataSet.setValueTextColor(R.color.colorPrimaryDark);

            LineChart chart1 = (LineChart) myView.findViewById(R.id.chart2);
            LineData Data = new LineData(dataSet);
            chart1.setData(Data);
            chart1.setScaleEnabled(true);
            chart1.invalidate();

        }

    }

}
