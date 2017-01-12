package com.daoshengwanwu.android.tourassistant.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.item.picgv.PicgvItem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        ImageView img = (ImageView)convertView.findViewById(R.id.picgvitem_picture_img);
        Drawable a = picgv.get(position).getImgsrc();
        img.setImageDrawable(a);
        System.out.println("aa+"+picgv.get(position).getImgsrc());
        return convertView;
    }
}
