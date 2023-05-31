package com.zhuozheng.sawyerdiyview.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class TestLifeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("lee","onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i("lee","onSaveInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.i("lee","onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.i("lee","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("lee","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("lee","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("lee","onDestroy")
    }
}