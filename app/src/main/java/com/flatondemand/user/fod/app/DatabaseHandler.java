package com.flatondemand.user.fod.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "fodApp";
    private static final String TABLE_LOCATION = "location";

    //column name
    private  static final String LOCATION_ID="id";
    private  static final String LOCATION_VALUE="value";


    public DatabaseHandler(Context context) {
        super(context,DB_NAME , null , DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database","Databse created");
       // String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
          //      + LOCATION_ID + " INT(5) PRIMARY KEY AUTOINCREMENT," + LOCATION_VALUE + " VARCHAR(20)" + ")";
        try {
            db.execSQL("create table "+TABLE_LOCATION +"(ID INTEGER  PRIMARY KEY AUTOINCREMENT,LOCATION TEXT)");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Exception" , e.getLocalizedMessage().toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);

        // Create tables again
        onCreate(db);
    }
}
