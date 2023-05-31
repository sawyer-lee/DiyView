package com.zhuozheng.sawyerdiyview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ThreeBezierView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs, 0) {

    init {
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint.apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = 8f
            textSize = 60f
        }
        start = PointF(0f, 0f)
        end = PointF(0f, 0f)
        control1 = PointF(0f, 0f)
        control2 = PointF(0f, 0f)
    }

    private lateinit var mPaint : Paint
    private lateinit var start: PointF
    private lateinit var end: PointF
    private lateinit var control1: PointF
    private lateinit var control2: PointF
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var mode = true

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w / 2f
        centerY = h / 2f
        //初始化数据点和控制点的位置
        start.x = centerX - 200f
        start.y = centerY
        end.x = centerX + 200f
        end.y = centerY
        control1.x = centerX
        control2.x = centerX
        control1.y = centerY - 100f
        control2.y = centerY - 100f
    }

    fun setMode(mode: Boolean){
        this.mode = mode
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 根据触摸位置更新控制点，并提示重绘
        if (mode){
            control1.x = event.x
            control1.y = event.y
        }else{
            control2.x = event.x
            control2.y = event.y
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        //绘制数据点和控制点
        mPaint.color = Color.GRAY
        mPaint.strokeWidth = 20f
        canvas.drawPoint(start.x, start.y, mPaint)
        canvas.drawPoint(end.x, end.y, mPaint)
        canvas.drawPoint(control1.x, control1.y, mPaint)
        canvas.drawPoint(control2.x, control2.y, mPaint)
        //绘制辅助线
        mPaint.strokeWidth = 4f
        canvas.drawLine(start.x, start.y, control1.x, control1.y, mPaint)
        canvas.drawLine(control1.x, control1.y, control2.x, control2.y, mPaint)
        canvas.drawLine(end.x, end.y, control2.x, control2.y, mPaint)
        //绘制Bezier曲线
        mPaint.color = Color.RED
        mPaint.strokeWidth = 8f
        mPaint.style = Paint.Style.STROKE
        val path = Path()
        path.moveTo(start.x, start.y)
        path.cubicTo(control1.x, control1.y,control2.x, control2.y, end.x, end.y)
        canvas.drawPath(path, mPaint)
    }

}

