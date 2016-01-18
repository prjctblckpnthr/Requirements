package com.blackpanther.parent_teachermeeting;

import android.app.ProgressDialog;
import android.content.Intent;
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

/**
 * Created by User on 14-Jan-16.
 */
public class Just_interact_parent extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    ScrollView scrollView;
    LinearLayout linearLayout;
    String[][] time= new String[6][8];
    /*
               {
               Student:
               {
               Details:{
               password="<password>"
               name="<name of the student>"
               },
               timestamps:[{
               1:{
               4-4.15="<either null or the unique id of the teacher who is booked in this time slot>"
               4.15-4.30="<>"
               4.30-4.45="<>"
               4.45-5="<>"
               5.15-5.30="<>"
               5.30-5.45="<>"
               5.45-6="<>"
               },
               2:{
               <similar to the above construct the data is stored for all 6 days except sunday>
               <a total of 48 records>
               },
               }],
               },
               }
                */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollView= new ScrollView(this);
        linearLayout= new LinearLayout(this);
        ProgressDialog progressDialog= null;
        try {
            progressDialog=ProgressDialog.show(Just_interact_parent.this,"Loading Student Details","Please wait while the data is being retrieved");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            JSONObject jsonObject = Download.student_retrieve(User.id);
            JSONArray jsonArray = jsonObject.getJSONArray("timestamps");
            for(int i=1;i<7;i++){
                char a=(char)i;
                JSONArray time_stamp=jsonArray.getJSONArray(a);
                for(int j=0;j<8;j++){
                    User.time_stamps[i-1][j]=time_stamp.getString(j);
                }
            }
        }catch(JSONException e) {
            Log.d("Just_interact_parent", "JSONException in line no.50");
            Toast.makeText(Just_interact_parent.this, "Failed to Load JSON exception\n Please Report to Coder", Toast.LENGTH_LONG).show();
        }
        progressDialog.cancel();
        for(int i=0;i<6;i++){
            for(int j=0;j<8;j++){
                TextView textView= new TextView(Just_interact_parent.this);
                if(User.time_stamps[i][j]==null){
                    textView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    textView.setText("No Teacher Booked\n Click to book");
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent a = new Intent(Just_interact_parent.this, Teacher_display.class);
                            startActivity(a);
                        }
                    });
                }
                else{
                    final int g=i;
                    final int h=j;
                    textView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent a = new Intent(Just_interact_parent.this, Teacher_display.class);
                            a.putExtra("ID", User.time_stamps[g][h]);
                            a.putExtra("day",g);
                            a.putExtra("time",h);
                            startActivity(a);
                        }
                    });
                    textView.setText(User.time_stamps[i][j]);
                }
                linearLayout.addView(textView);
            }
        }
    }

    @Override
    public void onRefresh() {

    }
}
