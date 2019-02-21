package com.flatondemand.user.fod.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "fod";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_LOCATION = "location";
    private static final String TABLE_PROPERTY="property";
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    //LOCATION TABLE Columns
    private static final String LOCTION_ID = "id";
    private static final String LOCATION_NAME = "location_val";

    //property table Columns
    private static   final String PropertyId="property_id";
    private static   final String PropertyName="property_name";
    private static   final String PropertyCoverImage="property_cover_image";
    private static   final String PropertyPrice="property_price";
    private static   final String PropertyUid="property_uid";
    private static   final String PropertyAdd="property_address";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                    + KEY_CREATED_AT + " TEXT" + ")";
            db.execSQL(CREATE_LOGIN_TABLE);
            String CREATE_LOCATION_TABLE =  "CREATE TABLE " + TABLE_LOCATION + "("
                    + LOCTION_ID + " INTEGER PRIMARY KEY," + LOCATION_NAME + ")";
            db.execSQL(CREATE_LOCATION_TABLE);
            String create_property_table="create table "+TABLE_PROPERTY +"("+PropertyId +" INTEGER PRIMARY KEY,"+ PropertyName +" TEXT, "+
                    PropertyCoverImage+" TEXT, "+PropertyPrice +" VARCHAR(256), "+PropertyUid+ " VARCHAR(256), "+PropertyAdd+" TEXT  )";
            db.execSQL(create_property_table);
        }catch (Exception e){
            Log.d(TAG , e.getLocalizedMessage());
        }
        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_PROPERTY);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    /*
    * storing property details
    * */
    public void addProperty(String propertyName , String propertyPrice , String propertyUid ,
                            String propertyCoverImage , String propertyAdd){

        SQLiteDatabase db;
        db=this.getReadableDatabase();
        String selectQuery="select * from "+TABLE_PROPERTY;
        long id=0;
        Cursor cursor=db.rawQuery(selectQuery , null);
        cursor.moveToFirst();
        if(cursor.getCount() >0){
            //update table else
            db=this.getWritableDatabase();
           try {
               db.execSQL("update "+TABLE_PROPERTY+" set "+PropertyName+"='"+propertyName+"' , "+PropertyAdd+"= '"+propertyAdd+"', "
                       +PropertyUid+"= '"+propertyUid+"', "+PropertyPrice+"= '"+propertyPrice+"' ,"+ PropertyCoverImage+"='"+propertyCoverImage+"' where "
                       +PropertyId+"=1");
           }catch (Exception e){
               Log.d(TAG, e.getLocalizedMessage());
           }
        }else{
            //insert
            db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(PropertyName , propertyName);
            contentValues.put(PropertyUid , propertyUid);
            contentValues.put(PropertyCoverImage , propertyCoverImage);
            contentValues.put(PropertyAdd , propertyAdd);
            contentValues.put(PropertyPrice,propertyPrice);
            try {
            id=db.insert(TABLE_PROPERTY ,null, contentValues);

            }catch (Exception e){
                Log.d(TAG , ""+e.getLocalizedMessage());
                Log.d(TAG, "New Property  inserted into sqlite: " + id);
            }
        }

    }
    /*
      * Storing user details in database
     * */
    public void addLocation(String location) {
        SQLiteDatabase db;
        String loc="'"+location+"'";
        String selectQuery="SELECT  * FROM "+TABLE_LOCATION;
        long id = 0;
         db= this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery , null);
        //move to first row
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            //map.put("location",cursor.getString(1));
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(LOCATION_NAME, location); // Name
           try {
               db.execSQL("update "+TABLE_LOCATION +" set "+LOCATION_NAME+"="+loc+" where "+LOCTION_ID+" =1");
               Log.d(TAG, "New location updated into sqlite: " + id);
           }catch (Exception e){
               Log.d(TAG , ""+e.getLocalizedMessage());
           }
        }else{
            // Inserting Row
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(LOCATION_NAME, location); // Name
             id = db.insert(TABLE_LOCATION, null, values);
            Log.d(TAG, "New location inserted into sqlite: " + id);
        }
        cursor.close();
        db.close();



    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("phone", cursor.getString(3));
            //user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    public HashMap<String , String>getLocation(){
        HashMap<String , String>map = new HashMap<String, String>();
        String selectQuery="SELECT  * FROM "+TABLE_LOCATION;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery , null);
        //move to first row
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            map.put("location",cursor.getString(1));
        }
        cursor.close();
        db.close();
        return map;
    }
public  HashMap<String,String>getProperty(){
    HashMap<String , String>map = new HashMap<String, String>();
    String query="select * from "+TABLE_PROPERTY;
    SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(query,null);
    cursor.moveToFirst();
    if (cursor.getCount()>0)
    {
        map.put("propertyName",cursor.getString(1));
        map.put("propertyCoverImage",cursor.getString(2));
        map.put("propertyPrice",cursor.getString(3));
        map.put("propertyUid",cursor.getString(4));
        map.put("propertyAdd",cursor.getString(5));
    }

    return  map;
}
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOCATION, null, null);
        db.close();

        Log.d(TAG, "Deleted all location info from sqlite");
    }
    public void deleteProperty() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PROPERTY, null, null);
        db.close();

        Log.d(TAG, "Deleted all property info from sqlite");
    }

}