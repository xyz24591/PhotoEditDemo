package com.example.sun.photoeditdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

/**
 * @author Sun
 * @data 2018/1/4
 * @desc
 */
public class ImageUtils {
    private static ImageUtils mImageUtils = null;

    private ImageUtils() {
    }

    public static ImageUtils getInstance() {
        if (mImageUtils == null) {
            mImageUtils = new ImageUtils();
        }
        return mImageUtils;
    }

    /**
     * 获取图片bitmap
     *
     * @param context
     * @param path
     * @return
     */
    public Bitmap loadImage(Context context, Object path) {
        Bitmap bitmap;
        try {
            bitmap = Glide.with(context)
                    .load(path)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
