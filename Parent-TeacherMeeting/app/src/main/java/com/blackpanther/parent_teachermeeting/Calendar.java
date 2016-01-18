package com.blackpanther.parent_teachermeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 14-Jan-16.
 */
public class Calendar extends AppCompatActivity{
    public static JSONArray jsonArray;
    ScrollView scrollView= new ScrollView(this);
    LinearLayout linearLayout= new LinearLayout(this);
    //Data sent from the database will be in the following JSON format
    /*
    {
    Date[
    <date>{
    brief:"<Brief notification>"
    image:"<image url>"
    notification:"<details or notification>"
    }
    <date>{
    brief:"<Brief notification>"
    image:"<image url>"
    notification:"<details or notification>"
    }
    ],
    }
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            JSONObject jsonObject = Download.calender_retrieve();
           jsonArray = jsonObject.getJSONArray("Date");
            TextView[] textViews = new TextView[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                textViews[i].setText(jsonArray.getJSONObject(i).getString("notification"));
                linearLayout.addView(textViews[i]);
                textViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(Calendar.this,Calendar_expansion.class);
                        i.putExtra("id",i);
                        startActivity(i);
                    }
                });
                scrollView.addView(linearLayout);
            }
        }catch(JSONException e){
            Log.d("Calendar","JsonException in line no. 36");
        }
        super.onCreate(savedInstanceState);
    }
}
