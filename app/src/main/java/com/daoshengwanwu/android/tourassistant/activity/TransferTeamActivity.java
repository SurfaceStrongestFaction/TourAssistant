package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.adapter.TransferTeamAdapter;
import com.daoshengwanwu.android.tourassistant.item.team.TransferTeamItem;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;

import java.util.ArrayList;

public class TransferTeamActivity extends BaseActivity {
    private ListView lv;
    private ArrayList<TransferTeamItem> items=new ArrayList<>();
    private static final String EXTRA_USER_NAME = "SecondActivity.EXTRA_USER_NAME";
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_transfer_team);
        getData();
        TransferTeamAdapter adapter=new TransferTeamAdapter(this,items);
        lv=(ListView)findViewById(R.id.transferTeam_listView);
        lv.setAdapter(adapter);
        setItemClick();
        //获取传递过来的数据：
        String userName = getIntent().getStringExtra(EXTRA_USER_NAME);
        //Toast.makeText(this, "userName: " + userName, Toast.LENGTH_SHORT).show();
        back=(ImageView)findViewById(R.id.transferTeam_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferTeamActivity.this.finish();
            }
        });
    }

    public static  void actionStartActivity(Context packageContext) {
        Intent intent = new Intent(packageContext,  TransferTeamActivity.class);
        packageContext.startActivity(intent);
    }

    public void  getData(){
        items.add(new TransferTeamItem(AppUtil.User.USER_IMG,AppUtil.User.USER_NAME));
        items.add(new TransferTeamItem(AppUtil.User.USER_IMG,AppUtil.User.USER_NAME));
        items.add(new TransferTeamItem(AppUtil.User.USER_IMG,AppUtil.User.USER_NAME));
        items.add(new TransferTeamItem(AppUtil.User.USER_IMG,AppUtil.User.USER_NAME));
        items.add(new TransferTeamItem(AppUtil.User.USER_IMG,AppUtil.User.USER_NAME));
        items.add(new TransferTeamItem(AppUtil.User.USER_IMG,AppUtil.User.USER_NAME));


    }

    public void setItemClick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
               /* String itemName=((TextView)view.findViewById(R.id.itemName)).getText().toString();
                String itemPic = ((ImageView) view.findViewById(R.id.img)).getDrawable().toString();
                Intent i = new Intent(Activity1.this, TalkActivity.class);
                overridePendingTransition(R.anim.zoom_exit,R.anim.zoom_enter);
                i.putExtra("NAME", itemName);
                i.putExtra("PIC",itemPic);
                startActivityForResult(i,1);*/

            }
        });
    }
}
