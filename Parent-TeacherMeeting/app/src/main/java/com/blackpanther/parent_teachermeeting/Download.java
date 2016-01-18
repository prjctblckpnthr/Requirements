package com.blackpanther.parent_teachermeeting;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/*
 * Created by User on 14-Jan-16.
 */
public class Download extends AsyncTask {
    private static String Student_url="";
    private static String Teacher_url="";
    private static String School_url="";
    private static String Calendar_url="";
    private static String Newsfeed_url="";
    private static String Teacher_table_url="";
    private static JSONObject jsonObject=null;

    public static JSONObject student_retrieve(String id){
            Object[] param= new Object[2];
            Download download= new Download();
            param[0]=Student_url;
            jsonObject=(JSONObject)download.doInBackground(param);
        return jsonObject;
    }
    //Method to retrieve teacher data
    public static JSONObject teacher_retrieve(String id){
            Object[] param= new Object[2];
            Download download= new Download();
            param[0]=Teacher_url;
            jsonObject=(JSONObject)download.doInBackground(param);
        return jsonObject;
    }
    //Method to retrieve all data in teacher table
    public static JSONObject teacher_table_retrieve(){
        Object[] param = new Object[2];
        Download download = new Download();
        param[0]=Teacher_table_url;
        jsonObject=(JSONObject)download.doInBackground(param);
        return jsonObject;
    }
    //Method to retrieve school details
    //This method will be access in both cases, user being a student or a teacher to display the school details
    public static JSONObject school_retrieve(String id){
        Object[] param= new Object[2];
        Download download= new Download();
        param[0]=School_url;
        jsonObject=(JSONObject)download.doInBackground(param);
        return jsonObject;
    }
    //method to retrieve news feed
    public static JSONObject newsfeed(){
        Object[] param= new Object[2];
        Download download= new Download();
        param[0]=Newsfeed_url;
        jsonObject=(JSONObject)download.doInBackground(param);
        return jsonObject;
    }
    //method to retrieve calendar details
    public static JSONObject calender_retrieve(){
        Object[] param= new Object[2];
        Download download= new Download();
        param[0]=Calendar_url;
        jsonObject=(JSONObject)download.doInBackground(param);
        return jsonObject;
    }
    @Override
    protected Object doInBackground(Object[] params) {
          //Getting data from the database
                    try {
                        URL url = new URL(params[0].toString());
                        URLConnection connection = url.openConnection();
                        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            httpURLConnection.setDoInput(true);
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append("\n");
                                stringBuilder.append(line);
                            }
                            params[1] = stringBuilder.toString();
                            inputStream.close();
                            reader.close();
                        }
                    } catch (MalformedURLException e) {
                        Log.d("Download Exception", "Malformed URL exception");
                    } catch (IOException e) {
                        Log.d("Download Exception", "IOException");
                    }

     return params[1];
    }
}
