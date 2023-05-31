package com.zhuozheng.sawyerdiyview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuozheng.sawyerdiyview.R;
import com.zhuozheng.sawyerdiyview.util.SnackbarUtils;
import com.zhuozheng.sawyerdiyview.widget.ArcSeekBar;

public class ArcSeekBarActivity extends AppCompatActivity {

    private TextView mProgressText;
    private ArcSeekBar mArcSeekBar;
    private ImageView imgMinus,imgPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_seek_bar);

        mProgressText = findViewById(R.id.txt_progress);
        mArcSeekBar = findViewById(R.id.arc_seek_bar);
        imgMinus = findViewById(R.id.img_minus);
        imgPlus = findViewById(R.id.img_plus);

        mArcSeekBar.setMinValue(0);
        mArcSeekBar.setMaxValue(100);

        setOpening(mArcSeekBar.getProgress());

        mArcSeekBar.setOnProgressChangeListener(new ArcSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(ArcSeekBar seekBar, int progress, boolean isUser) {
                setOpening(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(ArcSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(ArcSeekBar seekBar) {
                setOpening(seekBar.getProgress());
            }
        });

        imgMinus.setOnClickListener(v -> {
            int progress=mArcSeekBar.getProgress();
            if ((progress -1) < 0 ){
//                Toast.makeText(ArcSeekBarActivity.this,"已为最小值",Toast.LENGTH_SHORT).show();
                SnackbarUtils.Custom(mArcSeekBar,"已为最小值",2000)
                        .messageCenter()
                        .messageColor(Color.BLACK)
                        .backColor(Color.WHITE)
                        .show();
                return;
            }
            mArcSeekBar.setProgress(progress - 1);
            setOpening(progress - 1);
        });

        imgPlus.setOnClickListener(v -> {
            int progress=mArcSeekBar.getProgress();
            if ((progress +1) > 100 ){
//                Toast.makeText(ArcSeekBarActivity.this,"已为最大值",Toast.LENGTH_SHORT).show();
                SnackbarUtils.Custom(mArcSeekBar,"已为最大值",2000)
                        .messageCenter()
                        .messageColor(Color.BLACK)
                        .backColor(Color.WHITE)
                        .show();
                return;
            }
            mArcSeekBar.setProgress(progress + 1);
            setOpening(progress + 1);
        });
}

    @SuppressWarnings("setTextI18n")
    private void setOpening(int progress) {
        mProgressText.setText("开度：" + progress + " %");
    }
}