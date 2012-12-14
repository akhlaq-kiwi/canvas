package com.example.canvasexample;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {
	public ViewThread mThread;
	private Bitmap mBitmap;
	private int mx, my, game_id = 1;
	String me = "11112";
	private int rowsX = 9;
	private int rowsY = 9;
	public static String filename = "";
	Context ctx;
	public static boolean mDisableTouch = false;
	private ArrayList<Element> mHead = new ArrayList<Element>();
	private ArrayList<Element> mElements = new ArrayList<Element>();
	private ArrayList<Element> tElements = new ArrayList<Element>();
	DatabaseHandler db = new DatabaseHandler(getContext());
	
	public Panel(Context context) {
	    super(context);
	    ctx=context;
	    mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red);
	    getHolder().addCallback(this);
	    mThread = new ViewThread(this);
	    drawHead();
	    mElements = db.getAllElements(me);
	}
	 
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	    if (!mThread.isAlive()) {
	        mThread = new ViewThread(this);
	        mThread.setRunning(true);
	        mThread.start();
	    }
	}
	public void doDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        ArrayList<String> res = db.checkMoving(game_id, me);
        String status = res.get(0);
        
        if(status.equals("true")){
        	String poxX = res.get(2);
        	int x = Integer.parseInt(poxX);
        	createMovingElement(x, 0, true, res.get(3));
        }
        //mElements = db.getAllElements();
        synchronized (mElements) {
            for (Element element : mElements) {
            	element.doDraw(canvas, this);
            }
        }
        
        synchronized (mHead) {
            for (Element mhead : mHead) {
            	mhead.doDraw(canvas, this);
            }
        }
        synchronized (tElements) {
            for (Element tElement : tElements) {
            	if(tElement.movingState()){
            		tElement.doDraw(canvas, this);
            	}else{
            		
            		if(db.checkMoved(game_id)){
            			db.updateMoved(game_id);
            		}else{
            			db.addElement(tElement);
            		}
            		mElements.add(tElement);
            		//mElements = db.getAllElements();
            		synchronized (mElements) {
                        for (Element element : mElements) {
                        	element.doDraw(canvas, this);
                        }
                    }
            		tElements.remove(0);
            	}
            	
            }
        }
        
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		Boolean myturn = db.checkForMyTurn(game_id, me);
		
		if(tElements.size()==0 && myturn){
			createMovingElement((int)e.getX(), (int)e.getY(), false, "");
		}
		//db.addElement(ele);
		return super.onTouchEvent(e);
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    if (mThread.isAlive()) {
	        mThread.setRunning(false);
	    }
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	public void createMovingElement(int x, int y, boolean exact, String file_name){
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.down_arrow);
		int bHeight = mBitmap.getHeight();
		int bWidth = mBitmap.getWidth();
		int posX, posY;
		if(exact){
			posX = x;
			posY = 0;
		}else{
			posX = (int)Math.ceil(x/bWidth);
			posY = (int)Math.ceil(y/bHeight);
		}
		if(file_name.equals("")){
			if(db.checkForFirtsPlayer(me, game_id)){
				this.filename="red";
			}else{
				this.filename="green";
			}
		}else{
			this.filename=file_name;
		}
		if(posY==0){
			Element ele = new Element(getResources(), posX, posY, false, this.filename);
			ArrayList<String> m = db.getMovePosition(game_id, posX, me);
			int i = Integer.parseInt(m.get(0));
			//Log.d("msg", ""+i);
			if(exact){
				ele.setPosToY(i);
			}else{
				ele.setPosToY(i-1);
			}
			ele.setPosToX(posX);
			ele.setGameId(game_id);
			ele.setPlayer(me);
			ele.setMoving(true);
			if(i>1){
				tElements.add(ele);
			}
		}
	}
	
	public void drawHead(){
		for(int i=0; i < rowsX; i++){
			mHead.add(new Element(getResources(), i,0, true, ""));
		}
	}
	
	
}
