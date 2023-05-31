package com.zhuozheng.sawyerdiyview.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhuozheng.sawyerdiyview.R
import com.zhuozheng.sawyerdiyview.databinding.ActivityThreeBezierBinding

class ThreeBezierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThreeBezierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreeBezierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId){
                    R.id.controlOne -> bezierView.setMode(true)
                    R.id.controlTwo -> bezierView.setMode(false)
                }
            }
        }
    }
}