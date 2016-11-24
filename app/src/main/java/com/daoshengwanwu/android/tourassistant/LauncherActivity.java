package com.daoshengwanwu.android.tourassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhouxiuya.android.tourassistant.AboutActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //启动AboutActivity：
        Intent i = AboutActivity.newIntent(this, "");
        startActivity(i);
    }
}
