package com.daoshengwanwu.android.tourassistant.item.track;

/**
 * Created by dell on 2016/11/28.
 */

public class item1 extends BaseItem{
    private int imageId;
    private String nameValue;
    private String finishValue;
    private String suipianValue;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public String getFinishValue() {
        return finishValue;
    }

    public void setFinishValue(String finishValue) {
        this.finishValue = finishValue;
    }

    public String getSuipianValue() {
        return suipianValue;
    }

    public void setSuipianValue(String suipianValue) {
        this.suipianValue = suipianValue;
    }

    public item1(int item_type, int id,String name, String finish, String suipian) {
        super(item_type);
        this.imageId = id;
        this.nameValue = name;
        this.finishValue = finish;
        this.suipianValue = suipian;

    }
}
