package com.zhuozheng.sawyerdiyview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.widget.BezierSeekBar;

public class VerticalSeekBarActivity extends AppCompatActivity {

    private BezierSeekBar seekBar;
    private int selectedValue;
    private int minValue,maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_seek_bar);
        ImageView imgMinus=findViewById(R.id.img_minus);
        ImageView imgPlus=findViewById(R.id.img_plus);
        seekBar=findViewById(R.id.seekBar);
        minValue=seekBar.getValueMin();
        maxValue=seekBar.getValueMax();

        imgMinus.setOnClickListener(v -> {
            selectedValue=seekBar.getValueSelected();
            if ((selectedValue - 1) < minValue ){
                Toast.makeText(VerticalSeekBarActivity.this,"已为最小值",Toast.LENGTH_LONG).show();
                return;
            }
            seekBar.setValueSelected(selectedValue - 1);
        });

        imgPlus.setOnClickListener(v -> {
            selectedValue=seekBar.getValueSelected();
            if ((selectedValue + 1) > maxValue ){
                Toast.makeText(VerticalSeekBarActivity.this,"已为最大值",Toast.LENGTH_LONG).show();
                return;
            }
            seekBar.setValueSelected(selectedValue + 1);
        });

    }
}