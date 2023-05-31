package com.zhuozheng.sawyerdiyview.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.zhuozheng.sawyerdiyview.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val tvSecond = findViewById<TextView>(R.id.tvSecond)
        tvSecond.setOnClickListener {
            val intent = Intent(this,FirstActivity::class.java)
            intent.putExtra("data","data form SecondActivity")
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}