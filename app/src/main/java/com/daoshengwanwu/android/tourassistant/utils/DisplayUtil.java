package com.daoshengwanwu.android.tourassistant.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.daoshengwanwu.android.tourassistant.R;


public class DisplayUtil {
    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getTextHeight(Context context, float textSpSize) {
        Paint paint = new Paint();
        paint.setTextSize(sp2px(context, textSpSize));
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float lineHeight = metrics.descent - metrics.top; //文字高度

        return lineHeight;
    }

    public static int getScreenWidth(Activity activity) {
        if (-1 == sScreenWidth) {
            WindowManager winMan = activity.getWindowManager();
            Point size = new Point();
            winMan.getDefaultDisplay().getRealSize(size);
            sScreenWidth = size.x;
            sScreenHeight = size.y;
        }

        return sScreenWidth;
    }

    public static int getScreenHeight(Activity activity) {
        if (-1 == sScreenHeight) {
            WindowManager winMan = activity.getWindowManager();
            Point size = new Point();
            winMan.getDefaultDisplay().getRealSize(size);
            sScreenWidth = size.x;
            sScreenHeight = size.y;
        }

        return sScreenHeight;
    }
}
