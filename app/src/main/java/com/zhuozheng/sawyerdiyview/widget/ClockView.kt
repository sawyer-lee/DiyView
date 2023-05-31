package com.zhuozheng.sawyerdiyview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.customview.widget.ViewDragHelper
import java.util.*
import kotlin.math.min

class ClockView (
        context:Context,
        attrs:AttributeSet?=null,
        defStyleAttr: Int = 0
) :View(context,attrs, defStyleAttr) {


    private val mPaint:Paint by lazy { Paint() }
    private val mRect:Rect by lazy { Rect() }

    private var mWidth= 0           // View宽度
    private var mHeight= 0          // View高度
    private var radius= 30F        // 圆半径，默认300像素
    private val mCircleWidth= 4F    // 表盘宽度
    private val scaleMax = 50       // 表盘整点刻度尺寸
    private val scaleMin = 25       // 表盘非整点刻度尺寸
    private val mNumberSpace = 10F  // 刻度与数字间距

    private val mPointRange = 20F           // 指针矩形弧度
    private val mHourPointWidth = 15F       // 时针宽度
    private val mMinutePointWidth = 10F     // 分针宽度
    private val mSecondPointWidth = 4F      // 秒针宽度

    init {
        mPaint.textSize = 35f
        mPaint.typeface = Typeface.DEFAULT_BOLD //时钟数字加粗
        mPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = onMeasuredSpec(widthMeasureSpec) + (mCircleWidth*2).toInt()
        mHeight = onMeasuredSpec(heightMeasureSpec) + (mCircleWidth*2).toInt()
        radius = (mWidth - mCircleWidth * 2) / 2
        setMeasuredDimension(mWidth,mHeight)
    }

    private fun onMeasuredSpec(measureSpec: Int): Int {
        var specViewSize = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when(specMode){
            MeasureSpec.EXACTLY -> specViewSize = specSize
            MeasureSpec.AT_MOST -> specViewSize = min((radius*2).toInt() , specSize)
            MeasureSpec.UNSPECIFIED -> {}
        }
        return specViewSize
    }

    override fun onDraw(canvas: Canvas) {
        //设置圆心坐标为屏幕中心点
        val centerX = (mWidth / 2).toFloat()
        val centerY = (mHeight / 2).toFloat()
        canvas.translate(centerX,centerY)

        //1.绘制表外圆
        drawCircle(canvas)
        //2.绘制刻度
        drawLine(canvas)
        //3.绘制指针
        drawPointer(canvas)

        //间隔1秒刷新，重绘，形成转动效果
        postInvalidateDelayed(1000)
    }

    private fun drawCircle(canvas: Canvas) {
        mPaint.strokeWidth = mCircleWidth
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        canvas.drawCircle(0F,0F,radius,mPaint)
    }

    //表盘共有60个刻度：12长 + 48短
    private fun drawLine(canvas: Canvas) {
        for(index in 1..60){
            //以12点钟为基点，每次将表盘旋转6度
            canvas.rotate(6F)
            //5的整数倍为长刻度
            if (index % 5 == 0){
                //设置长刻度画笔宽度
                mPaint.strokeWidth = 4F
                canvas.drawLine(0F,-radius,0F,-radius+scaleMax,mPaint)

                /** 绘制文本 **/
                canvas.save()
                //设置画笔
                mPaint.strokeWidth = 1F
                mPaint.style = Paint.Style.FILL
                //获取绘制文本的边框，即文字构成的矩形区域
                mPaint.getTextBounds((index/5).toString(), 0, (index/5).toString().length, mRect)
                //绘制文本的中心点坐标
                canvas.translate(0F,-radius+mNumberSpace+scaleMax+(mRect.height()/2))
                //摆正文字旋转角度
                canvas.rotate(index * -6F)
                canvas.drawText((index/5).toString(),-mRect.width().toFloat()/2,mRect.height().toFloat()/2,mPaint)
                canvas.restore()
            }else{
                //设置短刻度画笔宽度
                mPaint.strokeWidth = 2F
                canvas.drawLine(0F,-radius,0F,-radius+scaleMin,mPaint)
            }
        }
    }

    private fun drawPointer(canvas: Canvas) {
        //获取当前的时间
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        //计算指针旋转角度
        val angleHour = (hour + minute.toFloat() / 60) * 360 / 12
        val angleMinute = (minute + second.toFloat() / 60) *6
        val angleSecond = second.toFloat() * 6

        /**绘制时针*/
        //保存当前状态
        canvas.save()
        canvas.rotate(angleHour)
        //修改画笔样式
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = mHourPointWidth
        val rectHour = RectF(-mHourPointWidth / 2, -radius / 2, mHourPointWidth / 2, radius / 6)
        canvas.drawRoundRect(rectHour,mPointRange,mPointRange,mPaint)
        //回滚到上一次保存的状态
        canvas.restore()

        /**绘制分针*/
        canvas.save()
        canvas.rotate(angleMinute)
        mPaint.color = Color.BLUE
        mPaint.strokeWidth = mMinutePointWidth
        val rectMinute = RectF(-mMinutePointWidth / 2, -radius * 3.5f / 5, mMinutePointWidth / 2, radius / 6)
        canvas.drawRoundRect(rectMinute,mPointRange,mPointRange,mPaint)
        canvas.restore()

        /**绘制秒针*/
        canvas.save()
        canvas.rotate(angleSecond)
        mPaint.color = Color.RED
        mPaint.strokeWidth = mSecondPointWidth
        val rectSecond = RectF(-mSecondPointWidth / 2, -radius + 10, mSecondPointWidth / 2, radius / 6)
        canvas.drawRoundRect(rectSecond,mPointRange,mPointRange,mPaint)
        canvas.restore()

        /**绘制表盘原点*/
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(0F,0F,mSecondPointWidth*4,mPaint)
    }
}