package com.sawyer.kotlindemo.ext

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.zhuozheng.sawyerdiyview.App

/*fun Context.showToast(message: CharSequence){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}*/

fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
//    Toast.makeText(App.instance, this, duration).show()
    Tip.show(this,duration)
}

fun String.showLongToast(duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(App.instance, this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(App.instance, this, duration).show()
}

object Tip {

     fun show(message: String?, duration: Int) {
        val toast = Toast(App.instance)
        toast.duration = duration
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.view = createTextToastView(message)
        toast.show()
    }

    /**
     * 创建自定义 Toast View
     */
    private fun createTextToastView(message: String?): View {
        // 画圆角矩形背景
        val rc = dp2px(6f).toFloat()
        val shape = RoundRectShape(floatArrayOf(rc, rc, rc, rc, rc, rc, rc, rc), null, null)
        val drawable = ShapeDrawable(shape)
        drawable.paint.color = Color.argb(225, 240, 240, 240)
        drawable.paint.style = Paint.Style.FILL
        drawable.paint.isAntiAlias = true
        drawable.paint.flags = Paint.ANTI_ALIAS_FLAG

        // 创建View
        val layout = FrameLayout(App.instance)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.layoutParams = layoutParams
        layout.setPadding(dp2px(16f), dp2px(12f), dp2px(16f), dp2px(12f))
        layout.background = drawable
        val textView = TextView(App.instance)
        textView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        textView.textSize = 15f
        textView.text = message
        textView.setLineSpacing(dp2px(4f).toFloat(), 1f)
        textView.setTextColor(Color.BLACK)
        layout.addView(textView)
        return layout
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = App.instance.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}