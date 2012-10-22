package com.example.canvasexample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "game";
 
    // Contacts table name
    private static final String TABLE_MOVES = "moves";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_GAME_ID = "game_id";
    private static final String KEY_PLAYER = "user";
    private static final String KEY_FILE = "file_name";
    private static final String KEY_POSX = "pos_x";
    private static final String KEY_POSY = "pos_y";
    Context ctx;
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }
    
 // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVES_TABLE = "CREATE TABLE " + TABLE_MOVES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_GAME_ID + " INTEGER,"
        		+ KEY_PLAYER + " TEXT,"
                + KEY_FILE + " TEXT,"
                + KEY_POSX + " INTEGER,"
                + KEY_POSY + " INTEGER)";
        db.execSQL(CREATE_MOVES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVES);
 
        // Create tables again
        onCreate(db);
    }
    
 // Adding new element
    void addElement(Element element) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_GAME_ID, element.getGameId());
        values.put(KEY_PLAYER, element.getPlayer());
        values.put(KEY_FILE, element.getFilename());
        values.put(KEY_POSX, element.getPosX());
        values.put(KEY_POSY, element.getPosY());
        // Inserting Row
        db.insert(TABLE_MOVES, null, values);
        //db.close(); // Closing database connection
    }
    
    public ArrayList<Element> getAllElements() {
    	ArrayList<Element> elementList = new ArrayList<Element>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MOVES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Element element = new Element(ctx.getResources(), Integer.parseInt(cursor.getString(4)),  Integer.parseInt(cursor.getString(5)), false);
                // Adding contact to list
                elementList.add(element);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return elementList;
    }
    
    public ArrayList<String> getMovePosition(int game_id, int x) {
    	ArrayList<String> res = new ArrayList<String>();
    	String selectQuery = "SELECT pos_y, file_name FROM " + TABLE_MOVES + " where game_id="+game_id+" and pos_x="+x+" order by id desc limit 0,1";
    	 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if(cursor.getCount()!=0){
	        if (cursor.moveToFirst()) {
	            do {
	            	res.add(cursor.getString(0));
	            } while (cursor.moveToNext());
	        }
        }else{
        	res.add("10");
        }
        return res;
    }
}
