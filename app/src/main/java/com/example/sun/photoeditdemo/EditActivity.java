package com.example.sun.photoeditdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.photoeditdemo.utils.OperateUtils;
import com.example.sun.photoeditdemo.widget.PaletteView;

/**
 * @author Sun
 * @data 2018/1/4
 * @desc 图片编辑页面
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private String mPath;
    private Bitmap mBitmap;

    private ImageView ivContent;
    private PaletteView drawView;

    private ViewGroup colorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit);

        initView();
        initData();
    }

    private void initView() {
        ivContent = findViewById(R.id.iv_content);
        drawView = findViewById(R.id.draw_view);

        colorLayout = findViewById(R.id.color_layout);

        drawView.setCallback(new PaletteView.Callback() {
            @Override
            public void onUndoRedoStatusChanged() {
                ((TextView) findViewById(R.id.recall)).setTextColor(drawView.canUndo() ?
                        getResources().getColor(R.color.white) : getResources().getColor(R.color.gray));
            }
        });
        findViewById(R.id.draw).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.complete).setOnClickListener(this);
        findViewById(R.id.white).setOnClickListener(colorClickListener);
        findViewById(R.id.black).setOnClickListener(colorClickListener);
        findViewById(R.id.red).setOnClickListener(colorClickListener);
        findViewById(R.id.yellow).setOnClickListener(colorClickListener);
        findViewById(R.id.green).setOnClickListener(colorClickListener);
        findViewById(R.id.blue).setOnClickListener(colorClickListener);
        findViewById(R.id.purple).setOnClickListener(colorClickListener);
        findViewById(R.id.pink).setOnClickListener(colorClickListener);
        findViewById(R.id.recall).setOnClickListener(colorClickListener);
    }

    private void initData() {
        mPath = getIntent().getStringExtra("camera_path");
        mBitmap = new OperateUtils(this).compressionFiller(mPath, ivContent);

        ivContent.setImageBitmap(mBitmap);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drawView.getLayoutParams();
        lp.width = mBitmap.getWidth();
        lp.height = mBitmap.getHeight();
        drawView.requestLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draw:
                if (colorLayout.getVisibility() == View.VISIBLE) {
                    drawView.setDrawable(false);
                    drawView.setVisibility(View.GONE);
                    colorLayout.setVisibility(View.GONE);
                } else {
                    drawView.setDrawable(true);
                    drawView.setVisibility(View.VISIBLE);
                    colorLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private View.OnClickListener colorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.white:
                    drawView.setPenColor(getResources().getColor(R.color.white));
                    break;
                case R.id.black:
                    drawView.setPenColor(getResources().getColor(R.color.black));
                    break;
                case R.id.red:
                    drawView.setPenColor(getResources().getColor(R.color.red));
                    break;
                case R.id.yellow:
                    drawView.setPenColor(getResources().getColor(R.color.yellow));
                    break;
                case R.id.green:
                    drawView.setPenColor(getResources().getColor(R.color.green));
                    break;
                case R.id.blue:
                    drawView.setPenColor(getResources().getColor(R.color.blue));
                    break;
                case R.id.purple:
                    drawView.setPenColor(getResources().getColor(R.color.purple));
                    break;
                case R.id.pink:
                    drawView.setPenColor(getResources().getColor(R.color.pink));
                    break;
                case R.id.recall:
                    drawView.undo();
                    break;
            }
        }
    };
}
