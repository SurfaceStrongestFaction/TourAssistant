package com.daoshengwanwu.android.tourassistant.jiangshengda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends BaseAdapter{
    private Context context;
    private List<History> lhistory = new ArrayList<>();

    public HistoryAdapter(Context c,ArrayList<History> ls){
        context = c;
        lhistory = ls;
    }

    @Override
    public int getCount(){
        return lhistory.size();
    }

    @Override
    public Object getItem(int i){
        return lhistory.get(i);
    }

    @Override
    public long getItemId(int i){
        return lhistory.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.jiangshengda_list_history,null);
        }
        TextView HisTv = (TextView) view.findViewById(R.id.His_Tv);
        HisTv.setText(lhistory.get(i).getHistory());
        ImageView ListImg = (ImageView) view.findViewById(R.id.His_Img);
        ListImg.setImageResource(lhistory.get(i).getSrc());
        return view;

    }
}
