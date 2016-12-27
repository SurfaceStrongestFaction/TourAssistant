package com.daoshengwanwu.android.tourassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.item.team.MyTeamItem;
import com.daoshengwanwu.android.tourassistant.item.team.TeamerMyTeamItem;
import com.daoshengwanwu.android.tourassistant.model.User;
import com.hyphenate.easeui.widget.LoaderImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2016/11/22.
 */
public class MyTeamAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mUsers = null;

    public MyTeamAdapter(Context context, List<User> users) {
        mContext = context;
        mUsers = users;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public User getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lk_activity_my_team_item, parent, false);
        }
        ImageView pic=(ImageView)convertView.findViewById(R.id.activity_my_team_item_pic);
        LoaderImage loaderImage=new LoaderImage(mContext,pic,mUsers.get(position).getHeadPicUrl());
        loaderImage.start();
        TextView name=(TextView)convertView.findViewById(R.id.activity_my_team_item_text);
        name.setText(mUsers.get(position).getNickName());
        return convertView;
    }
}
