package com.daoshengwanwu.android.tourassistant.wangxiao;

/**
 * Created by HP on 2016/12/5.
 */
public class Userty {
    private String id;
    private String name;
    private String gender;

    public Userty(String gender, String id, String name) {
        this.gender = gender;
        this.id = id;
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
