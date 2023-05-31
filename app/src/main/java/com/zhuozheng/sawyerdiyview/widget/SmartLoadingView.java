package com.zhuozheng.sawyerdiyview.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.util.ScreenUtil;

public class SmartLoadingView extends androidx.appcompat.widget.AppCompatTextView {

    //view的宽度
    private int width;
    //View的高度
    private int height;
    //获取文字绘画区域
    private Rect mRect;
    //按钮文字
    private String normalString = getResources().getString(R.string.normalString);
    private String errorString = getResources().getString(R.string.errorString);
    //当前要绘画的text
    private String currentString;
    //不可点击的背景颜色
    private int cannotclick_color;
    //加载失败的背景颜色
    private int error_color;
    //正常情况下背景颜色
    private int normal_color;
    //是否可以点击状态
    private boolean smartClickable;
    //圆角半径
    private int circleAngle;
    //从用户获得的圆角
    private int obtainCircleAngle;
    //文字滚动模式，默认为1：来回滚动
    private int textScrollMode = 1;
    //文字滚动速度
    private int speed;
    //关注的样式，默认是正常样式
    private int clickType = 1;
    //圆角矩形画笔
    private Paint paint;
    //对勾（√）画笔
    private Paint okPaint;
    //当前字体颜色值
    private int textColor;
    //当前字体透明度
    private int textAlpha;
    //文字画笔
    private Paint textPaint;
    //根据view的大小设置成矩形
    private RectF rectf = new RectF();
    //文字绘制所在矩形
    private Rect textRect = new Rect();
    //当前矩形在x轴left的位置
    private int current_left;
    //文字超过一行时，进行的文字滚动动画
    //模式一:来回滚动
    private ValueAnimator animator_text_scroll;
    //模式二：仿跑马灯
    private ValueAnimator animator_marque;
    /**
     * 加载loading动画相关
     */
    //是否开始绘画，加载转圈动画
    private boolean isDrawLoading = false;
    //是否处于加载状态，注意，这里和上面是不同的概念，只要点击按钮，没有走错误和走正确的都视为在加载状态下
    private boolean isLoading = false;
    private int startAngle = 0;
    private int progAngle = 30;
    private boolean isAdd = true;
    private int drawTextStart;
    private int drawMarqueTextStart;

    //是否开始绘制对勾
    private boolean startDrawOk = false;
    //绘制对勾（√）的动画
    private ValueAnimator animator_draw_ok;
    //对路径处理实现绘制动画效果
    private PathEffect effect;
    //路径--用来获取对勾的路径
    private Path path = new Path();
    //取路径的长度
    private PathMeasure pathMeasure;
    //矩形两边需要缩短的距离
    private int default_all_distance;
    /**
     * 动画集
     */
    //这是开始的启动
    private AnimatorSet animatorSet = new AnimatorSet();
    //这是网络错误的
    private AnimatorSet animatorNetfail = new AnimatorSet();
    //矩形-->正方形过度的动画
    private ValueAnimator animator_rect_to_square;
    //正方形-->矩形动画
    private ValueAnimator animator_squareToRect;
    //矩形-->圆角矩形过度的动画
    private ValueAnimator animator_rect_to_angle;
    //圆角矩形-->矩形的动画
    private ValueAnimator animator_angle_to_rect;

    public SmartLoadingView(@NonNull Context context) {
        this(context,null);
    }

    public SmartLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SmartLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();
        init(attrs);
        initPaint();
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.SmartLoadingView);
        //设置文本
        if (TextUtils.isEmpty(getText())){
            currentString = normalString;
        }else {
            normalString = (String) getText();
            currentString = normalString;
        }

        String currentErrorString = typedArray.getString(R.styleable.SmartLoadingView_errorMsg);
        if (!TextUtils.isEmpty(currentErrorString)){
            errorString = currentErrorString;
        }
        cannotclick_color = typedArray.getColor(R.styleable.SmartLoadingView_background_cannotClick,
                getResources().getColor(R.color.blackbb));
        error_color = typedArray.getColor(R.styleable.SmartLoadingView_background_error,
                getResources().getColor(R.color.remind_color));
        normal_color = typedArray.getColor(R.styleable.SmartLoadingView_background_normal,
                getResources().getColor(R.color.guide_anim));
        smartClickable = typedArray.getBoolean(R.styleable.SmartLoadingView_smart_clickable,true);
        obtainCircleAngle = (int)typedArray.getDimension(R.styleable.SmartLoadingView_cornerRaius,
                getResources().getDimension(R.dimen.default_corner));
        textScrollMode = typedArray.getInt(R.styleable.SmartLoadingView_textScrollMode,1);
        speed = typedArray.getInt(R.styleable.SmartLoadingView_speed,400);
        clickType = typedArray.getInt(R.styleable.SmartLoadingView_click_mode, 1);
        int paddingTop = getPaddingTop() == 0 ? ScreenUtil.dp2px(getContext(),7) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? ScreenUtil.dp2px(getContext(),7) : getPaddingBottom();
        int paddingLeft = getPaddingLeft() == 0 ? ScreenUtil.dp2px(getContext(),15) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? ScreenUtil.dp2px(getContext(),15) : getPaddingRight();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        setBackgroundColor(0);
        setMaxLines(1);
        setGravity(Gravity.CENTER);
    }

    private void initPaint() {
        //矩形画笔属性设置
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        if (smartClickable){
            paint.setColor(normal_color);
        }else{
            paint.setColor(cannotclick_color);
        }
        //对勾画笔属性设置
        okPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        okPaint.setStrokeWidth(5);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setAntiAlias(true);
        okPaint.setStrokeCap(Paint.Cap.ROUND);
        ColorStateList textColors = getTextColors();
        final int[] drawableState = getDrawableState();
        //获取TextView默认颜色值
        textColor = textColors.getColorForState(drawableState,0);
        //对勾的颜色与文字颜色一致
        okPaint.setColor(textColor);
        textAlpha = Color.alpha(textColor);
        //文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(getTextSize());
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        errorString = (String) text;
        normalString = (String) text;
        currentString = (String) text;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (width == 0){
            width = w;
            height = h;
            if (obtainCircleAngle > (height/2)){
                obtainCircleAngle = height/2;
            }
            circleAngle = obtainCircleAngle;
            default_all_distance = (w - h) / 2;
            initOk();
            initAnimation();
            //如果不是精准模式，我们代码里设置第一次的长宽，成为精准模式
            //这样避免，更改文字内容时，总是会改变控件的长宽
            setWidth(width);
            setHeight(height);
            setClickable(smartClickable);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw_oval_to_circle(canvas);
        drawText(canvas);
        //绘制加载进度
        if (isDrawLoading){
            canvas.drawArc(new RectF(width / 2 - height / 2 + height / 4,
                    height / 4,
                    width / 2 + height / 2 - height / 4,
                    height / 2 + height / 2 - height / 4),startAngle,progAngle,false,okPaint);
            startAngle += 6;
            if (progAngle >= 270) {
                progAngle -= 2;
                isAdd = false;
            } else if (progAngle <= 45) {
                progAngle += 6;
                isAdd = true;
            } else {
                if (isAdd) {
                    progAngle += 6;
                } else {
                    progAngle -= 2;
                }
            }
            postInvalidate();
        }
        //绘制对勾
        if (startDrawOk){
            canvas.drawPath(path,okPaint);
        }
    }

    private void draw_oval_to_circle(Canvas canvas) {
        rectf.left = current_left;
        rectf.top = 0;
        rectf.right = width - current_left;
        rectf.bottom = height;
        //画圆角矩形
        canvas.drawRoundRect(rectf,circleAngle,circleAngle,paint);
    }

    private void drawText(final Canvas canvas) {
        int sc = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
        rectf.left = current_left + getPaddingLeft();
        rectf.top = 0;
        rectf.right = width - current_left - getPaddingRight();
        rectf.bottom = height;
        //画圆角矩形
        canvas.drawRoundRect(rectf,circleAngle,circleAngle,paint);
        //设置混合模式
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        textRect.left = getPaddingLeft();
        textRect.top = 0;
        textRect.right = width - getPaddingRight();
        textRect.bottom = height;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        final int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //测量文字的长度
        int myTotal = (int)(textPaint.measureText(currentString) + getPaddingRight() + getPaddingLeft());
        if (myTotal > getWidth()){
            if (textScrollMode ==1){
                //文本对齐方式
                textPaint.setTextAlign(Paint.Align.LEFT);
                if (animator_text_scroll == null && !isLoading){
                    //此时文字长度已经超过一行，进行文字滚动
                    animator_text_scroll = ValueAnimator.ofInt(textRect.left,
                            (int)(textRect.left - textPaint.measureText(currentString)
                                    + (getWidth() - getPaddingLeft() - getPaddingRight())));
                    animator_text_scroll.setDuration(currentString.length() * speed);
                    animator_text_scroll.setRepeatMode(ValueAnimator.REVERSE);
                    animator_text_scroll.setRepeatCount(-1);
                    animator_text_scroll.addUpdateListener(animation -> {
                        drawTextStart = (int) animation.getAnimatedValue();
                        postInvalidate();
                    });
                    animator_text_scroll.start();
                }
                canvas.drawText(currentString,drawTextStart,baseline,textPaint);
            }else {
                textPaint.setTextAlign(Paint.Align.LEFT);
                if (animator_text_scroll == null && !isLoading) {
                    //此时文字长度已经超过一行，进行文字滚动
                    animator_text_scroll = ValueAnimator.ofInt(textRect.left,
                            (int)(textRect.left - textPaint.measureText(currentString)));
                    animator_text_scroll.setDuration(currentString.length() * speed);
                    animator_text_scroll.setInterpolator(new LinearInterpolator());
                    animator_text_scroll.addUpdateListener(animation -> {
                        drawTextStart = (int) animation.getAnimatedValue();
                        postInvalidate();
                        if (drawTextStart == textRect.left){
                            if (animator_marque != null){
                                animator_marque.cancel();
                                animator_marque = null;
                            }
                        }
                        if (animator_marque == null && !isLoading && drawTextStart <= (
                                (int) (textRect.left - textPaint.measureText(currentString)
                                        + (getWidth() - getPaddingLeft() - getPaddingRight())
                                        - (getWidth() - getPaddingLeft() - getPaddingRight()) / 3)
                                )){
                            int duration = (int) (((currentString.length() * speed)
                                    * (textRect.right - textRect.left))
                                    / textPaint.measureText(currentString));
                            animator_marque = ValueAnimator.ofInt(textRect.right,textRect.left);
                            animator_marque.setDuration(duration);
                            animator_marque.setInterpolator(new LinearInterpolator());
                            animator_marque.addUpdateListener(animation1 -> {
                                drawMarqueTextStart = (int) animation.getAnimatedValue();
                                if (drawMarqueTextStart == textRect.left){
                                    mHandler.sendEmptyMessageDelayed(14,1500);
                                }
                            });
                            postInvalidate();
                        }

                    });
                    animator_text_scroll.start();
                }
                if (animator_marque != null) {
                    canvas.drawText(currentString, drawMarqueTextStart, baseline, textPaint);
                }
                canvas.drawText(currentString,drawTextStart,baseline,textPaint);
            }
        }else {
            cancelScroll();
            textPaint.setTextAlign(Paint.Align.CENTER);
            drawTextStart = textRect.left;
            canvas.drawText(currentString,textRect.centerX(),baseline,textPaint);
        }
        // 还原混合模式
        textPaint.setXfermode(null);
        // 还原画布
        canvas.restoreToCount(sc);
    }

    //smartLoadingView 开启动画
    public void start() {
        //没有在loading的情况下才能点击（没有在请求网络的情况下）
        if (!isLoading) {
            cancelScroll();
            startDrawOk = false;
            currentString = normalString;
            this.setClickable(false);
            paint.setColor(normal_color);
            isLoading = true;
//            animatorSet.start();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*if (circlBigView == null) {
                return;
            }*/
            switch (msg.what) {
                case 11:
                    break;
                case 12:
                    break;
                case 13:
                    break;
                case 14:
                    if (animator_text_scroll != null) {
                        animator_text_scroll.cancel();
                        animator_text_scroll = null;
                        postInvalidate();
                    }
                    break;
            }
        }
    };

    /**
     * 绘制对勾
     */
    private void initOk() {
        //对勾的路径
        path.moveTo(default_all_distance + height / 8 * 3, height / 2);
        path.lineTo(default_all_distance + height / 2, height / 5 * 3);
        path.lineTo(default_all_distance + height / 3 * 2, height / 5 * 2);
        pathMeasure = new PathMeasure(path, true);
    }

    /**
     * 初始化所有动画
     */
    private void initAnimation() {
        set_rect_to_circle_animation();
    }

    /**
     * 圆角矩形<---->圆
     * 矩形<---->圆角矩形
     */
    private void set_rect_to_circle_animation() {

    }

    private void cancelScroll(){
        if (animator_text_scroll != null) {
            animator_text_scroll.cancel();
            animator_text_scroll = null;
        }
        if (animator_marque != null) {
            animator_marque.cancel();
            animator_marque = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelScroll();
    }

}
