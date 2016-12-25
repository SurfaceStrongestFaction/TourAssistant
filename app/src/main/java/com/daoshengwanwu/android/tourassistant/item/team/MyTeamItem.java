package com.daoshengwanwu.android.tourassistant.item.team;

import android.graphics.Bitmap;

/**
 * Created by LK on 2016/11/22.
 */
public class MyTeamItem {
    private String pic;
    private String name;

    public MyTeamItem(String pic, String name) {
        this.pic = pic;
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
