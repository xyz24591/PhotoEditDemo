package com.example.sun.photoeditdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.example.sun.photoeditdemo.utils.crop.CropImageType;
import com.example.sun.photoeditdemo.utils.crop.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Sun
 * @data 2018/1/12
 * @desc 裁剪页面
 */
public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private String mPath;
    private CropImageView cropView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_crop);

        mPath = getIntent().getStringExtra("path");
        Bitmap bit = BitmapFactory.decodeFile(mPath);
        cropView = (CropImageView) findViewById(R.id.cropmageView);

        Bitmap hh = BitmapFactory.decodeResource(this.getResources(), R.mipmap.crop_button);
        cropView.setCropOverlayCornerBitmap(hh);
        cropView.setImageBitmap(bit);
        cropView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// 触摸时显示网格
        cropView.setFixedAspectRatio(false);// 自由剪切

        findViewById(R.id.cha).setOnClickListener(this);
        findViewById(R.id.gou).setOnClickListener(this);
        findViewById(R.id.custom_crop).setOnClickListener(this);
        findViewById(R.id.action_1_1).setOnClickListener(this);
        findViewById(R.id.action_3_2).setOnClickListener(this);
        findViewById(R.id.action_4_3).setOnClickListener(this);
        findViewById(R.id.action_16_9).setOnClickListener(this);
        findViewById(R.id.action_rotate).setOnClickListener(this);
        findViewById(R.id.action_up_down).setOnClickListener(this);
        findViewById(R.id.action_left_right).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cha:
                Intent cancelData = new Intent();
                setResult(RESULT_CANCELED, cancelData);
                finish();
                break;
            case R.id.gou:
                Bitmap bit = cropView.getCroppedImage();
                writeImage(bit, mPath, 100);

                Intent okData = new Intent();
                okData.putExtra("path", mPath);
                setResult(RESULT_OK, okData);
                this.finish();
                break;
            case R.id.custom_crop:
                cropView.setFixedAspectRatio(false);
                break;
            case R.id.action_1_1:
                cropView.setFixedAspectRatio(true);
                cropView.setAspectRatio(10, 10);
                break;
            case R.id.action_3_2:
                cropView.setFixedAspectRatio(true);
                cropView.setAspectRatio(30, 20);
                break;

            case R.id.action_4_3:
                cropView.setFixedAspectRatio(true);
                cropView.setAspectRatio(40, 30);
                break;
            case R.id.action_16_9:
                cropView.setFixedAspectRatio(true);
                cropView.setAspectRatio(160, 90);
                break;
            case R.id.action_rotate:
                cropView.rotateImage(90);
                break;
            case R.id.action_up_down:
                cropView.reverseImage(CropImageType.REVERSE_TYPE.UP_DOWN);
                break;
            case R.id.action_left_right:
                cropView.reverseImage(CropImageType.REVERSE_TYPE.LEFT_RIGHT);
                break;
        }
    }

    /**
     * @param bitmap
     * @param destPath
     * @param quality
     */
    public void writeImage(Bitmap bitmap, String destPath, int quality) {
        try {
            deleteFile(destPath);
            if (createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
