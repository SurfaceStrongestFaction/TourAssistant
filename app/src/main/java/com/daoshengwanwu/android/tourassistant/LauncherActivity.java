package com.daoshengwanwu.android.tourassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zhu.PersonalDataActivity;
import zhu.SetActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Intent i = SetActivity.newIntent(this, "消息");
        startActivity(i);

    }
}
