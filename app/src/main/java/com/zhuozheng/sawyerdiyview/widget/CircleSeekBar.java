package com.zhuozheng.sawyerdiyview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

import com.zhuozheng.sawyerdiyview.R;

public class CircleSeekBar extends View {

    private Paint mPaint;
    private int mAnnulusWidth;              // 圆环宽度
    private int mAnnulusColor;              // 圆环颜色
    private int mLoadColor;                 // 加载进度圆弧扫过的颜色
    private int mTextColor;                 // 百分比文本颜色
    private int mTextSize;                  // 百分比文本大小

    private int mProgress ;
    private int minProgress = 0;
    private int maxProgress = 100;

    private int mProgressType;              // 类型：0代表实心  1代表空心
    public static final int FILL = 0;
    public static final int STROKE = 1;

    private int mIsShowText;                // 是否显示百分比文本
    private Rect rect;

    public CircleSeekBar(Context context) { this(context,null); }

    public CircleSeekBar(Context context, @Nullable AttributeSet attrs) { this(context,attrs,0); }

    public CircleSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleSeekBar);
        mAnnulusWidth = a.getDimensionPixelSize(R.styleable.CircleSeekBar_annulusWidth,dp2px(15));
        mAnnulusColor = a.getColor(R.styleable.CircleSeekBar_annulusColor, Color.BLACK);
        mLoadColor = a.getColor(R.styleable.CircleSeekBar_loadColor, Color.RED);
        mTextColor = a.getColor(R.styleable.CircleSeekBar_textColor,Color.BLACK);
        mTextSize = a.getDimensionPixelSize(R.styleable.CircleSeekBar_textSize,dp2px(24));
        mProgress = a.getInt(R.styleable.CircleSeekBar_progress,minProgress);
        mProgressType = a.getInt(R.styleable.CircleSeekBar_progressType,0);
        mIsShowText = a.getInt(R.styleable.CircleSeekBar_isShowText,0);
        a.recycle();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        rect = new Rect();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        // 返回边界最小矩形，用户测量文本高度，因为文本高度根据字体大小固定
        mPaint.getTextBounds("%",0,"%".length(),rect);
    }


    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        //绘制圆环
        int center = getWidth() / 2 ;
        int radius = center - mAnnulusWidth / 2;
        mPaint.setStrokeWidth(mAnnulusWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mAnnulusColor);
        canvas.drawCircle(center,center,radius,mPaint);

        //绘制弧环
        mPaint.setColor(mLoadColor);
        float leftTop = center - radius;
        float rightBottom = center + radius;

        switch (mProgressType){
            case FILL:
                mPaint.setStyle(Paint.Style.FILL);
                RectF ovalFill = new RectF(leftTop-mAnnulusWidth/2F,leftTop-mAnnulusWidth/2F,
                        rightBottom+mAnnulusWidth/2F,rightBottom+mAnnulusWidth/2F);
                canvas.drawArc(ovalFill,-90,(mProgress*360F/maxProgress),true,mPaint);
                break;
            case STROKE:
                mPaint.setStyle(Paint.Style.STROKE);
                RectF ovalStroke = new RectF(leftTop,leftTop,rightBottom,rightBottom);
                canvas.drawArc(ovalStroke,-90,(mProgress*360F/maxProgress),false,mPaint);
                break;
        }
        //如果不显示文本，直接return，不进行文字的绘制
        if(mIsShowText == 1){
            return;
        }
        // 计算圆弧进度获取文本内容
        int percentContext = (int) ((float)mProgress / (float)maxProgress * 100F);
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        float measureTextWidth = mPaint.measureText(percentContext+"%");
        canvas.drawText(percentContext+"%",center-measureTextWidth/2,center+rect.height()/2F,mPaint);
    }

    private int  dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    //根据外部进度传递更新View
    public synchronized void setProgress(final int progress) {
        this.mProgress = progress;
        new Thread(){
            @Override
            public void run() {
                if (mProgress == 100) {}
                postInvalidate();//此方法不可再主线程调用
            }
        }.start();
    }

}
