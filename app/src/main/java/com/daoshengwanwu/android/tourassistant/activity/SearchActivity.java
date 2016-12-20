package com.daoshengwanwu.android.tourassistant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.adapter.HistoryAdapter;
import com.daoshengwanwu.android.tourassistant.model.History;

import java.util.ArrayList;
import java.util.UUID;

public class SearchActivity extends Activity {
    private TextView log;
    private RelativeLayout btn;
    private ArrayList<History> ls = new ArrayList<History>();
    private ListView lv;
    private HistoryAdapter mAdapter;
    private ArrayAdapter<String> adapter;
    private void getData() {
        ls.add(new History((long) 1,R.drawable.history,"故宫"));
        ls.add(new History((long) 2,R.drawable.history,"八达岭长城"));
        ls.add(new History((long) 3,R.drawable.history,"天安门广场"));
        ls.add(new History((long) 4,R.drawable.history,"毛主席纪念堂"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiangshengda_activity_search);
        getData();
        mAdapter = new HistoryAdapter(getBaseContext(), ls);
        lv = (ListView) findViewById(R.id.Lv);
        log = (TextView) findViewById(R.id.His_log);
        lv.setAdapter(mAdapter);
        //每个Item的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ls.get(position).equals(1)){
                    ScenicspotActivity.actionStartActivity(SearchActivity.this, UUID.randomUUID().toString());
                }

            }
        });
        btn = (RelativeLayout) findViewById(R.id.Clean_history);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setVisibility(lv.GONE);
                btn.setVisibility(btn.GONE);
                log.setText("暂无历史记录");
            }
        });
    }

}
