package com.daoshengwanwu.android.tourassistant.caizihuan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatTableActivity extends Activity {
    private int[] ImageIds = new int[]{R.drawable.touxiang1,R.drawable.touxiang2};
    private String[] names = new String[]{"我的小队伍","李四四"};
    private String[] dates = new String[]{"10:39","11-13"};
    private String[] chatContents = new String[]{"中午12点集合","我在超市这边"};
    private EditText searchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caizihuan_activity_chat_table);
        searchEditText = (EditText)findViewById(R.id.Et_activitychattable_search);
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for (int i= 0;i<ImageIds.length;i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("image",ImageIds[i]);
            listItem.put("name",names[i]);
            listItem.put("date",dates[i]);
            listItem.put("chatContent",chatContents[i]);
            listItems.add(listItem);
        }
        //创建1个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.caizihuan_chattable_item,
                new String[]{"image","name","date","chatContent"},new int[]
                {R.id.chattable_item_touxiang,R.id.Tv_activitychattable_name,R.id.Tv_activitychattable_time,R.id.Tv_activitychattable_chatcontent});
        ListView listView = (ListView) findViewById(R.id.Lv_activitychattable_chattable);
        //为ListView设置Adapter
        listView.setAdapter(simpleAdapter);
        //为item绑定点击事件
        listView.setOnItemClickListener(itemListener);
        //为搜索框绑定点击事件
        searchEditText.setOnClickListener(searchListener);
    }
    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(ChatTableActivity.this,ChatActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ChatTableActivity.this,SearchMemberActivity.class);
            startActivity(intent);
        }
    };
}
