package com.zhuozheng.sawyerdiyview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.widget.Kawaii_LoadingView;

public class LoadingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_view);
        Kawaii_LoadingView  loadingView = findViewById(R.id.Kawaii_LoadingView);
        loadingView.startMoving();
    }
}