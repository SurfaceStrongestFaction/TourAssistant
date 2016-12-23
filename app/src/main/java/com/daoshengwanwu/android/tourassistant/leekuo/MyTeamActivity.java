package com.daoshengwanwu.android.tourassistant.leekuo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;

public class MyTeamActivity extends Activity {
    private ListView lv;
    private ArrayList<MyTeamItem> items=new ArrayList<>();
    private Button btn1;
    private Button btn2;
    private RelativeLayout transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_my_team);
        getData();
        MyTeamAdapter adapter=new MyTeamAdapter(this,items);
        lv=(ListView)findViewById(R.id.myTeam_listView);
        lv.setAdapter(adapter);
        setItemClick();
        btn2=(Button)findViewById(R.id.myTeam_button2);
        transfer=(RelativeLayout)findViewById(R.id.myTeam_transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = TransferTeamActivity.newIntent(MyTeamActivity.this,"李阔");
                startActivity(i);
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, MyTeamActivity.class);
        return i;
    }
    public void  getData(){
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));
        items.add(new MyTeamItem(R.drawable.item_pic2,"申玥"));


    }

    public void setItemClick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
                Intent i = TeamMemberActivity.newIntent(MyTeamActivity.this,"李阔");
                startActivity(i);
            }
        });
    }
}
