package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.item.team.MyTeamItem;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.utils.LoaderImage;
import com.daoshengwanwu.android.tourassistant.view.CircleImageView;
import com.hyphenate.easeui.EaseConstant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class TeamMemberActivity extends BaseActivity {
    private static final String EXTRA_USER_NAME = "SecondActivity.EXTRA_USER_NAME";
    private ImageView back;
    private Button button1;
    private String memberId;
    private TextView memberName1;
    private TextView memberName2;
    private TextView memberTell;
    private CircleImageView memberPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_team_member);
        initView();
        memberId=getIntent().getExtras().getString("memberId");
        AsyncHttpClient gclient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",memberId);
        gclient.get(getApplicationContext(), AppUtil.JFinalServer.xyurl,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    memberName1.setText(response.getString("nick_name"));
                    memberName2.setText(response.getString("nick_name"));
                    memberTell.setText(response.getString("tel"));
                    LoaderImage loaderImage=new LoaderImage(TeamMemberActivity.this,memberPic,response.getString("head_pic"));
                    loaderImage.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamMemberActivity.this.finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ECChatActivity.startChatActivity(TeamMemberActivity.this);
                //调用环信EM通讯页
                if(!AppUtil.User.USER_ID.equals(memberId)) {
                    Intent intent = new Intent(TeamMemberActivity.this, ECChatActivity.class);
                    //chatId为聊天者的环信id
                    String chatId = memberId;
                    intent.putExtra("userId", chatId);
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }else{
                    Toast.makeText(TeamMemberActivity.this, "不要和自己聊天", Toast.LENGTH_SHORT).show();
                }
                //ChatActivity.startChatActivity(TeamMemberActivity.this);


            }
        });
    }

    private void initView() {
        memberName1=(TextView) findViewById(R.id.team_member_name1);
        memberName2=(TextView) findViewById(R.id.team_member_name2);
        button1=(Button)findViewById(R.id.myTeam_button1);
        back=(ImageView)findViewById(R.id.activity_team_member_back);
        memberTell=(TextView)findViewById(R.id.Team_member_tell);
        memberPic=(CircleImageView)findViewById(R.id.activity_team_member_pic);
    }

    public static  void actionStartActivity(Context packageContext) {
        Intent intent = new Intent(packageContext, TeamMemberActivity.class);
        packageContext.startActivity(intent);
    }
}
