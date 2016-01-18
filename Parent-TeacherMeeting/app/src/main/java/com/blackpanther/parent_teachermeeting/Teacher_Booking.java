package com.blackpanther.parent_teachermeeting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 18-Jan-16.
 */
public class Teacher_Booking extends AppCompatActivity {
    String unique_id;
    ScrollView scrollView;
    LinearLayout linearLayout;
    LinearLayout.LayoutParams params;
    LinearLayout.LayoutParams parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollView= new ScrollView(this);
        linearLayout= new LinearLayout(this);
        params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        parameter= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        scrollView.setLayoutParams(params);
        linearLayout.setLayoutParams(params);
        scrollView.addView(linearLayout);
        unique_id=getIntent().getExtras().getString("ID");
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
        ProgressDialog progressDialog=ProgressDialog.show(Teacher_Booking.this,"Loading Student Details","Please wait while the data is being retrieved");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        JSONObject jsonObject = Download.teacher_retrieve(unique_id);
        try {
            String name=jsonObject.getString("name");
            String specialization = jsonObject.getString("specialization");
            String text="Unique ID: "+unique_id+"\nName: "+name+"\nSpecialization: "+specialization;
            //Processing the First text view to display Teacher details
            TextView characters = new TextView(Teacher_Booking.this);
            URL url=new URL(jsonObject.getString("image"));
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream= httpURLConnection.getInputStream();
            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
            ImageView imageView = new ImageView(Teacher_Booking.this);
            imageView.setLayoutParams(parameter);
            LinearLayout image= new LinearLayout(Teacher_Booking.this);
            image.setLayoutParams(parameter);
            image.setGravity(Gravity.START);
            characters.setLayoutParams(params);
            characters.setText(text);
            characters.setGravity(Gravity.END);
            LinearLayout linear = new LinearLayout(Teacher_Booking.this);
            linear.setLayoutParams(params);
            linear.addView(image);
            linear.addView(characters);
            linear.setPadding(0,30,0,30);
            linear.setOrientation(LinearLayout.HORIZONTAL);
            //Adding the processed layout to the main layout
            linearLayout.addView(linear);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            JSONArray time_stamps=jsonObject.getJSONArray("timestamps");
            for(int i=0;i<6;i++){
                JSONArray times;
                times=time_stamps.getJSONArray(i);
                String[] time= new String[8];
                for(int k=0;k<8;k++){
                    time[k]=times.getString(k);

                }
                for(int j=0;j<8;j++){
                final TextView textView = new TextView(Teacher_Booking.this);
                    textView.setLayoutParams(params);
                    textView.setPadding(0,30,0,30);
                    textView.setGravity(Gravity.CENTER);
                    String Text =number_to_day(i)+" at "+number_to_time(j);
                    textView.setText(Text);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    if(time[j]==null){
                        textView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Teacher_Booking.this,"Slot Already Booked\tTry some other Slot",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        textView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Teacher_Booking.this);
                                builder.setMessage("Are you Sure you want to Book this slot?");
                                builder.setTitle("Confirmation");
                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int q = Upload.teacher_book(unique_id);
                                        if (q == 1) {
                                            Toast.makeText(Teacher_Booking.this, "Booking successful", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(Teacher_Booking.this, "Booking Unsuccessful, Check internet and Try again", Toast.LENGTH_LONG).show();
                                            textView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                                        }
                                    }
                                });
                                builder.setNegativeButton("NO",null);
                                AlertDialog dialog= builder.create();
                                dialog.show();

                            }
                        });
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("Teacher_Booking","JSONException");
        }catch(MalformedURLException e){
            Log.d("Teacher_Booking","MalformedURLException");
        }catch(IOException e){
            Log.d("Teacher_Booking","IOException");
        }

        Teacher_Booking.this.setContentView(scrollView);
    }
    public String number_to_day(int a){
        switch(a){
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return "Invalid entry";
    }
    public String number_to_time(int a){
        switch (a){
            case 1:
                return "4-4.15";
            case 2:
                return "4.15-4.30";
            case 3:
                return "4.30-4.45";
            case 4:
                return "4.45-5";
            case 5:
                return "5-5.15";
            case 6:
                return "5.15-5.30";
            case 7:
                return "5.30-5.45";
            case 8:
                return "5.45-6";
        }
        return "Invalid entry";
    }
}
