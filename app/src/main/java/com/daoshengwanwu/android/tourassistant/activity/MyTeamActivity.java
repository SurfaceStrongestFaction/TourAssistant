package com.daoshengwanwu.android.tourassistant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.adapter.MyTeamAdapter;
import com.daoshengwanwu.android.tourassistant.item.team.MyTeamItem;
import com.daoshengwanwu.android.tourassistant.model.CreateQRImageTest;
import com.daoshengwanwu.android.tourassistant.model.UserWarehouse;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil.Group;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import internal.org.apache.http.entity.mime.content.Header;

public class MyTeamActivity extends BaseActivity {
    private static String KEY_BINDER = "MyTeamactivity.KEY_BINDER";
    private SharingService.SharingBinder mBinder;

    private ListView lv;
    private ArrayList<MyTeamItem> items=new ArrayList<>();
    private Button btn1;
    private Button btn2;
    private Button groupChat;
    private RelativeLayout transfer;

    private static final String TAG = "MyTeamActivity";
    private final String xyurl = "http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/user/getInformation";
    private final String xyurl1="http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/team/creatChat";
    private final String xyurl2 = "http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/team/getInformation";
    public String username;
    public String members;
    public String[] names;
    public int i;
    public MyTeamAdapter adapter;
    private ImageView back;
    public RelativeLayout add;

    private String mWhenMemberChangeTeamId = "";
    private List<String> mWhenMemberChangeIds = null;
    private static final int WHAT_ON_MEMBER_CHANGE = 10288;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unregisterOnTeamMemberChangeListener(mOnTeamMemberChangeListener);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_ON_MEMBER_CHANGE: {
                    if (null != mWhenMemberChangeIds) {
                        Log.d(TAG, "onTeamMemberChange: executed!" + mWhenMemberChangeIds);
                        UserWarehouse.getInstance(getApplicationContext()).updateUsersInfo(mWhenMemberChangeIds, new UserWarehouse.OnUsersInfoUpdatedListener() {
                            @Override
                            public void onUsersInfoUpdated(UserWarehouse userWarehouse) {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();

                        params.add("team_id", Group.GROUP_ID);
                        client.get(getApplicationContext(), "http://139.199.28.184:8083/team/getInformation", params, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    Group.CHAT_TEAM_ID = response.getString("chat_team_id");
                                    Group.GROUP_NAME = response.getString("name");
                                    Group.GROUP_CAPTIAN = response.getString("captain");

                                    ((TextView)findViewById(R.id.myTeam_name)).setText(Group.GROUP_NAME);
                                    getCaptianInfo(Group.GROUP_CAPTIAN);
//                                    items.clear();
//                                    adapter.notifyDataSetChanged();

//                                    for (i = 0; i < mWhenMemberChangeIds.size(); i++) {
//                                        AsyncHttpClient gclient = new AsyncHttpClient();
//                                        RequestParams pparams = new RequestParams();
//                                        final String user_id = mWhenMemberChangeIds.get(i);
//                                        pparams.add("user_id", user_id);
//                                        gclient.get(getApplicationContext(), AppUtil.JFinalServer.xyurl, pparams, new JsonHttpResponseHandler() {
//                                            @Override
//                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                                super.onSuccess(statusCode, headers, response);
//                                                try {
//                                                    String nick_name = response.getString("nick_name");
//                                                    String head_pic = response.getString("head_pic");
//                                                    items.add(new MyTeamItem(head_pic, nick_name, user_id));
//                                                    adapter.notifyDataSetChanged();
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                                                super.onFailure(statusCode, headers, throwable, errorResponse);
//                                            }
//                                        });
//                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } break;
                default: break;
            }
        }
    };

    private SharingService.OnTeamMemberChangeListener mOnTeamMemberChangeListener = new SharingService.OnTeamMemberChangeListener() {
        @Override
        public void onTeamMemberChange(String team_id, List<String> memberIds) {
            mWhenMemberChangeTeamId = team_id;
            mWhenMemberChangeIds = memberIds;

            Message msg = new Message();
            msg.what = WHAT_ON_MEMBER_CHANGE;

            mHandler.sendMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_my_team);

        //=====================白浩然=====================
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("team_id", Group.GROUP_ID);
        client.get(getApplicationContext(), "http://139.199.28.184:8083/team/getInformation", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Group.CHAT_TEAM_ID = response.getString("chat_team_id").equals("null") ? "" : response.getString("chat_team_id");
                    Group.GROUP_NAME = response.getString("name");
                    Group.GROUP_CAPTIAN = response.getString("captain");

                    doNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //================================================
    }

    private void doNext() {
        lv = (ListView)findViewById(R.id.myTeam_listView);
        adapter = new MyTeamAdapter(MyTeamActivity.this, UserWarehouse.getInstance(getApplicationContext()).getUsers());
        lv.setAdapter(adapter);
        UserWarehouse.getInstance(getApplicationContext()).updateUsersInfo(Group.GROUP_ID, new UserWarehouse.OnUsersInfoUpdatedListener() {
            @Override
            public void onUsersInfoUpdated(UserWarehouse userWarehouse) {
                adapter.notifyDataSetChanged();
            }
        });
        setItemClick();

        getData();
        mBinder = (SharingService.SharingBinder)getIntent().getSerializableExtra(KEY_BINDER);
        mBinder.registerOnTeamMemberChangeListener(mOnTeamMemberChangeListener);
        //队伍群聊
        groupChat=(Button)findViewById(R.id.group_chat);
        groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 //确保每次点击群聊都是最新的chat_team_id
                /*AsyncHttpClient gclient0 = new AsyncHttpClient();
                RequestParams params0 = new RequestParams();
                params0.add("team_id",AppUtil.Group.GROUP_ID);
                gclient0.get(getApplicationContext(),xyurl2,params0,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            AppUtil.Group.CHAT_TEAM_ID=response.getString("chat_team_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });*/
                /*while(TextUtils.isEmpty(AppUtil.Group.CHAT_TEAM_ID)){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.i("zhu", "创建队伍 "+ Group.CHAT_TEAM_ID+TextUtils.isEmpty(Group.CHAT_TEAM_ID));
                        if (Group.CHAT_TEAM_ID.equals("null")||Group.CHAT_TEAM_ID.equals("")) {
                            //队伍名
                            final String groupName = Group.GROUP_NAME;
                            String desc = "队伍简介";//队伍简介app并无体现，但作为创建群聊参数传入
                            //String[] members = data.getStringArrayExtra("newmembers");
                            List<String> member = new ArrayList<String>();
                            UserWarehouse userWarehouse = UserWarehouse.getInstance(getApplicationContext());
                            for (int i = 0; i < userWarehouse.getUsers().size(); i++) {
                                Log.i("zhu", "names:"+userWarehouse.getUsers().get(i).getUserId());
                                member.add(userWarehouse.getUsers().get(i).getUserId());
                            }
                            String[] members = member.toArray(new String[1]);
                            try {
                                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                                option.maxUsers = 200;
                                String reason = "Invite to join the group";
                                reason = EMClient.getInstance().getCurrentUser() + reason + groupName;
                                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                                Log.i("zhu", "创建队伍调用方法"+groupName);
                                EMGroup myGroup = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);
                                Group.CHAT_TEAM_ID = myGroup.getGroupId();
                                //创建成功后跳转到群聊页
                                Log.i("zhu", "onActivityResult:CHAT_TEAM_ID " + Group.CHAT_TEAM_ID);
                                Intent intent = new Intent(MyTeamActivity.this, ECChatActivity.class);
                                intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                                intent.putExtra("userId", Group.CHAT_TEAM_ID);
                                startActivity(intent);
                            } catch (final HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Log.i("zhu", "创建队伍错误 " + e.getLocalizedMessage());
                                        Toast.makeText(MyTeamActivity.this, "创建错误" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else
                        {
                            Log.i("zhu", "创建队伍else "+ Group.CHAT_TEAM_ID);
//                            try {
//                                EMClient.getInstance().groupManager().addUsersToGroup(Group.CHAT_TEAM_ID, names);
//                            } catch (HyphenateException e) {
//                                e.printStackTrace();
//                            }
                            Intent intent = new Intent(MyTeamActivity.this, ECChatActivity.class);
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                            intent.putExtra("userId", Group.CHAT_TEAM_ID);
                            startActivity(intent);
                        }
                    }
                }.start();

            }

                /*new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.i("zhu", "创建队伍 "+ Group.CHAT_TEAM_ID+TextUtils.isEmpty(Group.CHAT_TEAM_ID));
                        if (Group.CHAT_TEAM_ID.equals("null")) {
                            //队伍名
                            final String groupName = Group.GROUP_NAME;
                            String desc = "队伍简介";//队伍简介app并无体现，但作为创建群聊参数传入
                            //String[] members = data.getStringArrayExtra("newmembers");
                            List<String> member = new ArrayList<String>();
                            for (int i = 0; i < names.length; i++) {
                                Log.i("zhu", "names:"+names[i]);
                                member.add(names[i]);
                            }
                            String[] members = member.toArray(new String[1]);
                            try {
                                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                                option.maxUsers = 200;
                                String reason = "Invite to join the group";
                                reason = EMClient.getInstance().getCurrentUser() + reason + groupName;
                                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                                Log.i("zhu", "创建队伍调用方法"+groupName);
                                EMGroup myGroup = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);
                                Group.CHAT_TEAM_ID = myGroup.getGroupId();
                                //创建成功后跳转到群聊页
                                Log.i("zhu", "onActivityResult:CHAT_TEAM_ID " + Group.CHAT_TEAM_ID);
                                Intent intent = new Intent(MyTeamActivity.this, ECChatActivity.class);
                                intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                                intent.putExtra("userId", Group.CHAT_TEAM_ID);
                                startActivity(intent);
                            } catch (final HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Log.i("zhu", "创建队伍错误 " + e.getLocalizedMessage());
                                        Toast.makeText(MyTeamActivity.this, "创建错误" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else{
                            Log.i("zhu", "创建队伍else "+ Group.CHAT_TEAM_ID);
                            try {
                                EMClient.getInstance().groupManager().addUsersToGroup(Group.CHAT_TEAM_ID, names);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(MyTeamActivity.this, ECChatActivity.class);
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                            intent.putExtra("userId", Group.CHAT_TEAM_ID);
                            startActivity(intent);
                        }
                    }
                }.start();
                //保存myGroupId
                AsyncHttpClient gclient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add("team_id", Group.GROUP_ID);
                params.add("chat_team_id", Group.CHAT_TEAM_ID);
                gclient.get(getApplicationContext(),xyurl1,params,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if(!TextUtils.isEmpty(response.getString("chat_team_id "))){
                                Log.i("zhu", "onSuccess: "+"保存AppUtil.Group.CHAT_TEAM_ID成功");
                            };
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

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
        gclient.get(getApplicationContext(),AppUtil.JFinalServer.xyurl,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    username = response.getString("nick_name");
                    //zhu
                    TextView teamCaptain = (TextView) findViewById(R.id.team_captain);
                    teamCaptain.setText(username);
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
        gclient.get(getApplicationContext(),AppUtil.JFinalServer.xyurl2,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    members = response.getString("members");
                    Group.CHAT_TEAM_ID=response.getString("chat_team_id");
                    setMembers(members);

                    setItemClick();
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
        names = members.split(",");

        for ( i = 0; i < names.length; i++) {
            AsyncHttpClient gclient = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            final String user_id = names[i];
            params.add("user_id",user_id);

            gclient.get(getApplicationContext(), AppUtil.JFinalServer.xyurl, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        String nick_name = response.getString("nick_name");
                        String head_pic = response.getString("head_pic");
                        items.add(new MyTeamItem(head_pic, nick_name, user_id));
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
        tv.setText(Group.GROUP_NAME);
        getCaptianInfo(Group.GROUP_CAPTIAN);

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
                img.setImageBitmap(CreateQRImageTest.createQRImage(Group.GROUP_ID));
                new  AlertDialog.Builder(MyTeamActivity.this)
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
                //点击队伍成员列表跳转到队伍成员页
                Intent intent =new Intent(MyTeamActivity.this,TeamMemberActivity.class);
                intent.putExtra("memberId", UserWarehouse.getInstance(getApplicationContext()).getUsers().get(position).getUserId());
                startActivity(intent);
                //TeamMemberActivity.actionStartActivity(MyTeamActivity.this);
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
