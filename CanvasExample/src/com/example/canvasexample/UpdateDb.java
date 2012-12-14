package com.example.canvasexample;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateDb extends Service {
 
   String tag="TestService";
   private Timer timer;
   private int game_id = 1;
   DatabaseHandler db = new DatabaseHandler(UpdateDb.this);
   
   private TimerTask updateTask = new TimerTask() {
	    @Override
	    public void run() {
	    	JSONObject json_data = new JSONObject();
			try {
				json_data.put("game_id", game_id);
				json_data.put("action", "LASTTURN");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String response = Utils.postData("get_all_turns_for_game.php", json_data.toString());
			try {
				JSONArray turns = new JSONObject(response).getJSONArray("TURNS");
				if(turns.length() > 0){
	            	for(int i=0; i<turns.length(); i++){
	            		JSONObject turn = turns.getJSONObject(i);
	            		int game_id = turn.getInt("game_id");
	            		String user = turn.getString("turn_by");
	            		String file_name = turn.getString("file_name");
	            		int pos_x = turn.getInt("position_x");
	            		int pos_y = turn.getInt("position_y");
	            		int moving = turn.getInt("moving");
	            		if(!db.checkForTurn(game_id, pos_x, pos_y)){
	            			db.addElementToDevice(game_id, user, file_name, pos_x, pos_y, moving);
	            		}else{
	            			//Log.d("msg","turn exist");
	            			
	            			
	            		}
	            	}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
   };
   
   @Override
   public void onCreate() {
       super.onCreate();
       timer = new Timer("UpdateDb");
       timer.schedule(updateTask, 0, 3000);
   }
 
   @Override
   public void onStart(Intent intent, int startId) {      
       super.onStart(intent, startId);  
   }
   @Override
   public void onDestroy() {
       super.onDestroy();
       
       timer.cancel();
       timer = null;
   }

   @Override
   public IBinder onBind(Intent intent) {
       return null;
   }
}