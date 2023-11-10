package com.zhuozheng.sawyerdiyview.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.zhuozheng.sawyerdiyview.R
import com.zhuozheng.sawyerdiyview.databinding.ActivityVideoViewBinding

class VideoViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.apply {
            //设置播放视频路径
            videoView.setVideoURI(
                Uri.parse("android.resource://$packageName/${R.raw.rain}")
            )
            //播放
            videoView.start()
            //循环播放
            videoView.setOnCompletionListener {
                videoView.start()
            }
        }
    }
}