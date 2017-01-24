package com.daoshengwanwu.android.tourassistant.view;


import android.content.Context;
import android.widget.ImageView;


/**
 * 本类几乎和原生ImageView一样，不过有以下两点不同：
 * 1、ScaleType永远是fit_xy，任何方式修改该属性都无效
 * 2、宽度永远和高度相同，任何对宽度的指定都无效
 * 3、无法通过xml方式实例化，只可通过代码方式进行实例化
 */
public class StarImageView extends ImageView {
    public StarImageView(Context context) {
        super(context);
        super.setScaleType(ScaleType.FIT_XY);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
    }
}
