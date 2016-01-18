package com.blackpanther.parent_teachermeeting;

import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * Created by User on 14-Jan-16.
 */
public class Upload extends AsyncTask{
    String student_table_url="";
    String teacher_table_url="";
    String school_table_url="";
    public static int student_upload(User user){

        return 0;
    }
    public static int teacher_upload(User user){

        return 0;
    }
    public static int teacher_book(String id){

        return 0;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            URL url = new URL(student_table_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                httpURLConnection.setRequestMethod("POST");
                DataOutputStream dataOutputStream= new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeChars("name="+User.name+"email="+User.email_id+
                        "specialization="+User.specialization+"id="+User.id);
                dataOutputStream.flush();
                dataOutputStream.close();
            }
        }catch(MalformedURLException e) {
            Log.d("Upload Exception", "Malformed Url Exception");
        }catch(IOException e){
            Log.d("Upload Exception","IOException");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
