package com.zhuozheng.sawyerdiyview.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuozheng.sawyerdiyview.R

class BottomSheetFragment : BaseBottomSheetFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_acitons,container,false)
    }

}