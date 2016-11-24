package com.daoshengwanwu.android.tourassistant.shenyue;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/11/24.
 */

public class PicturegvAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> picgv = new ArrayList<Integer>();

    public PicturegvAdapter(Context context, List<Integer> picgv) {
        this.context = context;
        this.picgv = picgv;
    }

    @Override
    public int getCount() {
        return picgv.size();
    }

    @Override
    public Object getItem(int position) {
        return picgv.get(position);
    }

    @Override
    public long getItemId(int position) {
        return picgv.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
