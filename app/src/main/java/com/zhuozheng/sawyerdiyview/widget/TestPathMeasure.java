package com.zhuozheng.sawyerdiyview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.zhuozheng.sawyerdiyview.R;

public class TestPathMeasure extends View {

    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    private Paint mPaint;
    private Path path;
    private int mWidth,mHeight;

    public TestPathMeasure(Context context) { this(context,null); }

    public TestPathMeasure(Context context, @Nullable AttributeSet attrs) { this(context, attrs,0); }

    public TestPathMeasure(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        path = new Path();
        initPaint();
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;  //缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.arrow_circular,options);
        mMatrix = new Matrix();    //矩阵,用于对图片进行一些操作
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth/2f,mHeight/2f);             //移动坐标系至屏幕中心
        path.addCircle(0,0,200, Path.Direction.CW);     //添加一个圆
        PathMeasure pathMeasure = new PathMeasure(path,false);  //创建PathMeasure
        currentValue += 0.005;                                        //当前位置在总长度的比例【0-1】
        if (currentValue >= 1) currentValue = 0;
        pathMeasure.getPosTan(pathMeasure.getLength()*currentValue,pos,tan);    //获取当前位置的坐标以及趋势
        mMatrix.reset();                                            //重置Matrix
        float degree = (float) (Math.atan2(tan[1],tan[0])*180 /Math.PI);    //计算图片旋转角度
        mMatrix.postRotate(degree,mBitmap.getWidth() / 2f, mBitmap.getHeight() / 2f);      //旋转图片
        mMatrix.postTranslate(pos[0]-mBitmap.getWidth()/2f,pos[1]-mBitmap.getHeight()/2f); //将图片绘制中心调整到与当前点重合

        canvas.drawPath(path,mPaint);
        canvas.drawBitmap(mBitmap,mMatrix,mPaint);
        //页面刷新此处是在 onDraw 里面调用了 invalidate 方法来保持界面不断刷新，
        // 但并不提倡这么做，正确对做法应该是使用 线程 或者 ValueAnimator 来控制界面的刷新
        invalidate();
    }
}
