package com.zhuozheng.sawyerdiyview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.widget.HalfCircle;

public class HalfCircleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_circle);
        HalfCircle halfCircle = findViewById(R.id.halfCircle);
    }
}