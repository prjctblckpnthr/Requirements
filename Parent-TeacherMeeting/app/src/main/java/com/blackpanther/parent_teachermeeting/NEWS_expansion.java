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

public class NEWS_expansion extends AppCompatActivity {
    TextView textView=(TextView)findViewById(R.id.description);
    ImageView imageView=(ImageView)findViewById(R.id.news_image);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_expansion);

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
                int id=getIntent().getExtras().getInt("id");
                try {
                    String data = NewsFeed.jsonArray.getJSONObject(id).getString("data");
                    URL url=new URL(NewsFeed.jsonArray.getJSONObject(id).getString("image"));
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    textView.setText(data);
                }catch(JSONException e){
                    Log.d("NEWS_expansion","JSON array parsing error line no. 46");
                }catch(MalformedURLException e){
                    Log.d("NEWS_expansion","Malformed Url Exception line no. 54");
                }catch(IOException e){
                    Log.d("NEWS_expansion","IOException lino no. 57");
                }



        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

}
