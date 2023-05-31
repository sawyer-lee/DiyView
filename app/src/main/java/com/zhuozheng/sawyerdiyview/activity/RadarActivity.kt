package com.zhuozheng.sawyerdiyview.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sawyer.kotlindemo.ext.showToast
import com.zhuozheng.sawyerdiyview.R
import com.zhuozheng.sawyerdiyview.databinding.ActivityRadarBinding
import com.zhuozheng.sawyerdiyview.widget.BottomSheetFragment

class RadarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRadarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}