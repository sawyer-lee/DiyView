package com.zhuozheng.sawyerdiyview.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.zhuozheng.calorimeter.util.LogUtil
import com.zhuozheng.sawyerdiyview.R
import kotlin.Int as KotlinInt

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val tvJump = findViewById<TextView>(R.id.tvJump)
        tvJump.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            requestDataLauncher.launch(intent)
        }
    }

    private val requestDataLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val data = it.data?.getStringExtra("data")
            LogUtil.i("$data")
        }
    }
}