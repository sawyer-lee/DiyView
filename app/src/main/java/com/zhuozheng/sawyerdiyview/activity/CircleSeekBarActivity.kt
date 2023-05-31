package com.zhuozheng.sawyerdiyview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhuozheng.sawyerdiyview.databinding.ActivityCircleSeekBarBinding

class CircleSeekBarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCircleSeekBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleSeekBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread{
            for (i in 1..100){
                binding.circleBar.setProgress(i)
                try {
                    Thread.sleep(100)
                }catch (e:InterruptedException){
                    e.printStackTrace()
                }
            }
        }.start()
    }

}