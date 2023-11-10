package com.zhuozheng.sawyerdiyview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewHandWriting extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Path mPath;
    private boolean mIsDrawing;
    private int x = 0, y = 0;
    private Canvas mCanvas;

    public SurfaceViewHandWriting(Context context) { this(context,null); }

    public SurfaceViewHandWriting(Context context, AttributeSet attrs) { this(context, attrs, 0); }

    public SurfaceViewHandWriting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mPath.moveTo(0,100);//设置初始点
        initView();
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { mIsDrawing = false; }

    @Override
    public void run() {
        //没有让线程一直运行，而是让它休眠一会，从而节约系统资源，
        //一般建议判断的阈值为50-100之间即可,保证用户体验同时节约系统资源
        while (mIsDrawing){
            long start = System.currentTimeMillis();
            drawPoint();//绘制sin函数曲线
            long end = System.currentTimeMillis();
            if (end - start < 100){
                try {
                    Thread.sleep(100 - (end - start));
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        }
    }


    //(1) 通过lockCanvas()方法获得Canvas对象
    //(2) 在子线程中使用Canvas对象进行绘制
    //(3) 使用unlockCanvasAndPost()方法将画布内容进行提交
    //注意: lockCanvas() 方法获得的Canvas对象仍然是上次绘制的对象，由于我们是不断进行绘制，
    //但是每次得到的Canvas对象都是第一次创建的Canvas对象。

    private void drawPoint() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);//绘制背景
            mCanvas.drawPath(mPath,mPaint);//绘制路径
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放Canvas对象并提交画布
            if (mCanvas != null) mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
