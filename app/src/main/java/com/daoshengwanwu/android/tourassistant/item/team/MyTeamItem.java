package com.daoshengwanwu.android.tourassistant.item.team;

/**
 * Created by LK on 2016/11/22.
 */
public class MyTeamItem {
    private int pic;
    private String name;

    public MyTeamItem(int pic, String name) {
        this.pic = pic;
        this.name = name;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
