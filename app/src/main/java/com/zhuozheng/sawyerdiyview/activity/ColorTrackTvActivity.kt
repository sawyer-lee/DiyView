package com.zhuozheng.sawyerdiyview.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhuozheng.sawyerdiyview.R
import com.zhuozheng.sawyerdiyview.databinding.ActivityColorTrackTvBinding
import com.zhuozheng.sawyerdiyview.databinding.ActivityMainBinding
import com.zhuozheng.sawyerdiyview.widget.ColorTrackTextView

class ColorTrackTvActivity : AppCompatActivity() {
    private lateinit var binding: ActivityColorTrackTvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColorTrackTvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnLeftToRight.setOnClickListener {
                setAnimation(ColorTrackTextView.Direction.LEFT_TO_RIGHT)
            }
            btnRightToLeft.setOnClickListener {
                setAnimation(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
            }
        }
    }

    fun setAnimation(direction: ColorTrackTextView.Direction?) {
        binding.colorTv.setDirection(direction)
        val valueAnimator = ObjectAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener { animation ->
            val currentProgress = animation.animatedValue as Float
            binding.colorTv.setCurrentProgress(currentProgress)
        }
        valueAnimator.start()
    }

}