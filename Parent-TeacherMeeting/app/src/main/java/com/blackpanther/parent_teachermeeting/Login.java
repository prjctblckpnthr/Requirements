package com.blackpanther.parent_teachermeeting;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText id,pass;
    Button login;
    String unique_id,password;
    public static final String DATABASE_NAME="student.db";
    public static final String STUDENT_DATA="student_data";
    public static final String TEACHER_DATA="teacher_data";
    private static final String TEACHER_DATA_CREATE="create table if not exists"+TEACHER_DATA+
            "(id integer primary key not null, name text not null , email text not null, specialization text not null,login integer not null)";
    private static final String  STUDENT_DATA_CREATE="create table if not exists"+
            STUDENT_DATA+"(id integer primary key ,name text not null ,login integer not null )";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);
        //Creating the tables
        db.execSQL(STUDENT_DATA_CREATE);
        db.execSQL(TEACHER_DATA_CREATE);
        id=(EditText)findViewById(R.id.id);
        pass=(EditText)findViewById(R.id.pass);
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unique_id=id.getText().toString();
                password=pass.getText().toString();
                //Char array to get the unique id and separate it and extract the Teacher and Student differentiation Character
                //This character is specifically placed at the 7th position which is id[6]
                char[] a=unique_id.toCharArray();
                //designation is the char used to store the differentiation character
                char designation=a[6];
                //The Data sent from the database will be in the following format
                if(designation=='S'){
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
                    try {
                        ProgressDialog progressDialog=ProgressDialog.show(Login.this,"Loading Student Details","Please wait while the data is being retrieved");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        JSONObject jsonObject = Download.student_retrieve(unique_id);
                        String data_pass = jsonObject.getString("password");
                        User.name=jsonObject.getString("name");
                        User.id=unique_id;
                        JSONArray jsonArray=jsonObject.getJSONArray("timestamps");
                       //need to transfer this data to the internal database
                        for(int i=1;i<7;i++){
                            JSONObject temp=jsonArray.getJSONObject(i);
                            User.time_stamps[i][0]=temp.getString("4-4.15");
                            User.time_stamps[i][1]=temp.getString("4.15-4.30");
                            User.time_stamps[i][2]=temp.getString("4.30-4.45");
                            User.time_stamps[i][3]=temp.getString("4.45-5");
                            User.time_stamps[i][4]=temp.getString("5-5.15");
                            User.time_stamps[i][5]=temp.getString("5.15-5.30");
                            User.time_stamps[i][6]=temp.getString("5.30-5.45");
                            User.time_stamps[i][7]=temp.getString("5.45-6");
                        }
                        if(password.equals(data_pass))
                        {
                            ContentValues contentValues= new ContentValues();
                            contentValues.put("name",User.name);
                            contentValues.put("id",User.id);
                            contentValues.put("login",1);
                            db.delete(STUDENT_DATA, "where login=?", new String[]{"1"});
                            db.execSQL(STUDENT_DATA_CREATE);
                            db.insert(STUDENT_DATA,null,contentValues);

                            startActivity(new Intent(Login.this, Homepage.class));
                        }
                        else {
                            Toast.makeText(Login.this,"Unique ID and Password don't match \nTry again",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.cancel();
                    }catch(JSONException e){
                        Log.d("Login class","JSON array parsing exception at line no.81");
                    }

                }
            else if (designation=='T'){
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
                    try {
                        ProgressDialog progressDialog=ProgressDialog.show(Login.this,"Loading Teacher Details","Please wait while the data is being retrieved");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        JSONObject jsonObject = Download.teacher_retrieve(unique_id);
                        String data_pass = jsonObject.getString("password");
                        User.name=jsonObject.getString("name");
                        String data_email=jsonObject.getString("email_id");
                        String data_specialization=jsonObject.getString("specialization");
                        JSONArray jsonArray=jsonObject.getJSONArray("timestamps");
                        String[][] time=new String[6][8];
                        //This part is commented because the timestamps need to be updated often, and should not be stored
                        //in a database
                      /*  for(int i=1;i<7;i++){
                            JSONObject temp=jsonArray.getJSONObject(i);
                            time[i][0]=temp.getString("4-4.15");
                            time[i][1]=temp.getString("4.15-4.30");
                            time[i][2]=temp.getString("4.30-4.45");
                            time[i][3]=temp.getString("4.45-5");
                            time[i][4]=temp.getString("5-5.15");
                            time[i][5]=temp.getString("5.15-5.30");
                            time[i][6]=temp.getString("5.30-5.45");
                            time[i][7]=temp.getString("5.45-6");
                        }*/
                        //need to transfer this data to the internal database
                        if(password.equals(data_pass))
                        {
                            ContentValues contentValues= new ContentValues();
                            contentValues.put("name",User.name);
                            contentValues.put("id",User.id);
                            contentValues.put("login",1);
                            contentValues.put("email",User.email_id);
                            contentValues.put("specialization",User.specialization);
                            db.delete(TEACHER_DATA, "where login=?", new String[]{"1"});
                            db.execSQL(TEACHER_DATA_CREATE);
                            db.insert(TEACHER_DATA,null,contentValues);
                            startActivity(new Intent(Login.this,Homepage.class));
                        }
                        else {
                            Toast.makeText(Login.this,"Unique ID and Password don't match \nTry again",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.cancel();
                    }catch(JSONException e){
                        Log.d("Login class","JSON array parsing exception at line no.81");
                    }
                }

            }
        });
    }

}
