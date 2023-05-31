package com.zhuozheng.sawyerdiyview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.annotation.Nullable;
import com.zhuozheng.sawyerdiyview.R;

public class AnimLogoView extends View {
    private static final String DEFAULT_LOGO = "SAWYERLee";
    private static final int DEFAULT_TEXT_PADDING = 10;
    private static final int ANIM_LOGO_DURATION = 1500;
    private static final int ANIM_LOGO_GRADIENT_DURATION = 1500;
    private static final int ANIM_LOGO_TEXT_SIZE = 30;
    private static final int ANIM_LOGO_TEXT_COLOR = Color.BLACK;
    private static final int ANIM_LOGO_GRADIENT_COLOR = Color.YELLOW;
    private SparseArray<String> mLogoTexts = new SparseArray<>();
    private SparseArray<PointF> mQuietPoints = new SparseArray<>();  //最终合成logo后的坐标
    private SparseArray<PointF> mRandomPoints = new SparseArray<>();  //logo被随机打散的坐标
    private ValueAnimator mOffsetAnimator;
    private ValueAnimator mGradientAnimator;
    private Paint mPaint;
    private int mTextPadding;
    private int mTextColor;
    private int mTextSize;
    private float mOffsetAnimProgress;
    private int mOffsetDuration;
    private boolean isOffsetAnimEnd;
    private int mGradientDuration;
    private LinearGradient mLinearGradient;
    private int mGradientColor;
    private Matrix mGradientMatrix;
    private int mMatrixTranslate;
    private boolean isAutoPlay;
    private int mWidth, mHeight;
    private boolean isShowGradient;
    private int mLogoOffset;

    public AnimLogoView(Context context) { this(context, null); }

    public AnimLogoView(Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }

    public AnimLogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimLogoView);
        String logoName = ta.getString(R.styleable.AnimLogoView_logoName);
        isAutoPlay = ta.getBoolean(R.styleable.AnimLogoView_autoPlay, true);
        isShowGradient = ta.getBoolean(R.styleable.AnimLogoView_showGradient, false);
        mOffsetDuration = ta.getInt(R.styleable.AnimLogoView_offsetAnimDuration, ANIM_LOGO_DURATION);
        mGradientDuration = ta.getInt(R.styleable.AnimLogoView_gradientAnimDuration, ANIM_LOGO_GRADIENT_DURATION);
        mGradientColor = ta.getColor(R.styleable.AnimLogoView_gradientColor, ANIM_LOGO_GRADIENT_COLOR);
        mTextSize = ta.getDimensionPixelSize(R.styleable.AnimLogoView_logoTextSize, ANIM_LOGO_TEXT_SIZE);
        mTextColor = ta.getColor(R.styleable.AnimLogoView_logoTextColor, ANIM_LOGO_TEXT_COLOR);
        mTextPadding = ta.getDimensionPixelSize(R.styleable.AnimLogoView_textPadding, DEFAULT_TEXT_PADDING);
        mLogoOffset = ta.getDimensionPixelOffset(R.styleable.AnimLogoView_verticalOffset, 0);
        ta.recycle();
        if (TextUtils.isEmpty(logoName)) logoName = DEFAULT_LOGO;
        fillLogoTextArray(logoName);
        initPaint();
        initOffsetAnimation();
    }

    /**对于数据来源，期望传入一个logo的字符串，内部将字符串拆解为单个文字数组*/
    private void fillLogoTextArray(String logoName) {
        if (TextUtils.isEmpty(logoName)) return;
        if (mLogoTexts.size() > 0) mLogoTexts.clear();
        for (int i = 0; i <logoName.length() ; i++) {
            char c = logoName.charAt(i);
            mLogoTexts.put(i,String.valueOf(c));
        }
    }

    private void initPaint() {

    }

    /**初始化文字平移动画*/
    private void initOffsetAnimation() {}

    /**初始化文字渐变色动画*/
    private void initGradientAnimation(int width) {}

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        mWidth = w;
        mHeight = h;
        initLogoCoordinate();
    }

    /**初始化文字散落位置*/
    private void initLogoCoordinate() {
        if (mWidth == 0 || mHeight == 0) {
            Log.i("lee", "The view has not measure, it will auto init later.");
            return;
        }
        //最终展示文本的Y坐标
        float centerY = mHeight / 2f + mPaint.getTextSize() / 2 + mLogoOffset;
        float totalLength = 0;
        //计算文本的最终长度
        for (int i = 0; i < mLogoTexts.size(); i++) {
            String str = mLogoTexts.get(i);
            float currentLength = mPaint.measureText(str);
            if (i != mLogoTexts.size()-1){
                totalLength += currentLength + mTextPadding;
            }else {
                totalLength += currentLength;
            }
        }
        if (totalLength > mWidth){
            throw new IllegalStateException("The text of logoName is too large that this view can not display all");
        }

        //最终展示文本的X坐标
        float startX = (mWidth - totalLength) / 2;

        if (mQuietPoints.size() > 0) {
            mQuietPoints.clear();
        }
        for (int i = 0; i < mLogoTexts.size(); i++) {
            String str = mLogoTexts.get(i);
            float currentLength = mPaint.measureText(str);
            mQuietPoints.put(i,new PointF(startX,centerY));
            startX += currentLength + mTextPadding;
        }

        if (mRandomPoints.size() > 0) {
            mRandomPoints.clear();
        }
        for (int i = 0; i < mLogoTexts.size(); i++) {
            mRandomPoints.put(i,new PointF((float)Math.random()*mWidth,(float)Math.random()*mHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
