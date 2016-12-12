package com.daoshengwanwu.android.tourassistant.shenyue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.leekuo.BaseActivity;

public class ViewspotActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_viewspot);
        getSupportActionBar().hide();
    }
}
