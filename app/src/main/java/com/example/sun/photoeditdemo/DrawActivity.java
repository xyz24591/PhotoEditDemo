package com.example.sun.photoeditdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.sun.photoeditdemo.draw.PhotoView;

/**
 * @author Sun
 * @data 2018/1/29
 * @desc
 */
public class DrawActivity extends AppCompatActivity {
    PhotoView drawView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = findViewById(R.id.drawView);
        String path = getIntent().getStringExtra("camera_path");

        Glide.with(this).load(path).into(drawView);
        drawView.enable();
        drawView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
