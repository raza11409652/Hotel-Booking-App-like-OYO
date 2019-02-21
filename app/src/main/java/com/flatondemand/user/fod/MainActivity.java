package com.flatondemand.user.fod;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flatondemand.user.fod.app.DatabaseHandler;
import com.flatondemand.user.fod.app.SQLiteHandler;
import com.flatondemand.user.fod.app.SessionManager;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME = 2500;
    private SessionManager session;
    private SQLiteHandler db;
   // SQLiteDatabase sqLiteDatabase;
    SQLiteHandler sqLiteHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SQLite database handler

sqLiteHandler= new SQLiteHandler(this);
        // Session manager
        session = new SessionManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is already logged in or not
                if (session.isLoggedIn()) {
                    // User is already logged in. Take him to main activity
                    Intent intent = new Intent(getApplicationContext(), Dash.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent goToHome= new Intent(getApplicationContext() ,Home.class );
                    startActivity(goToHome);
                    finish();
                }

            }
        },SPLASH_TIME);


    }
}
