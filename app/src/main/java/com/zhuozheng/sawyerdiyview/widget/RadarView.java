package com.zhuozheng.sawyerdiyview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.zhuozheng.calorimeter.util.LogUtil;
import com.zhuozheng.sawyerdiyview.R;

public class RadarView extends View {

    private int count = 6;                //雷达边数
    private float angle = (float) (Math.PI*2/count);
    private String[] titles = {"a","b","c","d","e","f"};   //各维度标题
    private double[] data = {100,60,80,60,100,30};         //各维度分值
    private float maxValue = 100;           //数据最大值
    private float radius;                   //网格最大半径
    private int centerX;                    //中心X
    private int centerY;                    //中心Y
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔

    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        mainPaint = new Paint();
        textPaint = new Paint();
        valuePaint = new Paint();
        mainPaint.setStrokeWidth(2);
        textPaint.setStrokeWidth(2);
        mainPaint.setAntiAlias(true);
        valuePaint.setAntiAlias(true);
        textPaint.setTextSize(30f);
        mainPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStyle(Paint.Style.STROKE);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h,w) / 2f * 0.85f;
        centerX = w/2;
        centerY = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(centerX,centerY);
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /** 绘制蛛网 */
    private void drawPolygon(Canvas canvas){
        Path path = new Path();
        float r = radius/(count-1);     //r是蜘蛛丝之间的间距
        for (int i = 0; i < count; i++) {
            float currentR = r * i;     //当前半径
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0){
                    path.moveTo(currentR,0);//移动初始点至最右边的点
                }else {
                    //根据半径，计算六边形上每个点的坐标
                    float x = (float) (currentR * Math.cos(angle*j));
                    float y = (float) (currentR * Math.sin(angle*j));
                    path.lineTo(x,y);
//                    LogUtil.INSTANCE.i("x="+x+",y="+y);
                }
            }
            path.close();   //闭合路径
            canvas.drawPath(path,mainPaint);
        }
    }

    /** 绘制直线 */
    private void drawLines(Canvas canvas){
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            path.reset();
            path.moveTo(0,0);
            float x = (float) (radius * Math.cos(angle*i));
            float y = (float) (radius * Math.sin(angle*i));
            //每一次连接中心点与计算的边界点
            path.lineTo(x,y);
            canvas.drawPath(path,mainPaint);
        }
    }

    /** 绘制文字
     * 对于文本的绘制，首先要找到末端的坐标，由于末端和文本有一定距离，给每个末端加上这个距离以后，再绘制文本。
     * 另外，当文本在左边时，由于不希望文本和蜘蛛网交叉，可以先计算出文本的长度，然后使起始绘制坐标向左偏移这个长度
     * */
    private void drawText(Canvas canvas){
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < count; i++) {
            float x = (float) ((radius+fontHeight/2)*Math.cos(angle*i));
            float y = (float) ((radius+fontHeight/2)*Math.sin(angle*i));
            float dis = textPaint.measureText(titles[i]);       //文本长度

            if (angle*i >= 0 && angle*i <= Math.PI/2){                  //第一象限
                canvas.drawText(titles[i],x,y+dis,textPaint);
            }else if (angle*i >= Math.PI/2 && angle*i < Math.PI){       //第二象限
                canvas.drawText(titles[i],x-dis,y+dis,textPaint);
            }else if (angle*i >= Math.PI && angle*i <= Math.PI*3/2){    //第三象限
                canvas.drawText(titles[i],x-dis,y,textPaint);
            }else if (angle*i >= Math.PI*3/2 && angle*i <= Math.PI*2){  //第四象限
                canvas.drawText(titles[i],x+dis,y,textPaint);
            }
        }
    }

    /** 绘制覆盖区域 */
    private void drawRegion(Canvas canvas){
        Path path = new Path();
        valuePaint.setColor(getResources().getColor(R.color.colorPrimary));
        for (int i = 0; i < count; i++) {
            double percent = data[i]/maxValue;
            float x = (float) (radius * Math.cos(angle*i) * percent);
            float y = (float) (radius * Math.sin(angle*i) * percent);
            if (i == 0){
                path.moveTo(x,0);
            }else {
                path.lineTo(x,y);
            }
            //绘制小圆点
            canvas.drawCircle(x,y,8f,valuePaint);
        }
        //绘制连线
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,valuePaint);
        //绘制区域
        valuePaint.setAlpha(127);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path,valuePaint);
    }
}
