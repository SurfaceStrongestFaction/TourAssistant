package com.daoshengwanwu.android.tourassistant.jiangshengda;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daoshengwanwu.android.tourassistant.R;


public class MeFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.jiangshengda_layout_me, container, false);
        //Codes
        return v;
    }
}
