package com.blackpanther.parent_teachermeeting;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//The main home page which will be displayed to the user in case of successful login
public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Database and table names declaration
    public static final String DATABASENAME="student.db";
    //A separate table to store the details of the student in the event of the app being accessed by a student
    public static final String STUDENT_DATA="student_data";
    //A separate table to store the details of the school such as logo, principal name, school photo and the achievements
    //public static final String SCHOOL_DATA="school_data";
    //A separate table to store the details of the teachers that are booked if app is accessed by a student
    //or to store the details of the teacher if the app is accessed by a teacher
    //Additional details are also being downloaded and stored in order to facilitate the storage of
    public static final String TEACHER_DATA="teacher_data";
    //A separate table to store the timings for which a teacher is booked for a total of  6 days for teacher access only
    //If the user is a student then this same table will store the unique id of the teacher whom he has booked during the time-stamp
   // public static final String TIME_STAMP="time_stamp";
    //Database table creation strings
    /*
    The following table contains the following fields:
    id- integer-primary key- not null=>unique id of the student
    name -text-not null=>name of the student
    login-integer-not null=>used to decide whether an account is synced with this app or not, 0-no 1-yes
    bookings-text=>contains the unique id of the teachers with which the parent is booked
     */
    private static final String  STUDENT_DATA_CREATE="create table if not exists"+
            STUDENT_DATA+"(id integer primary key ,name text not null ,login integer not null)";
    /*
    The following table contains the following fields:
    school_name-text-primary key-not null=>The name of the school
    principal-text-not null=>name of the principal of the school
    description-text=>The description paragraph that will be entered by the administrator
     */
    /******************************************************************************************************************************************************************/
    //The following code is commented in the thinking that this data need not be stored in the phone, it can be displayed directly from the foreign database
    /*private static final String SCHOOL_DATA_CREATE="create table if not exits"+SCHOOL_DATA+
            "(school_name text not null primary key, principal text not null, description text)";*/
    /*
    The following table contains the following fields:
    id-integer-primary key-not null=>The unique code of the teacher
    name-text-not null=>name of the teacher
    email-text-not null=>Email id of the teacher
    specialization-text-not null=>The field of specialization of the teacher
    login-integer-not null=>To indicate whether it is teacher login or student login
     */
    private static final String TEACHER_DATA_CREATE="create table if not exists"+TEACHER_DATA+
            "(id integer primary key not null, name text not null , email text not null, specialization text not null,login integer not null)";
    /*
    The following table contains the following fields:
    id-integer-primary key-not null=>indicates the day of the week
    The remaining fields indicate the time stamps, they contain only binary values, either 1-true or 0-false,to indicate if they are booked in the specified time stamp or not
     */
   // private static final String TIME_STAMP_CREATE="create table if not exists"+TIME_STAMP+
         //   "(id integer primary key auto increment,4 integer, 4.15 integer, 4.30 integer, 4.45 integer, 5 integer, 5.15 integer, 5.30 integer, 5.45 integer, 6 integer)";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/****************************************************************************************************************************/
        //Creating the database
        SQLiteDatabase db = openOrCreateDatabase(DATABASENAME, Context.MODE_PRIVATE,null);
        //Creating the tables
        db.execSQL(STUDENT_DATA_CREATE);
        db.execSQL(TEACHER_DATA_CREATE);
        //The following code segment is commented due to the thinking that these date are better to be received from the internet
        //rather than initializing them to 0 each time the app opens
        /*for(int i=0;i<6;i++) {
            ContentValues contentValues= new ContentValues();
            contentValues.put("4",0);
            contentValues.put("4.15",0);
            contentValues.put("4.30",0);
            contentValues.put("4.45",0);
            contentValues.put("5",0);
            contentValues.put("5.15",0);
            contentValues.put("5.30",0);
            contentValues.put("5.45",0);
            contentValues.put("6",0);
            db.insert(TIME_STAMP,null,contentValues);
        }*/
        /*
        This the numbers corresponding to the days stored in the teacher's local database, which is the primary key
        1-Monday
        2-Tuesday
        3-Wednesday
        4-Thursday
        5-Friday
        6-Saturday
        */
        /******************************************************************************************************************************/
        //Cursor to check the student table to check login
        Cursor c=db.rawQuery("select * from"+STUDENT_DATA+"where login=?",new String[]{"1"});
        //Cursor to check the teacher table to check login
        Cursor a=db.rawQuery("select * from"+TEACHER_DATA+"where login=?",new String[]{"1"});
        if(c==null) {
            //it is essential that the student table is first checked so that both data don't interfere
            if(a==null) {
                //if both the logins do not exist then the app prompts the user to login manually by opening the login activity
                startActivity(new Intent(this, Login.class));
            }
            else {
                try {
                    User.id = c.getString(c.getColumnIndex("id"));
                    User.name = c.getString(c.getColumnIndex("name"));
                    User.email_id = c.getString(c.getColumnIndex("email"));
                    User.specialization = c.getString(c.getColumnIndex("specialization"));
                }catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else{
                User.name=c.getString(c.getColumnIndex("name"));
                User.id=c.getString(c.getColumnIndex("id"));
            }

        if (c!= null)
            c.close();
        if(a!=null)
            a.close();

        /*********************************************************************************************************************/
        /*
        The data in the school database will be stored in the following format only
         {
         School:{
         school_name="<Name of the School">
         description="<Description to be displayed>"
         school_image="<link to the image>"
         school_logo="<link to the image>"
         principal="<link to image>"
         },
         }
         */
        String id=User.id.substring(0,4);
        String description,school_name;
        Bitmap school_image,school_logo,principal;
        try {
            JSONObject jsonObject = Download.school_retrieve(id);
            //setting school name
            school_name=jsonObject.getString("name");
            User.school_name=school_name;
            TextView tvschool_name=(TextView)findViewById(R.id.school_name);
            tvschool_name.setText(school_name);
            //getting the description of the school
            description = jsonObject.getString("description");
            User.school_description=description;
            //setting text for description
            tvdescription.setText(description);
            //processing school_image
            URL url=new URL(jsonObject.getString("school_image"));
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            InputStream inputStream=httpURLConnection.getInputStream();
            school_image=BitmapFactory.decodeStream(inputStream);
            User.school_photo=school_image;
            ivschool_image.setImageBitmap(school_image);
            inputStream.close();
            httpURLConnection.disconnect();
            //processing school_logo
            url=new URL(jsonObject.getString("school_logo"));
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            inputStream=httpURLConnection.getInputStream();
            school_logo=BitmapFactory.decodeStream(inputStream);
            User.school_logo=school_logo;
            ivschool_logo.setImageBitmap(school_logo);
            inputStream.close();
            httpURLConnection.disconnect();
            //processing principal photo
            url=new URL(jsonObject.getString("principal"));
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            inputStream=httpURLConnection.getInputStream();
            principal=BitmapFactory.decodeStream(inputStream);
            User.principal=principal;
            ivprincipal.setImageBitmap(principal);
            inputStream.close();
            httpURLConnection.disconnect();
        }catch(JSONException e){
            Log.d("Homepage","JSONException in line no 174");
        }catch(MalformedURLException e){
            Log.d("Homepage","Malformed Url exception in line no.178");
        }catch(IOException e){
            Log.d("Homepage","IOException in line no.181");
        }
        }

    private ImageView ivschool_image=(ImageView)findViewById(R.id.school_image);
    private ImageView ivschool_logo=(ImageView)findViewById(R.id.school_logo);
    private ImageView ivprincipal=(ImageView)findViewById(R.id.principal);
    private TextView tvdescription=(TextView)findViewById(R.id.description);


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.homepage:
                startActivity(new Intent(this,Homepage.class));
                break;
            case R.id.newsfeed:
                startActivity(new Intent(this, NewsFeed.class));
                break;
            case R.id.calendar:
                startActivity(new Intent(this,Calendar.class));
                break;
            case R.id.just_interact:
                if(User.id.charAt(6)=='S')
                    startActivity(new Intent(this,Just_interact_parent.class));
                else if(User.id.charAt(6)=='T')
                    startActivity(new Intent(this,Just_interact_teacher.class));
                break;
            case R.id.privacy:

                break;
            case R.id.settings:

                break;
            case R.id.logout:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
