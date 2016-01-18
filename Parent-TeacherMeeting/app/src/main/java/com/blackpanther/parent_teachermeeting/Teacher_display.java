package com.blackpanther.parent_teachermeeting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Teacher_display extends AppCompatActivity {
    ScrollView scrollView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_display);
        if(getIntent()==null){
            //This method contains the details of all teachers in a JSON array, each identified by the unique id
           /*{
           Teacher[{
            Unique id:
            {
                password="<password>"
                name="<name of the Teacher>"
                email="<Email id of the teacher>"
                specialization="<Field of Specialization>"
                image="<Teacher's image url>"
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
                2:{[
                    <similar to the above construct the data is stored for all 6 days except sunday>
                    <a total of 48 records>
                ]},
            }],
            ]},
            },*/

            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout display = new LinearLayout(Teacher_display.this);
            JSONObject jsonObject=Download.teacher_table_retrieve();
            try {
                JSONArray Teacher=jsonObject.getJSONArray("Teacher");
                int a=Teacher.length();
                for(int i=0;i<a;i++){
                    JSONObject Unique_id=Teacher.getJSONObject(i);
                    final String unique_id=Unique_id.toString();
                    String name=Unique_id.getString("name");
                    String specialization=Unique_id.getString("specialization");
                    //transforming the url to image
                    URL url=new URL(jsonObject.getString("image"));
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    //Assigning the image to the image view
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(parameter);
                    //creating a linear layout to left align the image view
                    LinearLayout image = new LinearLayout(Teacher_display.this);
                    image.setLayoutParams(parameter);
                    image.addView(imageView);
                    image.setGravity(Gravity.START);
                    //creating a text view to display the data
                    TextView textView= new TextView(Teacher_display.this);
                    display.setLayoutParams(parameter);
                    display.addView(image);
                    textView.setLayoutParams(params);
                    textView.setPadding(0, 30, 0, 30);
                    textView.setGravity(Gravity.END);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Teacher_display.this, Teacher_Booking.class);
                            i.putExtra("ID", unique_id);
                            startActivity(i);
                        }
                    });
                    String text="Unique ID: "+unique_id+"Name: "+name+"\nSpecialization: "+specialization;
                    textView.setText(text);
                    display.addView(textView);
                    scrollView.addView(display);
                }
            } catch (JSONException e) {
                Log.d("Teacher_display","JSONException in line no. 61");
            }catch(MalformedURLException e){
                Log.d("Teacher_display","Malformed URl exception in line no.100");
            }catch(IOException e){
                Log.d("Teacher_display","IOException in line no.101");
            }
        }
        else
        { /*{
            Teacher:
            {
                password="<password>"
                name="<name of the Teacher>"
                email="<Email id of the teacher>"
                specialization="<Field of Specialization>"
                image="<Teacher's image url>"
                timestamps:[{
                1:{
                    4-4.15="<either null or the unique id of the student who is booked in this time slot>"
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
            TextView textView=(TextView)findViewById(R.id.details);
            ImageView imageView=(ImageView)findViewById(R.id.imageView);
            String id=getIntent().getExtras().getString("ID");
            JSONObject jsonObject=Download.teacher_retrieve(id);
            try {
                String name=jsonObject.getString("name");
                String specialization=jsonObject.getString("specialization");
                URL url=new URL(jsonObject.getString("image"));
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                int a=getIntent().getExtras().getInt("day");
                int b=getIntent().getExtras().getInt("time");
                String detail="Name: "+name+"\nSpecialization: "+specialization+"\n Time Allotted: "+number_to_day(a)+" "+number_to_time(b);
                textView.setText(detail);
                linearLayout.addView(textView);
            } catch (JSONException e) {
                Log.d("Teacher_display","JSONException in line no. 100");
            }catch(MalformedURLException e){
                Log.d("Teacher_display","Malformed URl exception in line no.100");
            }catch(IOException e){
                Log.d("Teacher_display","IOException in line no.101");
            }
            scrollView.addView(linearLayout);
        }
        Teacher_display.this.setContentView(scrollView);
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
