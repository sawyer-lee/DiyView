package com.zhuozheng.sawyerdiyview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.widget.SmartLoadingView;

public class SmartLoadingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_loading_view);
        SmartLoadingView smartLoadingView = findViewById(R.id.smartLoadingView);
        smartLoadingView.start();
    }
}