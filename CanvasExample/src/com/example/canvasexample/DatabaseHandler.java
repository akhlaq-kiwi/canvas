package com.example.canvasexample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String KEY_MOVING = "moving";
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
                + KEY_POSY + " INTEGER,"
                + KEY_MOVING + " INTEGER)";
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
    void addElementToDevice(int game_id, String user, String file_name, int pos_x, int pos_y, int moving) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_GAME_ID, game_id);
        values.put(KEY_PLAYER, user);
        values.put(KEY_FILE, file_name);
        values.put(KEY_POSX, pos_x);
        values.put(KEY_POSY, pos_y);
        values.put(KEY_MOVING, moving);
        // Inserting Row
        db.insert(TABLE_MOVES, null, values);
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
        values.put(KEY_MOVING, 1);
        // Inserting Row
        db.insert(TABLE_MOVES, null, values);
        //db.close(); // Closing database connection
        
        JSONObject json_data = new JSONObject();
		try {
			json_data.put("game_id", element.getGameId());
			json_data.put("turn_by", element.getPlayer());
			json_data.put("file_name", element.getFilename());
			json_data.put("status", 1);
			json_data.put("position_x", element.getPosX());
			json_data.put("position_y", element.getPosY());
			json_data.put("moving", 1);
			json_data.put("action", "ADD");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String response = Utils.postData("turns.php", json_data.toString());
		Log.d("response", response);
    }
    public Boolean checkForTurn(int game_id, int pos_x, int pos_y){
    	Boolean res = false;
    	String selectQuery = "SELECT * FROM " + TABLE_MOVES + " where game_id = "+game_id+" and pos_x = '"+ pos_x +"' and pos_y = '"+ pos_y +"'";
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()!=0){
        	res = true;
        }
    	return res;
    }
    
    public ArrayList<Element> getAllElements(String user) {
    	ArrayList<Element> elementList = new ArrayList<Element>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MOVES + " where moving = 0 or user = '"+ user +"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Element element = new Element(ctx.getResources(), Integer.parseInt(cursor.getString(4)),  Integer.parseInt(cursor.getString(5)), false, cursor.getString(3));
                // Adding contact to list
                //element.setFilename(cursor.getString(3));
                elementList.add(element);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return elementList;
    }
    
    public ArrayList<String> getMovePosition(int game_id, int x, String user) {
    	ArrayList<String> res = new ArrayList<String>();
    	String selectQuery = "SELECT pos_y, file_name FROM " + TABLE_MOVES + " where game_id="+game_id+" and pos_x="+x+" order by id desc limit 0,1";
    	 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if(cursor.getCount()!=0){
	        if (cursor.moveToFirst()) {
	            do {
	            	res.add(cursor.getString(0));
	            	res.add(cursor.getString(1));
	            } while (cursor.moveToNext());
	        }
        }else{
        	res.add("10");
        	res.add("green");
        }
        return res;
    }
    public Boolean checkForMyTurn(int game_id, String user_id){
    	Boolean res = true;
    	String selectQuery = "SELECT user FROM " + TABLE_MOVES + " where game_id='" + game_id + "' and moving != 2 order by id desc limit 0,1";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()!=0){
        	if (cursor.moveToFirst()) {
	            do {
	            	String user = cursor.getString(0);
	            	if(user.equals(user_id)){
	            		res = false;
	            	}
	            } while (cursor.moveToNext());
	        }
        }
        return res;    	
    }
    
    public ArrayList<String> checkMoving(int game_id, String user_id){
    	int id = 0;
    	int pos_x;
    	int pos_y;
    	ArrayList<String> res = new ArrayList<String>();
    	String selectQuery = "SELECT * FROM " + TABLE_MOVES + " where game_id="+game_id+" and user != " + user_id + " and moving=1 limit 0,1";
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()!=0){
	        if (cursor.moveToFirst()) {
	            do {
	            	res.add("true");
	            	res.add(cursor.getString(0));
	            	id = cursor.getInt(0);
	            	game_id = cursor.getInt(1);
	            	pos_x = cursor.getInt(4);
	            	pos_y = cursor.getInt(5);
	            	res.add(cursor.getString(4));
	            	res.add(cursor.getString(3));
	            	
	            	JSONObject json_data = new JSONObject();
	    			try {
	    				json_data.put("game_id", game_id);
	    				json_data.put("position_x", pos_x);
	    				json_data.put("position_y", pos_y);
	    				json_data.put("moving", 0);
	    				json_data.put("action", "UPDATE");
	    			} catch (JSONException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			String response = Utils.postData("turns.php", json_data.toString());
	    			Log.d("response", response);
	            	
	            } while (cursor.moveToNext());
	        }
	        ContentValues values = new ContentValues();
	        values.put(KEY_MOVING, 2);
	        db.update(TABLE_MOVES, values, KEY_ID + " = ?", new String[] { String.valueOf(id)});
	        //db.delete(TABLE_MOVES, KEY_ID + " = ?", new String[] { String.valueOf(id) });
	    	
	        
	        
        }else{
        	res.add("false");
        }
        //db.close();
        return res;
    }
    
    public Boolean checkForFirtsPlayer(String user, int game_id){
    	Boolean res = false;
    	String selectQuery = "SELECT user FROM " + TABLE_MOVES + " where game_id="+game_id+" order by id asc limit 0,1";
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()!=0){
        	if (cursor.moveToFirst()) {
	            do {
	            	String user_db = cursor.getString(0);
	            	if(user_db.equals(user)){
	            		res = true;
	            	}else{
	            		res = false;
	            	}
	            } while (cursor.moveToNext());
	        }
        }else{
        	res = true;
        }
        return res;
    }
    
    public Boolean checkMoved(int game_id){
    	Boolean res = false;
    	String selectQuery = "SELECT * FROM " + TABLE_MOVES + " where game_id='" + game_id + "' and moving=2 order by id desc limit 0,1";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()!=0){
        	res = true;
	    }
        return res;    	
    }
    public void updateMoved(int game_id){
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
        values.put(KEY_MOVING, 0);
        db.update(TABLE_MOVES, values, KEY_MOVING + " = ?", new String[] {"2"});
    }
}
