package com.daoshengwanwu.android.tourassistant.item.track;

/**
 * Created by dell on 2016/11/28.
 */

public class BaseItem {
    private int item_type = 0;
    public BaseItem(int item_type) {
        this.item_type = item_type;
    }
    public int getItem_type() {
        return item_type;
    }
    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }


}
