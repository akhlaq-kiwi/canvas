package com.example.canvasexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
 
public class Canvas extends Activity {
  
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       //setContentView(new Panel(this));
       Panel p = new Panel(this);
       setContentView(R.layout.main);
       LinearLayout l = (LinearLayout)findViewById(R.id.canvas);
       l.addView(p);
       
       startService(new Intent(this, UpdateDb.class));
   }
   
   @Override
   public void onDestroy()
   {
       super.onDestroy();
       stopService(new Intent(this, UpdateDb.class));
   }
   
}