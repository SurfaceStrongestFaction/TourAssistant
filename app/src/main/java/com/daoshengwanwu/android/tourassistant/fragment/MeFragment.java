package com.daoshengwanwu.android.tourassistant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.view.CircleImageView;


public class MeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jiangshengda_layout_me, container, false);
        TextView tv = (TextView)view.findViewById(R.id.mName);
        CircleImageView cv = (CircleImageView)view.findViewById(R.id.mImg);
        ImageView iv = (ImageView)view.findViewById(R.id.mGender);
        tv.setText(AppUtil.User.USER_NAME);
        cv.setImageBitmap(AppUtil.User.USER_IMG);
        if(AppUtil.User.USER_GENDER.equals("女")){
            iv.setImageResource(R.drawable.woman);
        }
        return view;
    }
}
