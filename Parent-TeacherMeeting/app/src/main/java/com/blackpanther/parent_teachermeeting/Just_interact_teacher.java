package com.blackpanther.parent_teachermeeting;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.R.color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/*
 * Created by User on 18-Jan-16.
 */
public class Just_interact_teacher extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    ScrollView scrollView;
    LinearLayout linearLayout;
    LinearLayout.LayoutParams params, parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeRefreshLayout= new SwipeRefreshLayout(this);
        scrollView = new ScrollView(this);
        linearLayout = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parameter = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(params);
        linearLayout.setLayoutParams(parameter);
        scrollView.addView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
         /*
                {
                Teacher:
                {
                password="<password>"
                name="<name of the teacher>"
                email="<Email id of the teacher>"
                specialization="<Field of Specialization>"
                image="<Teacher's Image url>"
                timestamps:[{
                1:{[
                4-4.15="<either null or the unique id of the student who is booked in this time slot>"
                4.15-4.30="<>"
                4.30-4.45="<>"
                4.45-5="<>"
                5.15-5.30="<>"
                5.30-5.45="<>"
                5.45-6="<>"
                ]},
                2:{
                <similar to the above construct the data is stored for all 6 days except sunday>
                <a total of 48 records>
                },
                }],
                },
                }
                 */
        JSONObject jsonObject = Download.teacher_retrieve(User.id);
        try {
            JSONArray time = jsonObject.getJSONArray("timestamps");
            for (int i = 0; i < 6; i++) {
                char q = (char) i;
                JSONArray timestamp = time.getJSONArray(q);
                for (int j = 0; i < 8; i++) {
                    TextView textView = new TextView(Just_interact_teacher.this);
                    textView.setLayoutParams(params);
                    String text = "Booked by: " + timestamp.getString(j) + "\n at: " + timestamp.getJSONObject(j).toString();
                    textView.setText(text);
                    textView.setPadding(0, 30, 0, 30);
                    linearLayout.addView(textView);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.addView(scrollView);
        swipeRefreshLayout.setColorSchemeResources(color.holo_orange_dark, color.holo_green_dark, color.holo_red_dark, color.holo_blue_dark);
        this.setContentView(swipeRefreshLayout);
    }

    @Override
    public void onRefresh() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000);

                    Just_interact_teacher.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = Download.teacher_retrieve(User.id);
                            try {
                                JSONArray time = jsonObject.getJSONArray("timestamps");
                                for (int i = 0; i < 6; i++) {
                                    char q = (char) i;
                                    JSONArray timestamp = time.getJSONArray(q);
                                    for (int j = 0; i < 8; i++) {
                                        TextView textView = new TextView(Just_interact_teacher.this);
                                        textView.setLayoutParams(params);
                                        String text = "Booked by: " + timestamp.getString(j) + "\n at: " + timestamp.getJSONObject(j).toString();
                                        textView.setText(text);
                                        textView.setPadding(0, 30, 0, 30);
                                        linearLayout.addView(textView);
                                    }
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }
}
