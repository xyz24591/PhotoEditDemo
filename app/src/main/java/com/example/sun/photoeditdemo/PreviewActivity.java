package com.example.sun.photoeditdemo;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.view.WindowManager.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

/**
 * @author Sun
 * @data 2018/1/11
 * @desc
 */
public class PreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);

        ImageView ivContent = findViewById(R.id.iv_content);
        String path = getIntent().getStringExtra("path");
        Glide.with(this)
                .load(path)
                .signature(new StringSignature("version" + SystemClock.currentThreadTimeMillis()))
                .into(ivContent);
    }
}
