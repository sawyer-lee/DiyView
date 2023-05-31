package com.zhuozheng.sawyerdiyview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class PracticeView extends View {

    private Paint mPaint;
    private int mWidth,mHeight;
    private RectF mRectF;

    public PracticeView(Context context) {
        this(context,null);
    }

    public PracticeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PracticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mRectF = new RectF(-400f,-400f,400f,400f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
        //translate是坐标系的移动，可以为图形绘制选择一个合适的坐标系，
        //请注意：位移是基于当前位置移动，而不是每次基于屏幕左上角的(0,0)点移动
        mPaint.setColor(Color.BLACK);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);
        mPaint.setColor(Color.CYAN);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);*/

        /*mPaint.setColor(Color.CYAN);
        //移动坐标系原点至屏幕中心点
        canvas.translate(mWidth/2f,mHeight/2f);
        canvas.scale(1,-1);
        canvas.drawCircle(-200,200,100,mPaint);*/

        /*mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        canvas.translate(mWidth/2f,mHeight/2f);
        for (int i = 0; i <20; i++) {
            canvas.scale(0.9f,0.9f);
            canvas.drawRect(mRectF,mPaint);
        }*/

        mPaint.setColor(Color.CYAN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        canvas.translate(mWidth/2f,mHeight/2f);
        canvas.drawCircle(0,0,400,mPaint);
        canvas.drawCircle(0,0,360,mPaint);
        for (int i = 0; i <= 180; i+=10) {
            canvas.drawLine(360,0,400,0,mPaint);
            canvas.rotate(-10);
        }
    }
}
