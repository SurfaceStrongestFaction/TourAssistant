package com.daoshengwanwu.android.tourassistant.jiangshengda;

/**
 * Created by jsd58 on 11/24/2016.
 */

public class History {
    private int src;

    public String getHistory() {
        return History;
    }

    public void setHistory(String history) {
        History = history;
    }

    private String History;
    private long id;

    public History(long id,int src,String history){
        this.id = id;
        History = history;
        this.src = src;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

}

