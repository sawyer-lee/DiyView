package com.zhuozheng.sawyerdiyview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.util.ScreenUtil;

import javax.security.auth.login.LoginException;

/**
 * 半圆形SeekBar-->HalfCircle
 * 功能：半圆形精度框，带有可拖动的滑块，有最小值和最大值
 * 1.先画出一定宽度的半圆形进度条
 * 2.指定滑块的初始位置，滑块为纯色的小圆，直径约等于进度条的宽度
 */
public class HalfCircle extends View {

    private static final int DEFAULT_EDGE_LENGTH = 260;             // 默认宽、高
    private static final int DEFAULT_ARC_WIDTH = 20;                // 默认宽度 dp
    private static final String KEY_PROGRESS_PRESENT = "PRESENT";   // 用于存储和获取当前百分比
    private static final int DEFAULT_MAX_VALUE = 100;               // 默认最大数值
    private static final int DEFAULT_MIN_VALUE = 0;                 // 默认最小数值
    private float mProgressPresent = 0;                             // 当前进度百分比

    private int mArcColor;          // SeekBar背景色
    private float mCenterX;         // 圆弧 SeekBar 中心点 X
    private float mCenterY;         // 圆弧 SeekBar 中心点 Y
    private float mArcWidth;        // SeekBar宽度
    private int mMaxValue;          // 最大数值
    private int mMinValue;          // 最小数值

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mThumbPaint;

    private Path mSeekPath;
    private PathMeasure mSeekPathMeasure;
    private float[] mTempPos;
    private float[] mTempTan;

    private RectF seekRectF;
    private RectF sideRectF;
    private int radius;

    public HalfCircle(Context context) {
        this(context,null);
    }

    public HalfCircle(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HalfCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置是否启用视图状态的保存，即调用onSaveInstanceState()
        setSaveEnabled(true);
        /**
         * View.setLayerType() 是直接把整个 View 都绘制在离屏缓冲中
         * setLayerType(LAYER_TYPE_HARDWARE) 是使用 GPU 来缓冲
         * setLayerType(LAYER_TYPE_SOFTWARE) 是直接用一个 Bitmap 来缓冲
         */
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        initAttrs(context, attrs);
        initData();
        initPaint();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat(KEY_PROGRESS_PRESENT, mProgressPresent);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        //todo
        super.onRestoreInstanceState(state);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.HalfCircle);
        mArcWidth = ta.getDimensionPixelSize(R.styleable.HalfCircle_circle_width,dp2px(DEFAULT_ARC_WIDTH));
        mArcColor = ta.getColor(R.styleable.HalfCircle_circle_background_color, Color.RED);
        mMaxValue = ta.getInt(R.styleable.HalfCircle_circle_max,DEFAULT_MAX_VALUE);
        mMinValue = ta.getInt(R.styleable.HalfCircle_circle_min,DEFAULT_MIN_VALUE);
        if (mMaxValue <= mMinValue) {
            mMaxValue = DEFAULT_MAX_VALUE;
            mMinValue = DEFAULT_MIN_VALUE;
        }
        int progress = ta.getInt(R.styleable.HalfCircle_circle_progress, mMinValue);
        ta.recycle();
    }

    private void initData() {
        mSeekPath = new Path();
        mSeekPathMeasure = new PathMeasure();
        mTempPos = new float[2];
        mTempTan = new float[2];
    }

    private void initPaint() {
        initArcPaint();
        initLinePaint();
    }

    // 初始化圆弧画笔
    private void initArcPaint() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initLinePaint(){
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mArcColor);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);     //取出宽的确切数值
        int wm = MeasureSpec.getMode(widthMeasureSpec);     //取出宽度的测量Mode
        int hs = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int hm = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量Mode

        if (wm == MeasureSpec.UNSPECIFIED){
            wm = MeasureSpec.EXACTLY;
            ws = dp2px(DEFAULT_EDGE_LENGTH);
        }else if (wm == MeasureSpec.AT_MOST){
            wm = MeasureSpec.EXACTLY;
            ws = Math.min(dp2px(DEFAULT_EDGE_LENGTH),ws);
        }

        if (hm == MeasureSpec.UNSPECIFIED){
            hm = MeasureSpec.EXACTLY;
            hs = dp2px(DEFAULT_EDGE_LENGTH);
        }else if (hm == MeasureSpec.AT_MOST){
            hm = MeasureSpec.EXACTLY;
            hs = Math.min(dp2px(DEFAULT_EDGE_LENGTH),hs);
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(ws,wm),MeasureSpec.makeMeasureSpec(hs,hm));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 计算在当前大小下,内容应该显示的大小和起始位置
        int safeW = w - getPaddingLeft() - getPaddingRight();
        int safeH = h - getPaddingTop() - getPaddingBottom();
        if (safeW < safeH){
            radius = safeW / 2;
        }else {
            radius = safeH / 2;
        }

        sideRectF = new RectF(0,0,2*radius,2*radius);
        mCenterX = sideRectF.centerX();
        mCenterY = sideRectF.centerY();
        seekRectF = new RectF(80,80,2*radius-80,2*radius-80);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        //x 轴的正向，即正右的方向，是 0 度的位置；顺时针为正角度，逆时针为负角度
        canvas.drawArc(seekRectF,0,-180,false,mArcPaint);
        canvas.translate(mCenterX,mCenterY);
        //循环画圆外的线
        for (int i =0; i <= 180 ; i+=9) {
            canvas.drawLine(radius-60,0,radius+mArcWidth/2-30,0,mLinePaint);
            canvas.rotate(-9);  //此处为关键
        }
        canvas.restore();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

}
