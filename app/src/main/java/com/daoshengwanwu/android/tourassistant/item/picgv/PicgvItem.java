package com.daoshengwanwu.android.tourassistant.item.picgv;

import android.graphics.drawable.Drawable;

/**
 * Created by dell on 2016/11/24.
 */

public class PicgvItem {
    private int id;
    private Drawable imgsrc;
    public PicgvItem(int id, Drawable imgsrc) {
        this.id = id;
        this.imgsrc = imgsrc;
    }

    public int getId() {
        return id;
    }

    public Drawable getImgsrc() {
        return imgsrc;
    }
}
