package com.example.canvasexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
 
public class Canvas extends Activity {
  
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
       //setContentView(new Panel(this));
       
       Display display = getWindowManager().getDefaultDisplay();
       DisplayMetrics outMetrics = new DisplayMetrics ();
       display.getMetrics(outMetrics);

       float density  = getResources().getDisplayMetrics().density;
       int sHeight = (int) ((int)outMetrics.heightPixels / density);
       int sWidth  = (int) ((int)outMetrics.widthPixels / density);
       Log.d("msg", "H: "+sHeight+" W: "+sWidth);
       int canvasHeight = (sHeight);
       int canvasWidth = (sWidth);
       
       Log.d("msg", "H: "+canvasHeight+" W: "+canvasWidth);
       
       Panel p = new Panel(this);
       //p.setLayoutParams(new ViewGroup.LayoutParams(canvasHeight, canvasWidth));
       setContentView(R.layout.main);
       LinearLayout l = (LinearLayout)findViewById(R.id.canvas);
       android.view.ViewGroup.LayoutParams params = l.getLayoutParams();
       params.height = canvasHeight;//(int) convertDpToPixel(canvasHeight, this);
       params.width = canvasWidth;//(int) convertDpToPixel(canvasWidth, this);
       l.setLayoutParams(params);
       l.addView(p);
       
       startService(new Intent(this, UpdateDb.class));
   }
   
   @Override
   public void onDestroy()
   {
       super.onDestroy();
       stopService(new Intent(this, UpdateDb.class));
   }
   public static float convertDpToPixel(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}
}