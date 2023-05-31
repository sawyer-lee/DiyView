package com.zhuozheng.sawyerdiyview.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sawyer.kotlindemo.ext.showToast
import com.zhuozheng.sawyerdiyview.R
import com.zhuozheng.sawyerdiyview.databinding.ActivityControlMenuBinding
import com.zhuozheng.sawyerdiyview.widget.RemoteControlMenu

class ControlMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityControlMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*binding.rcm.setListener(object : RemoteControlMenu.MenuListener{
            override fun onCenterCliched() {
                "点击了中间".showToast()
            }

            override fun onUpCliched() {
                "点击了上边".showToast()
            }

            override fun onRightCliched() {
                "点击了右边".showToast()
            }

            override fun onDownCliched() {
                "点击了下边".showToast()
            }

            override fun onLeftCliched() {
                "点击了左边".showToast()
            }
        })*/
    }
}