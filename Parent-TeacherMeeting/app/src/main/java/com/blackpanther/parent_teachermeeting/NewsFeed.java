package com.blackpanther.parent_teachermeeting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*
 * Created by User on 14-Jan-16.
 */
public class NewsFeed extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static JSONObject jsonObject;
    public static JSONArray jsonArray;
    ProgressDialog dialog;
    ScrollView scrollView = new ScrollView(this);
    LinearLayout linearLayout = new LinearLayout(this);
    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    LinearLayout.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            dialog = ProgressDialog.show(this, "Loading", "The NEWS is on its way to reach you! Please wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            //The news format will be stored in the database according to the following convention to enable easier access
            //and splitting of data
    /*
    {
     Date[
     <date>{
     image:"<image url>"
     brief:"<a brief description>"
     data:"<complete data>"
     }
     <date>{
     image:"<image url>"
     brief:"<a brief description>"
     data:"<complete data>"
     }],
    }
    */
            try {
                jsonObject = Download.newsfeed();
                jsonArray = jsonObject.getJSONArray("Date");
                TextView[] textViews = new TextView[jsonArray.length()];
                for (int i = jsonArray.length(); i >= 0; i--) {
                    textViews[i].setHeight(150);
                    textViews[i].setPadding(0, 15, 0, 15);
                    linearLayout.addView(textViews[i]);
                    //Assigning the data from the JSONObject to the Data array
                    String text = "" + jsonArray.getJSONObject(i) + ":-\n" + (jsonArray.getJSONObject(i)).getString("brief");
                    textViews[i].setText(text);
                    textViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(NewsFeed.this, NEWS_expansion.class);
                            i.putExtra("id", i);
                            startActivity(i);
                        }
                    });
                }
            } catch (JSONException e) {
                Log.d("NewsFeed ", "JSONObject to Array parsing Exception line no.76");
            }
        } else
            Toast.makeText(this, "Network not Connected\n Check network connection and try again", Toast.LENGTH_LONG).show();
        scrollView.addView(linearLayout);
        scrollView.setSmoothScrollingEnabled(true);
        setContentView(scrollView);
        dialog.cancel();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NewsFeed.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            dialog = ProgressDialog.show(NewsFeed.this, "Loading", "The NEWS is on its way to reach you! Please wait");
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            try {
                                jsonObject = Download.newsfeed();
                                jsonArray = jsonObject.getJSONArray("Date");
                                TextView[] textViews = new TextView[jsonArray.length()];
                                for (int i = jsonArray.length(); i >= 0; i--) {
                                    textViews[i].setHeight(150);
                                    textViews[i].setPadding(0, 15, 0, 15);
                                    linearLayout.addView(textViews[i]);
                                    //Assigning the data from the JSONObject to the Data array
                                    String text = "" + jsonArray.getJSONObject(i) + ":-\n" + (jsonArray.getJSONObject(i)).getString("brief");
                                    textViews[i].setText(text);
                                    textViews[i].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(NewsFeed.this, NEWS_expansion.class);
                                            i.putExtra("id", i);
                                            startActivity(i);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                Log.d("NewsFeed ", "JSONObject to Array parsing Exception line no.76");
                            }
                        } else
                            Toast.makeText(NewsFeed.this, "Network not Connected\n Check network connection and try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }
}
