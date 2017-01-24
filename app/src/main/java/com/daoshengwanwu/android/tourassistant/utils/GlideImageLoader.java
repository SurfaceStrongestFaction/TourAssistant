package com.daoshengwanwu.android.tourassistant.utils;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    private static GlideImageLoader sGlideImageLoader = null;


    public static GlideImageLoader getInstance() {
        if (null == sGlideImageLoader) {
            sGlideImageLoader = new GlideImageLoader();
        }

        return sGlideImageLoader;
    }

    private GlideImageLoader() {}

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
