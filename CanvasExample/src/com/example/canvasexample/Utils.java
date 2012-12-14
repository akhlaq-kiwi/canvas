package com.example.canvasexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	public static String APP_ID = "282573958514998";
	public static String HOST_URL = "http://mstage.ruckusreader.com/dropfour/";
	public static String response = "";
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    
    @SuppressLint("NewApi")
	public static String postData(final String page_uri, final String json_data){
    	
    	response = "";
    	
    	Thread thrd = new Thread(new Runnable() {
    		
    		
			@Override
			public void run() {
				// Create a new HttpClient and Post Header
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(HOST_URL+page_uri);

		        try {
		            // Add your data
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		            nameValuePairs.add(new BasicNameValuePair("json_data", json_data));
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            
		            // Execute HTTP Post Request
		            HttpResponse http_response = httpclient.execute(httppost);
		            //Utils.response = http_response.getEntity().getContent().toString();
		            InputStream is = http_response.getEntity().getContent();
		            
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line + "n");
		            }
		            is.close();
		            response = sb.toString();
		           
		        } catch (ClientProtocolException e) {
		            Log.d("Exp", "1");
		            e.printStackTrace();
		        } catch (IOException e) {
		        	Log.d("Exp", "2");
		        	e.printStackTrace();
		        }
		        
		       
			}
		});
    	thrd.start();
    	
    	while(response.isEmpty() == true)
    	{
    		//Log.d("Exp", "2ffffff");
    	}

    	
    	return response;
    }
}