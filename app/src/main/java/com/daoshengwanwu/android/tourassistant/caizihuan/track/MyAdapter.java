package com.daoshengwanwu.android.tourassistant.caizihuan.track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.List;

/**
 * Created by dell on 2016/11/28.
 */
public class MyAdapter extends BaseAdapter{
    private LayoutInflater mInflater = null;
    private List<BaseItem> mData = null;//要显示的数据

    public MyAdapter(Context mContext, List<BaseItem> dataList) {
        super();
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = dataList;
    }
    //添加一个新的Item，并通知listview进行显示刷新
    public void addItem(BaseItem newItem){
        this.mData.add(newItem);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mData == null){
            return 0;
        }
        return this.mData.size();

    }
    //每个convert view都会调用此方法，获得当前所需要的view样式
    public int getItemViewType(int position) {
        return mData.get(position).getItem_type();
    }
    @Override
    public Object getItem(int i) {
        return mData.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View viewItem0 = null;
        View viewItem1 = null;
        int itemType = this.getItemViewType(position);
        if(itemType == 0){
            //第一种item
            item0Holder viewHolder0 = null;
            if(convertView == null){
                //没有缓存过
                viewHolder0 = new item0Holder();
                viewItem0 = this.mInflater.inflate(R.layout.caizihuan_item0, null, false);
                viewHolder0.textView = (TextView)viewItem0.findViewById(R.id.Tv_mytrackitem_date);
                viewItem0.setTag(viewHolder0);
                convertView = viewItem0;
            }else{
                viewHolder0 = (item0Holder)convertView.getTag();
            }
            viewHolder0.textView.setText(((item0) mData.get(position)).getDateValue());
        }else {
            //第二种item
            item1Holder viewHolder1 = null;
            if(convertView == null){
                //没有缓存过
                viewHolder1 = new item1Holder();
                viewItem1 = this.mInflater.inflate(R.layout.caizihuan_item1, null, false);
                viewHolder1.imageView = (ImageView) viewItem1.findViewById(R.id.Iv_trackitem_image);
                viewHolder1.nameView = (TextView)viewItem1.findViewById(R.id.Tv_trackitem_placename);
                viewHolder1.finishView = (TextView)viewItem1.findViewById(R.id.Tv_trackitem_finishvalue);
                viewHolder1.suipianView = (TextView)viewItem1.findViewById(R.id.Tv_trackitem_suipianvalue);
                viewItem1.setTag(viewHolder1);
                convertView = viewItem1;
            }else{
                viewHolder1 = (item1Holder)convertView.getTag();
            }
            viewHolder1.imageView.setImageResource(((item1)mData.get(position)).getImageId());
            viewHolder1.nameView.setText(((item1)mData.get(position)).getNameValue());
            viewHolder1.finishView.setText(((item1)mData.get(position)).getFinishValue());
            viewHolder1.suipianView.setText(((item1)mData.get(position)).getSuipianValue());
        }
        return convertView;
    }

}
