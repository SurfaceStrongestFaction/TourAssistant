package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.item.track.BaseItem;
import com.daoshengwanwu.android.tourassistant.adapter.TrackAdapter;
import com.daoshengwanwu.android.tourassistant.item.track.item0;
import com.daoshengwanwu.android.tourassistant.item.track.item1;

import java.util.ArrayList;
import java.util.List;

public class MyTrackActivity extends BaseActivity {
    private ListView mlistView;
    //适配器
    private TrackAdapter trackAdapter = null;
    //数据
    private List<BaseItem> mData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caizihuan_activity_my_track);
        mlistView=(ListView)findViewById(R.id.Lv_activitymytrack_list);
        init();
    }
    private void init(){
        this.mData = new ArrayList<BaseItem>();
        this.mData.add(new item0(0,"11月20日"));
        this.mData.add(new item1(1,R.drawable.gugong,"故宫","80%","90%"));
        this.mData.add(new item1(1,R.drawable.haiyangguan,"海洋馆","60%","50%"));
        this.mData.add(new item0(0,"11月6日"));
        this.mData.add(new item1(1,R.drawable.gugong,"故宫","60%","40%"));
        this.trackAdapter = new TrackAdapter(MyTrackActivity.this,this.mData);
        this.mlistView.setAdapter(this.trackAdapter);
    }
    public static void startMyTrackActivity(Context c){
        Intent i = new Intent(c,MyTrackActivity.class);
        c.startActivity(i);
    }
}
