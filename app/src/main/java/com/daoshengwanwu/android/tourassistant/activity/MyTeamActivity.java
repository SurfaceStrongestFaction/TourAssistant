package com.daoshengwanwu.android.tourassistant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.adapter.MyTeamAdapter;
import com.daoshengwanwu.android.tourassistant.item.team.MyTeamItem;
import com.daoshengwanwu.android.tourassistant.model.CreateQRImageTest;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyTeamActivity extends BaseActivity {
    private static String KEY_BINDER = "MyTeamactivity.KEY_BINDER";
    private SharingService.SharingBinder mBinder;

    private ListView lv;
    private ArrayList<MyTeamItem> items=new ArrayList<>();
    private Button btn1;
    private Button btn2;
    private RelativeLayout transfer;
    private final String xyurl = new String("http://123.206.14.122/user/getInformation");
    private final String xyurl2 = new String("http://123.206.14.122/team/getInformation");
    public String username;
    public String members;
    public String[] names;
    public int i;
    public MyTeamAdapter adapter;
    private ImageView back;
    public RelativeLayout add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_my_team);
        getData();
        mBinder = (SharingService.SharingBinder)getIntent().getSerializableExtra(KEY_BINDER);
        mBinder.registerOnTeamMemberChangeListener(new SharingService.OnTeamMemberChangeListener() {
            @Override
            public void onTeamMemberChange(String team_id, List<String> memberIds) {
                getCaptianInfo(AppUtil.Group.GROUP_CAPTIAN);
                for ( i = 0; i <  memberIds.size(); i++) {
                    AsyncHttpClient gclient = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("user_id", memberIds.get(i));
                    gclient.get(getApplicationContext(),xyurl,params,new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                String n = response.getString("nick_name");
                                items.add(new MyTeamItem(AppUtil.User.USER_IMG,n));
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });
    }

    public static  void actionStartActivity(Context packageContext) {
        Intent intent = new Intent(packageContext,  MyTeamActivity.class);
        packageContext.startActivity(intent);
    }



    private void getCaptianInfo(String groupcaptian) {
        AsyncHttpClient gclient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", groupcaptian);
        gclient.get(getApplicationContext(),xyurl,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    username = response.getString("nick_name");
                    TextView tv1 = (TextView) findViewById(R.id.myTeam_leader);
                    tv1.setText(username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
    }

    private void getMembersInfo(String groupid) {
        AsyncHttpClient gclient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("team_id",groupid);
        gclient.get(getApplicationContext(),xyurl2,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    members = response.getString("members");
                    setMembers(members);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void setMembers(String members) {
        names = members.split("\\,");
        for ( i = 0; i < names.length; i++) {
            AsyncHttpClient gclient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("user_id",names[i]);
            gclient.get(getApplicationContext(),xyurl,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        String n = response.getString("nick_name");
                        items.add(new MyTeamItem(AppUtil.User.USER_IMG,n));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }


    public void  getData(){
        back=(ImageView)findViewById(R.id.myTeam_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTeamActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.myTeam_name);
        tv.setText(AppUtil.Group.GROUP_NAME);
        getCaptianInfo(AppUtil.Group.GROUP_CAPTIAN);
        getMembersInfo(AppUtil.Group.GROUP_ID);
        adapter = new MyTeamAdapter(this,items);
        lv = (ListView)findViewById(R.id.myTeam_listView);
        lv.setAdapter(adapter);
        setItemClick();
        btn2 = (Button)findViewById(R.id.myTeam_button2);
        transfer = (RelativeLayout)findViewById(R.id.myTeam_transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferTeamActivity.actionStartActivity(MyTeamActivity.this);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        add = (RelativeLayout) findViewById(R.id.addmember);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img =  new ImageView(MyTeamActivity.this);
                img.setImageBitmap(CreateQRImageTest.createQRImage("我是王肖"));
                new  AlertDialog.Builder(MyTeamActivity.this)
                        .setTitle("队伍二维码" )
                        .setView(img)
                        .setPositiveButton("确定" ,  null )
                        .show();
            }
        });
    }

    public void setItemClick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
                /*Intent i = TeamMemberActivity.newIntent(MyTeamActivity.this,"李阔");
                startActivity(i);*/
                TeamMemberActivity.actionStartActivity(MyTeamActivity.this);
                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            }
        });
    }


    public static void actionStartActivity(Context packageContext, SharingService.SharingBinder binder) {
        Intent intent = new Intent(packageContext, MyTeamActivity.class);
        intent.putExtra(KEY_BINDER, binder);
        packageContext.startActivity(intent);
    }
}
