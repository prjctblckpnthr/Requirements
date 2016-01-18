package com.blackpanther.parent_teachermeeting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Calendar_expansion extends AppCompatActivity {
    TextView textView=(TextView)findViewById(R.id.notification);
    ImageView imageView=(ImageView)findViewById(R.id.notification_image);
    TextView textview1=(TextView)findViewById(R.id.date);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_expansion);

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
        try {
            int i=getIntent().getExtras().getInt("id");
            String data=Calendar.jsonArray.getJSONObject(i).getString("notification");
            String date=Calendar.jsonArray.getJSONObject(i).toString();
             textview1.setText(date);
             textView.setText(data);
            URL url= new URL(Calendar.jsonArray.getJSONObject(i).getString("image"));
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.d("Calendar ","Null Pointer Exception in line no. 29");
        }catch(JSONException e){
            Log.d("Calendar_expansion","JSONException in line no.52");
        }catch(MalformedURLException e){
            Log.d("Calendar_expansion","Malformed Url Exception in line no.60");
        }catch(IOException e){
            Log.d("Calendar_expansion","IOException in line no.66");
        }
    }

}
