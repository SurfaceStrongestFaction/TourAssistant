package com.daoshengwanwu.android.tourassistant.shenyue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/11/24.
 */

public class PicturegvAdapter extends BaseAdapter {

    private Context context;
    private List<PicgvItem> picgv = new ArrayList<PicgvItem>();

    public PicturegvAdapter(Context context, List<PicgvItem> picgv) {
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
        return picgv.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.shenyue_activity_picgvitem, null);
        }
        ImageView img = (ImageView)convertView.findViewById(R.id.picgvitem_img);
        img.setImageResource(picgv.get(position).getImgsrc());
        return convertView;
    }


}
