package com.daoshengwanwu.android.tourassistant.item.team;

import android.graphics.Bitmap;

/**
 * Created by LK on 2016/11/22.
 */
public class MyTeamItem {
    private Bitmap pic;
    private String name;

    public MyTeamItem(Bitmap pic, String name) {
        this.pic = pic;
        this.name = name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
