package com.example.sun.photoeditdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sun.photoeditdemo.utils.OperateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PHOTO_PICKED_WITH_DATA = 3021;

    private String photoPath = null, camera_path;

    int width = 0;
    private ImageView pictureShow;
    private LinearLayout content_layout;

    private OperateUtils operateUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureFromPhoto();
            }
        });

        findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_path == null) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("camera_path", camera_path);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_draw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_path == null) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                intent.putExtra("camera_path", camera_path);
                startActivity(intent);
            }
        });

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        pictureShow = (ImageView) findViewById(R.id.pictureShow);
        content_layout = (LinearLayout) findViewById(R.id.mainLayout);

        operateUtils = new OperateUtils(this);
    }

    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openPhotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openPhotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    @SuppressLint("HandlerLeak")
    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (content_layout.getWidth() != 0) {
                    Log.i("LinearLayoutW", content_layout.getWidth() + "");
                    Log.i("LinearLayoutH", content_layout.getHeight() + "");
                    // 取消定时器
                    timer.cancel();
                    compressed();
                }
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    private void compressed() {
        Bitmap resizeBmp = operateUtils.compressionFiller(photoPath, content_layout);
        pictureShow.setImageBitmap(resizeBmp);
        camera_path = SaveBitmap(resizeBmp, "saveTemp");
    }

    // 将生成的图片保存到内存中
    public String SaveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(Constants.filePath);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(Constants.filePath + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_PICKED_WITH_DATA && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            photoPath = c.getString(columnIndex);
            c.close();

            // 延迟每次延迟10 毫秒 隔1秒执行一次
            if (content_layout.getWidth() == 0) {
                timer.schedule(task, 10, 1000);
            } else {
                compressed();
            }
        }
    }
}
