package com.daoshengwanwu.android.tourassistant.leekuo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.daoshengwanwu.android.tourassistant.R;

public class TeamMemberActivity extends BaseActivity {
    private static final String EXTRA_USER_NAME = "SecondActivity.EXTRA_USER_NAME";
    private ImageView back;

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
    }

    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, TeamMemberActivity.class);
        i.putExtra(EXTRA_USER_NAME, userName);
        return i;
    }
}
