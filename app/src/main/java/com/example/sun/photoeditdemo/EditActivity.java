package com.example.sun.photoeditdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private Bitmap mBitmap, mEditBitmap;

    private TextView tvTitle, tvCancel, tvComplete;

    private ImageView ivContent;
    private PaletteView drawView;

    private ViewGroup centerLayout;
    private ViewGroup editLayout, menuLayout, drawLayout;

    enum EditFlag {
        NONE, DRAW,
    }

    private EditFlag currentFlag = EditFlag.NONE;

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
        tvTitle = findViewById(R.id.tv_title);
        tvCancel = findViewById(R.id.tv_cancel);
        tvComplete = findViewById(R.id.tv_complete);

        centerLayout = findViewById(R.id.center_layout);

        ivContent = findViewById(R.id.iv_content);
        drawView = findViewById(R.id.draw_view);

        editLayout = findViewById(R.id.edit_layout);
        menuLayout = findViewById(R.id.menu_layout);
        drawLayout = findViewById(R.id.draw_layout);

        drawView.setCallback(new PaletteView.Callback() {
            @Override
            public void onUndoRedoStatusChanged() {
                ((TextView) findViewById(R.id.recall)).setTextColor(drawView.canUndo() ?
                        getResources().getColor(R.color.white) : getResources().getColor(R.color.gray));
            }
        });

        tvCancel.setOnClickListener(this);
        tvComplete.setOnClickListener(this);

        findViewById(R.id.cha).setOnClickListener(this);
        findViewById(R.id.gou).setOnClickListener(this);
        findViewById(R.id.draw).setOnClickListener(this);

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
            case R.id.gou:
                if (currentFlag == EditFlag.DRAW) {
                    mEditBitmap = new OperateUtils(this).convertViewToBitmap(centerLayout);
                    ivContent.setImageBitmap(mEditBitmap);
                    drawView.reset();
                    drawView.setDrawable(false);
                }
                reset();
                break;
            case R.id.cha:
                if (currentFlag == EditFlag.DRAW) {
                    drawView.clear();
                    drawView.setDrawable(false);
                }
                reset();
                break;
            case R.id.draw:
                currentFlag = EditFlag.DRAW;
                tvTitle.setText("涂鸦");
                drawView.setDrawable(true);
                editLayout.setVisibility(View.GONE);
                menuLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void reset() {
        tvTitle.setText("");
        currentFlag = EditFlag.NONE;
        menuLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
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
