package com.daoshengwanwu.android.tourassistant.caizihuan.track;

/**
 * Created by dell on 2016/11/28.
 */

public class item0 extends BaseItem {
    private String dateValue;
    public item0(int item_type, String date) {
        super(item_type);
        this.dateValue = date;
    }

    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }
}
