package com.daoshengwanwu.android.tourassistant.jiangshengda;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil;


public class MeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jiangshengda_layout_me, container, false);
        TextView tv = (TextView)view.findViewById(R.id.mName);
        CircleImageView cv = (CircleImageView)view.findViewById(R.id.mImg);
        tv.setText(AppUtil.User.USER_NAME);
        cv.setImageBitmap(AppUtil.User.USER_IMG);
        return view;
    }
}
