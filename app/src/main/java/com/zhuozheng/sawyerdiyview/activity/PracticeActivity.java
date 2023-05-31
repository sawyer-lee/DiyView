package com.zhuozheng.sawyerdiyview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.databinding.ActivityPracticeBinding;

@SuppressLint("SetTextI18n")
public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPracticeBinding binding = ActivityPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}