package com.daoshengwanwu.android.tourassistant.leekuo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.caizihuan.ChatActivity;

public class TeamMemberActivity extends BaseActivity {
    private static final String EXTRA_USER_NAME = "SecondActivity.EXTRA_USER_NAME";
    private ImageView back;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lk_activity_team_member);
        back=(ImageView)findViewById(R.id.activity_team_member_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamMemberActivity.this.finish();
            }
        });
        button1=(Button)findViewById(R.id.myTeam_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChatActivity.startChatActivity(TeamMemberActivity.this);

            }
        });
    }

    public static  void actionStartActivity(Context packageContext) {
        Intent intent = new Intent(packageContext, TeamMemberActivity.class);
        packageContext.startActivity(intent);
    }
}
