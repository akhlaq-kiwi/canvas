package com.example.canvasexample;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

public class ViewThread extends Thread {
	private Panel mPanel;
    private SurfaceHolder mHolder;
    private boolean mRun = false;
 
    public ViewThread(Panel panel) {
        mPanel = panel;
        mHolder = mPanel.getHolder();
    }
 
    public void setRunning(boolean run) {
        mRun = run;
    }
 
    @Override
    public void run() {
        Canvas canvas = null;
        while (mRun) {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                mPanel.doDraw(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
            //Log.d("msg", "running");
        }
    }
}
