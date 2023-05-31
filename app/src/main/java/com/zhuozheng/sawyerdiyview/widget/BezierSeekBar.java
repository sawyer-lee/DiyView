package com.zhuozheng.sawyerdiyview.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.zhuozheng.sawyerdiyview.R;

import java.text.DecimalFormat;

public class BezierSeekBar extends View {

    private Context context;
    private DecimalFormat decimalFormat;
    private int mPaddingStart;
    private int mPaddingEnd;
    private int mPaddingTop;
    private int mPaddingBottom;

    private int width;
    private int height;
    /**
     * View默认高度
     */
    private int diameterDefault = 300;

    /**
     * 曲线弧高度
     */
    private float bezierHeight = 50F;

    private float circleRadiusMin = 15F;
    private float circleRadiusMax = circleRadiusMin * 1.5F;
    private float circleRadius = circleRadiusMin;

    /**
     * 圆与线的垂直距离
     */
    private float spaceToLine = circleRadiusMin * 2F;

    /**
     * 触摸点的坐标
     */
    private float fingerX, fingerXMin = circleRadiusMin, fingerXMax, fingerXDefault, fingerYDefault;

    private float textSelectedSize = 20f;

    private float textSize = 12f;

    private int colorValue, colorValueSelected, colorLine, colorBall, colorBgSelected;

    /**
     * 贝塞尔线的path
     */
    private Path bezierPath;

    /**
     * 曲线画笔
     */
    private Paint bezierPaint, ballPaint, textPaint, textDownPaint;
    private Point fingerPoint;
    private Paint txtSelectedBgPaint;
    private RectF bgRect;

    private String unit = "kg";
    private int valueMin, valueMax, valueSelected;

    private OnSelectedListener selectedListener;
    private boolean animInFinshed = false;
    private ValueAnimator animatorFingerIn, animatorFingerOut;
    private boolean robTouchEvent = false;
    public boolean isRobTouchEvent() {
        return robTouchEvent;
    }


    public BezierSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public BezierSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BezierSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attrs){
        this.context=context;
        decimalFormat=new DecimalFormat("#");
        textSelectedSize=dp2px(context,20F);
        textSize = dp2px(context, 12F);
        this.valueMax = 200;
        this.valueMin = 30;
        this.colorBall = this.colorLine = this.colorValue = Color.BLACK;
        this.colorValueSelected = Color.WHITE;
        this.fingerX = 100F;
        initAttr(context, attrs);
        this.bgRect = new RectF();

        //初始化贝塞尔曲线Path
        this.bezierPath = new Path();
        //初始化贝塞尔曲线Paint
        this.bezierPaint = new Paint();
        this.bezierPaint.setAntiAlias(true);
        this.bezierPaint.setStyle(Paint.Style.STROKE);
        this.bezierPaint.setColor(colorLine);
        this.bezierPaint.setStrokeWidth(5F);

        //初始化选中文字Paint
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setColor(colorValue);
        this.textPaint.setStrokeWidth(2F);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        this.textPaint.setTypeface(font);
        this.textPaint.setTextSize(textSelectedSize);

        //初始化选中值文本背景色Paint
        this.txtSelectedBgPaint = new Paint();
        this.txtSelectedBgPaint.setAntiAlias(true);
        this.txtSelectedBgPaint.setColor(colorBgSelected);
        this.txtSelectedBgPaint.setStyle(Paint.Style.FILL);

        //初始化选中文字放下后的Paint
        this.textDownPaint = new Paint();
        this.textDownPaint.setAntiAlias(true);
        this.textDownPaint.setStyle(Paint.Style.FILL);
        this.textDownPaint.setColor(colorValue);
        this.textDownPaint.setStrokeWidth(2F);
        this.textDownPaint.setTextSize(textSize);

        //初始化圆球的Paint
        this.ballPaint = new Paint();
        this.ballPaint.setAntiAlias(true);
        this.ballPaint.setStyle(Paint.Style.FILL);
        this.ballPaint.setColor(colorBall);

        this.fingerXDefault = 200f;
        this.fingerYDefault = 200f;

        //初始化动画: 放大
        this.animatorFingerIn = ValueAnimator.ofFloat(0f,1f);
        this.animatorFingerIn.setDuration(200L);
        this.animatorFingerIn.setInterpolator(new LinearInterpolator());
        //初始化动画: 缩小
        this.animatorFingerOut = ValueAnimator.ofFloat(1f,0f);
        this.animatorFingerOut.setDuration(200L);
        this.animatorFingerOut.setInterpolator(new LinearInterpolator());

        this.animatorFingerIn.addUpdateListener((animation) -> {
            float progress = (float) animation.getAnimatedValue();

            animInFinshed = (progress >= 0.15f);
            //选中值文本背景色做一个透明度的变化
            txtSelectedBgPaint.setAlpha((int)(255 * (progress - 0.15F)));
            if (progress >0.95F){
                textPaint.setColor(colorValueSelected);
            }else {
                textPaint.setColor(colorValue);
            }
            //Bezier曲线高度变化
            bezierHeight = circleRadiusMax * 1.5F * progress;
            //圆球半径变化
            circleRadius = circleRadiusMin + (circleRadiusMax - circleRadiusMin) * progress;
            spaceToLine = circleRadiusMin * 2 * (1F - progress);
            postInvalidate();
        });

        this.animatorFingerOut.addUpdateListener((animation) -> {
            float progress = (float) animation.getAnimatedValue();

            animInFinshed = (progress >= 0.15F);
            txtSelectedBgPaint.setAlpha((int) (255 * (progress - 0.15F)));
            if (progress >= 0.95F) {
                textPaint.setColor(colorValueSelected);
            } else {
                textPaint.setColor(colorValue);
            }
            bezierHeight = circleRadiusMax * 1.5F * progress;
            circleRadius = circleRadiusMin + (circleRadiusMax - circleRadiusMin) * progress;
            spaceToLine = circleRadiusMin * 2 * (1F - progress);
            postInvalidate();
        });
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs !=null){
            TypedArray attributes=context.obtainStyledAttributes(attrs, R.styleable.BezierSeekBar);

            this.colorBall= attributes.getColor(R.styleable.BezierSeekBar_bsBar_color_ball, Color.BLACK);
            this.colorLine = attributes.getColor(R.styleable.BezierSeekBar_bsBar_color_line, Color.BLACK);
            this.colorValue = attributes.getColor(R.styleable.BezierSeekBar_bsBar_color_value, Color.BLACK);
            this.colorValueSelected = attributes.getColor(R.styleable.BezierSeekBar_bsBar_color_value_selected, Color.WHITE);
            this.colorBgSelected = attributes.getColor(R.styleable.BezierSeekBar_bsBar_color_bg_selected, Color.BLACK);
            this.valueMin = attributes.getInteger(R.styleable.BezierSeekBar_bsBar_value_min, 0);
            this.valueMax = attributes.getInteger(R.styleable.BezierSeekBar_bsBar_value_max, 100);
            this.valueSelected = attributes.getInteger(R.styleable.BezierSeekBar_bsBar_value_selected, 45);
            this.unit = attributes.getString(R.styleable.BezierSeekBar_bsBar_unit) + "";

            attributes.recycle();
        }
    }

    /**
     *处理wrap_content不起作用的情况
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        if (widthMode ==MeasureSpec.AT_MOST && heightMode== MeasureSpec.AT_MOST){
            width=diameterDefault;
            height=diameterDefault;
            setMeasuredDimension(diameterDefault,diameterDefault);
        }else if (widthMode ==MeasureSpec.AT_MOST){
            width=diameterDefault;
            height=heightSize;
            setMeasuredDimension(diameterDefault,heightSize);
        }else if (heightMode ==MeasureSpec.AT_MOST){
            width=widthSize;
            height=diameterDefault;
            setMeasuredDimension(widthSize,diameterDefault);
        }
        fingerXMax=width-circleRadiusMin;
        this.fingerXDefault = width * (float) (this.valueSelected - valueMin) / (float) (this.valueMax - this.valueMin);
        this.fingerX = this.fingerXDefault;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mPaddingStart = getPaddingStart();
        mPaddingEnd = getPaddingEnd();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

        bezierHeight = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画线line 1: 线的起点到左侧贝塞尔曲线的起点
        bezierPath.reset();
        bezierPath.moveTo(0, (float) 2 * height / 3);
        bezierPath.lineTo(this.fingerX - circleRadiusMax * 2 * 3, (float) 2 * height / 3);

        //左侧三次Bezier曲线
        bezierPath.cubicTo(this.fingerX - circleRadiusMax * 2 * 2, (float) 2 * height / 3,
                this.fingerX - circleRadiusMax * 2 * 1, (float) 2 * height / 3 - bezierHeight,
                this.fingerX, (float) 2 * height / 3 - bezierHeight);

        //右侧侧三次Bezier曲线
        bezierPath.cubicTo(this.fingerX + circleRadiusMax * 2, (float) 2 * height / 3 - bezierHeight,
                this.fingerX + circleRadiusMax * 2 * 2, (float) 2 * height / 3,
                this.fingerX + circleRadiusMax * 2 * 3, (float) 2 * height / 3);

        //画线line 2: 右侧贝塞尔曲线的终点到线的终点
        bezierPath.lineTo(width, (float) 2 * height / 3);
        canvas.drawPath(bezierPath,bezierPaint);

        //画圆球
        canvas.drawCircle(this.fingerX,(float) 2 * height / 3 + spaceToLine + circleRadius,circleRadius,ballPaint);
        //起点文字
        canvas.drawText("" + valueMin, 1F, (float) 2 * height / 3F + textSize, textDownPaint);
        //终点文字
        canvas.drawText("" + valueMax, width - getTextWidth(textDownPaint, "200") - 1F,
                (float) 2 * height / 3F + dp2px(getContext(), 12F), textDownPaint);

        //选中文字及背景框
        String text = valueSelected + unit;

        float valueX = this.fingerX - getTextWidth(textPaint, text) / 2F - 20F;
        float valueXend = this.fingerX + getTextWidth(textPaint, text) / 2F + 20F;
        //滑动至最左边的情况
        if (valueX <= 0) {
            valueX = 0F;
            valueXend = getTextWidth(textPaint, text) + 40F;
        }
        //滑动至最右边的情况
        if (valueXend >= width) {
            valueXend = width;
            valueX = width - getTextWidth(textPaint, text) - 40F;
        }

        if (animInFinshed) {
            bgRect.set(valueX,
                    (float) 2 * height / 3F - bezierHeight * 2 - 30F - getTextHeight(textPaint, text),
                    valueXend,
                    (float) 2 * height / 3F - bezierHeight * 2 + 10F);
            canvas.drawRoundRect(bgRect, 20F, 20F, txtSelectedBgPaint);
        }
        canvas.drawText(text, valueX + 20F, (float) 2 * height / 3F - bezierHeight * 2 - 15F, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                fingerX=event.getX();
                if (fingerX < fingerXMin) fingerX = fingerXMin;
                if (fingerX > fingerXMax) fingerX = fingerXMax;
                //执行放大的动画
                this.animatorFingerIn.start();
                break;
            case MotionEvent.ACTION_MOVE:
                robTouchEvent= true;
                fingerX = event.getX();
                if (fingerX < fingerXMin) fingerX = fingerXMin;
                if (fingerX > fingerXMax) fingerX = fingerXMax;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                robTouchEvent = false;
                //执行缩小的动画
                this.animatorFingerOut.start();
                break;
        }
        valueSelected = Integer.parseInt(decimalFormat.format(
                valueMin + (valueMax - valueMin) * (fingerX - fingerXMin) / (fingerXMax - fingerXMin)));
        if (selectedListener != null) {
            selectedListener.onSelected(valueSelected);
        }
        return true;
    }

    private float px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return px / scale + 0.5f;
    }

    private float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float getTextWidth(Paint paint, String str) {
        float iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (float) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private float getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return (float) rect.height();
    }

    //=====================================================================================
    //提供getter/setter方法给代码动态添加View使用
    //=====================================================================================

    //最大值
    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }
    public int getValueMax() {
        return valueMax;
    }
    //最小值
    public void setValueMin(int valueMin) {
        this.valueMin = valueMin;
    }
    public int getValueMin() {
        return valueMin;
    }
    //提示文本正常颜色
    public int getColorValue() {
        return colorValue;
    }
    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
        this.textDownPaint.setColor(this.colorValue);
    }
    //提示文本选中颜色
    public int getColorValueSelected() {
        return colorValueSelected;
    }
    public void setColorValueSelected(int colorValueSelected) {
        this.colorValueSelected = colorValueSelected;
        this.textPaint.setColor(this.colorValue);
    }
    //线的颜色
    public int getColorLine() {
        return colorLine;
    }
    public void setColorLine(int colorLine) {
        this.colorLine = colorLine;
        this.bezierPaint.setColor(this.colorLine);
    }
    //圆球颜色
    public int getColorBall() {
        return colorBall;
    }
    public void setColorBall(int colorBall) {
        this.colorBall = colorBall;
        this.ballPaint.setColor(this.colorBall);
    }
    //提示文本背景色
    public void setColorBgSelected(int colorBgSelected) {
        this.colorBgSelected = colorBgSelected;
        this.txtSelectedBgPaint.setColor(this.colorBgSelected);
    }
    //默认选中值
    public void setValueSelected(int valueSelected) {
        this.valueSelected = valueSelected;
        this.fingerXDefault = fingerXMax * (float) (this.valueSelected - valueMin) / (float) (this.valueMax - this.valueMin);
        this.fingerX = this.fingerXDefault;
        postInvalidate();
    }
    public int getValueSelected() {
        return valueSelected;
    }
    //单位
    public void setUnit(String unit) {
        this.unit = unit;
    }
    //点击选中的监听
    public void setSelectedListener(OnSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }
}
