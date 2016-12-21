package com.daoshengwanwu.android.tourassistant.leekuo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;

public class TeamerMyTeamActivity extends Activity {
    private ListView lv;
    private ArrayList<TeamerMyTeamItem> items=new ArrayList<>();
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_my_team_teamer);
        getData();
        TeamerMyTeamAdapter adapter=new TeamerMyTeamAdapter(this,items);
        lv=(ListView)findViewById(R.id.Teamer_myTeam_listView);
        lv.setAdapter(adapter);
        setItemClick();
    }
    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, TeamerMyTeamActivity.class);
        return i;
    }
    public void  getData(){
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new TeamerMyTeamItem(R.drawable.item_pic2,"申玥"));
    }

    public void setItemClick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
                Intent i = TeamMemberActivity.newIntent(TeamerMyTeamActivity.this,"李阔");
                startActivity(i);
            }
        });
    }
}
