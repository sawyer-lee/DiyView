package com.zhuozheng.sawyerdiyview.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhuozheng.sawyerdiyview.databinding.ActivityBezierBinding

class BezierActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBezierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBezierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            tvVerticalSeekBar.setOnClickListener {
                startActivity(Intent(this@BezierActivity, VerticalSeekBarActivity::class.java))
            }
            tvArcSeekBar.setOnClickListener {
                startActivity(Intent(this@BezierActivity,ArcSeekBarActivity::class.java))
            }
            tvTwoRank.setOnClickListener {
                startActivity(Intent(this@BezierActivity,TwoBezierActivity::class.java))
            }
            tvThreeRank.setOnClickListener {
                startActivity(Intent(this@BezierActivity,ThreeBezierActivity::class.java))
            }
            tvHeartBezier.setOnClickListener {
                startActivity(Intent(this@BezierActivity,HeartBezierActivity::class.java))
            }
        }
    }
}