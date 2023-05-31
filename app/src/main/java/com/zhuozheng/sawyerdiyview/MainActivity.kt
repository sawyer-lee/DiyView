package com.zhuozheng.sawyerdiyview

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhuozheng.sawyerdiyview.activity.*
import com.zhuozheng.sawyerdiyview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mData= mutableListOf<HomeBean>()
    private lateinit var mAdapter: HomeAdapter 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("lee","a_onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        mAdapter = HomeAdapter().also {
            binding.recycler.adapter = it
            it.setOnItemClickListener { _, _, position ->
                startActivity(Intent(this@MainActivity , mData[position].clazz))
            }
        }
        mAdapter.setList(mData)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i("lee","a_onSaveInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.i("lee","a_onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.i("lee","a_onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("lee","a_onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("lee","a_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("lee","a_onDestroy")
    }

    private fun initData() {
        mData.add(HomeBean(resources.getString(R.string.btn0), PracticeActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn1), BezierActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn3), SmartLoadingViewActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn4), SearchActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn5),LoadingViewActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn6),HalfCircleActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn7),PathMeasureActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn8),ClockActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn9),CircleSeekBarActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn10),AnimLogoViewActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn11),SinWaterActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn12),RadarActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn13),ControlMenuActivity::class.java))
        mData.add(HomeBean(resources.getString(R.string.btn14),FallingBallActivity::class.java))
        mData.add(HomeBean("Activity Result API",FirstActivity::class.java))
        mData.add(HomeBean("Test Lifecycle",TestLifeActivity::class.java))
    }

    inner class HomeAdapter(layoutRes: Int = R.layout.item_home)
        : BaseQuickAdapter<HomeBean,BaseViewHolder>(layoutRes){

        override fun convert(holder: BaseViewHolder, item: HomeBean) {
            holder.itemView.findViewById<TextView>(R.id.tvItem).text = item.txt
        }

    }

}