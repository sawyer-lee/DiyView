package com.zhuozheng.sawyerdiyview.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import com.zhuozheng.sawyerdiyview.App
import com.zhuozheng.sawyerdiyview.R

fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(App.instance, this, duration).show()
}

fun String.showLongToast(duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(App.instance, this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(App.instance, this, duration).show()
}

class Tips(mContext: Context) {

    companion object{
        private const val TOP = Gravity.TOP
        private const val BOTTOM = Gravity.BOTTOM
        private const val CENTER = Gravity.CENTER
        private const val DEFAULT = -1
    }

    private var tips : Tips = Tips(mContext)
    private var toast: Toast = Toast(mContext)
    private var toastLayout: View = LayoutInflater.from(App.instance).inflate(R.layout.toast_view, null)
    private var tv:TextView
    private var iv:ImageView
    private var llToast:LinearLayout
    private var position = DEFAULT

    init {
        tv = toastLayout.findViewById(R.id.toastTv)
        iv = toastLayout.findViewById(R.id.toastImg)
        llToast = toastLayout.findViewById(R.id.llToast)
        toast.view = toastLayout
    }

    private fun setPosition(position: Int) : Tips{
        this.position = position
        return tips
    }




}