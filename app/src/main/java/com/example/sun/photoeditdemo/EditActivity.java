package com.example.sun.photoeditdemo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sun.photoeditdemo.utils.OperateUtils;
import com.example.sun.photoeditdemo.utils.operate.ImageObject;
import com.example.sun.photoeditdemo.utils.operate.OperateView;
import com.example.sun.photoeditdemo.utils.operate.TextObject;
import com.example.sun.photoeditdemo.widget.DrawingView;
import com.example.sun.photoeditdemo.widget.MosaicView;

/**
 * @author Sun
 * @data 2018/1/4
 * @desc 图片编辑页面
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private String mPath;// 修改的图片路径
    /**
     * 根据源图片生成的Bitmap
     */
    private Bitmap mBitmap,
    /**
     * 修改后的Bitmap(一直更新)
     */
    mEditBitmap;

    private TextView tvTitle;

    /**
     * 涂鸦控件
     */
    private DrawingView drawView;

    /**
     * 添加表情、文字的控件
     */
    private TextView tvAddTags;
    private OperateView operateView;

    /**
     * 打马赛克的控件
     */
    private MosaicView mosaicView;

    /**
     * 内容布局
     */
    private ViewGroup centerLayout;
    /**
     * 底部编辑栏布局
     */
    private ViewGroup editLayout,
    /**
     * 具体编辑的操作栏布局
     */
    menuLayout,
    /**
     * 涂鸦工具栏布局
     */
    drawLayout,
    /**
     * 添加水印工具栏布局
     */
    tagsLayout;

    enum EditFlag {
        NONE("默认"), DRAW("涂鸦"), TEXT("文字"), BROW("表情"), MOSAIC("马赛克");

        public String tag;

        EditFlag(String tag) {
            this.tag = tag;
        }
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

        centerLayout = findViewById(R.id.center_layout);

        drawView = findViewById(R.id.draw_view);
        tvAddTags = findViewById(R.id.tv_add_tags);
        mosaicView = findViewById(R.id.mosaic_view);

        editLayout = findViewById(R.id.edit_layout);
        menuLayout = findViewById(R.id.menu_layout);
        drawLayout = findViewById(R.id.draw_layout);
        tagsLayout = findViewById(R.id.tags_layout);

        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_complete).setOnClickListener(this);

        findViewById(R.id.cha).setOnClickListener(this);
        findViewById(R.id.gou).setOnClickListener(this);
        findViewById(R.id.draw).setOnClickListener(this);
        findViewById(R.id.brow).setOnClickListener(this);
        findViewById(R.id.text).setOnClickListener(this);
        findViewById(R.id.mosaic).setOnClickListener(this);

        findViewById(R.id.white).setOnClickListener(colorClickListener);
        findViewById(R.id.black).setOnClickListener(colorClickListener);
        findViewById(R.id.red).setOnClickListener(colorClickListener);
        findViewById(R.id.yellow).setOnClickListener(colorClickListener);
        findViewById(R.id.green).setOnClickListener(colorClickListener);
        findViewById(R.id.blue).setOnClickListener(colorClickListener);
        findViewById(R.id.purple).setOnClickListener(colorClickListener);
        findViewById(R.id.pink).setOnClickListener(colorClickListener);
        findViewById(R.id.recall).setOnClickListener(colorClickListener);
        tvAddTags.setOnClickListener(tagsClickListener);
    }

    private void initData() {
        // 初始化修改源文件路径以及生成对应的Bitmap
        mPath = getIntent().getStringExtra("camera_path");
        mBitmap = new OperateUtils(this).compressionFiller(mPath, centerLayout);
        mEditBitmap = mBitmap;

        drawView.initializePen();
        drawView.setPenSize(18);
        drawView.setPenColor(getResources().getColor(R.color.white));
        drawView.loadImage(mBitmap);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drawView.getLayoutParams();
        lp.width = mBitmap.getWidth();
        lp.height = mBitmap.getHeight();
        drawView.requestLayout();

        operateView = new OperateView(this, mBitmap);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                mBitmap.getWidth(), mBitmap.getHeight());
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        operateView.setLayoutParams(layoutParams);
        centerLayout.addView(operateView);
        operateView.setMultiAdd(true); //设置此参数，可以添加多个文字
        operateView.setVisibility(View.GONE);

        mosaicView.setImageBitmap(mEditBitmap);
        mosaicView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gou:// 保存当前的修改
                if (currentFlag == EditFlag.DRAW) {
                    mEditBitmap = drawView.getImageBitmap();
                    drawView.updateDrawMode(false);
                    drawView.loadImage(mEditBitmap);
                    operateView.updateBitmap(mEditBitmap);
                } else if (currentFlag == EditFlag.TEXT) {
                    operateView.save();
                    mEditBitmap = new OperateUtils(this).getBitmapByView(operateView);
                    drawView.loadImage(mEditBitmap);
                    operateView.reset();
                    operateView.setVisibility(View.GONE);
                } else if (currentFlag == EditFlag.BROW) {
                    operateView.save();
                    mEditBitmap = new OperateUtils(this).getBitmapByView(operateView);
                    drawView.loadImage(mEditBitmap);
                    operateView.reset();
                    operateView.setVisibility(View.GONE);
                } else if (currentFlag == EditFlag.MOSAIC) {
                    mEditBitmap = new OperateUtils(this).convertViewToBitmap(mosaicView);
                    drawView.loadImage(mEditBitmap);
                    mosaicView.clear();
                    mosaicView.setVisibility(View.GONE);
                }
                reset();
                break;
            case R.id.cha:// 撤销当前的修改
                if (currentFlag == EditFlag.DRAW) {
                    drawView.reset();
                    drawView.updateDrawMode(false);
                } else if (currentFlag == EditFlag.TEXT) {
                    operateView.reset();
                    operateView.setVisibility(View.GONE);
                } else if (currentFlag == EditFlag.BROW) {
                    operateView.reset();
                    operateView.setVisibility(View.GONE);
                } else if (currentFlag == EditFlag.MOSAIC) {
                    mosaicView.clear();
                    mosaicView.setVisibility(View.GONE);
                }
                reset();
                break;
            case R.id.draw:// 进行涂鸦
                currentFlag = EditFlag.DRAW;
                tvTitle.setText("涂鸦");
                drawView.updateDrawMode(true);
                editLayout.setVisibility(View.GONE);
                drawLayout.setVisibility(View.VISIBLE);
                menuLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.brow:// 添加表情
                currentFlag = EditFlag.BROW;
                tvTitle.setText("表情");
                tvAddTags.setText("添加表情");
                operateView.updateBitmap(mEditBitmap);
                operateView.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                tagsLayout.setVisibility(View.VISIBLE);
                menuLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.text:// 添加文字
                currentFlag = EditFlag.TEXT;
                tvTitle.setText("文字");
                tvAddTags.setText("添加文字");
                operateView.updateBitmap(mEditBitmap);
                operateView.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                tagsLayout.setVisibility(View.VISIBLE);
                menuLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.mosaic:// 打马赛克
                currentFlag = EditFlag.MOSAIC;
                tvTitle.setText("马赛克");
                mosaicView.setImageBitmap(mEditBitmap);
                mosaicView.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                tagsLayout.setVisibility(View.VISIBLE);
                menuLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_cancel:// 退出
                finish();
                break;
            case R.id.tv_complete:// 到下一个页面查看修改后的图片
                String saveBitmap = new OperateUtils(this).saveBitmap(mEditBitmap);
                Intent intent = new Intent(EditActivity.this, PreviewActivity.class);
                intent.putExtra("path", saveBitmap);
                startActivity(intent);
                break;
        }
    }

    // UI界面还原
    private void reset() {
        tvTitle.setText("");
        currentFlag = EditFlag.NONE;
        drawLayout.setVisibility(View.GONE);
        tagsLayout.setVisibility(View.GONE);
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

    private View.OnClickListener tagsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_add_tags:
                    if (currentFlag == EditFlag.TEXT) {
                        final EditText editText = new EditText(EditActivity.this);
                        new AlertDialog.Builder(EditActivity.this).setView(editText)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    public void onClick(DialogInterface dialog, int which) {
                                        String string = editText.getText().toString();
                                        TextObject textObj = new OperateUtils(EditActivity.this).getTextObject(string, operateView, 5, 150, 100);
                                        if (textObj != null) {
                                            textObj.commit();
                                            operateView.addItem(textObj);
                                            operateView.setOnListener(new OperateView.MyListener() {
                                                public void onClick(TextObject tObject) {
                                                    alert(tObject);
                                                }
                                            });
                                        }
                                    }
                                }).show();
                    } else if (currentFlag == EditFlag.BROW) {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.comment);
                        ImageObject imgObject = new OperateUtils(EditActivity.this)
                                .getImageObject(bmp, operateView, 5, 150, 100);
                        operateView.addItem(imgObject);
                    }
                    break;
            }
        }
    };

    private void alert(final TextObject tObject) {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which) {
                        String string = editText.getText().toString();
                        tObject.setText(string);
                        tObject.commit();
                    }
                })
                .show();
    }
}
