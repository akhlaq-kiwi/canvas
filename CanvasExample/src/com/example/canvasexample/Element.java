package com.example.canvasexample;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Canvas;
import android.util.Log;

public class Element {
    private int mX, mY, posX, posY, posToX, posToY, game_id;
    private boolean head, moving;
    private String filename; 
    private Bitmap mBitmap;
    private String player;
    
    public boolean getMoving(){
    	return this.moving;
    }
    
    public void setMoving(boolean moving){
    	this.moving = moving;
    }
    
    public String getFilename(){
    	return this.filename;
    }
    
    public void setFilename(String filename){
    	this.filename = filename;
    }
    
    public String getPlayer(){
    	return this.player;
    }
    
    public void setPlayer(String player){
    	this.player = player;
    }
    
    public int getGameId(){
    	return this.game_id;
    }
    
    public void setGameId(int game_id){
    	this.game_id = game_id;
    }
    
    public String getFileName(){
    	return this.filename;
    }
    
    public void setFileName(String filename){
    	this.filename = filename;
    }
    
    public void setPosToY(int posToY){
    	this.posToY = posToY;
    }
    public int getPosToY(){
    	return this.posToY;
    }
    
    public void setPosToX(int posToX){
    	this.posToX = posToX;
    }
    public int getPosToX(){
    	return this.posToX;
    }
    
    public void setPosY(int posY){
    	this.posY = posY;
    }
    public int getPosY(){
    	return this.posY;
    }
    
    public void setPosX(int posX){
    	this.posX = posX;
    }
    public int getPosX(){
    	return this.posX;
    }
    
    public boolean getElementTouched(int x, int y) {
		if((mX < x && x < mX+mBitmap.getWidth()) && (mY < y && y < mY+mBitmap.getHeight()) && this.head){
			return true;
		}else{
			return false;
		}
    }
    public Element(Resources res, int x, int y, boolean head) {
        if(head){
        	mBitmap = BitmapFactory.decodeResource(res, R.drawable.down_arrow);
        }else if(filename == "red"){
        	mBitmap = BitmapFactory.decodeResource(res, R.drawable.red);
        }else{
        	mBitmap = BitmapFactory.decodeResource(res, R.drawable.green);        }
        this.mX = mBitmap.getWidth()*x;
        this.mY = mBitmap.getHeight()*y;
        this.posX = x;
        this.posY = y;
    }
    public boolean movingState(){
    	boolean res;
    	if(moving && posToY*50 > mY){
    		mY += 50;
    		res = true;
    	}else{
    		this.posX = this.posToX;
    		this.posY = this.posToY;
    		moving = false;
    		res = false;
    	}
    	return res;
    }
    public void doDraw(Canvas canvas, Panel p) {
    	canvas.drawBitmap(mBitmap, mX, mY, null);
    }
}
