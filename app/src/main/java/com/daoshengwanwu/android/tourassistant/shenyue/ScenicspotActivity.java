package com.daoshengwanwu.android.tourassistant.shenyue;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.leekuo.BaseActivity;

public class ScenicspotActivity extends BaseActivity {
    private static final String SCENICSPOTEXTRA_ID = "ScenicspotActivity.EXTRA_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_scenicspot);
        getSupportActionBar().hide();
        getIntent().getIntExtra(SCENICSPOTEXTRA_ID, 0);
    }


    public static Intent newIntent(Context packageContext, int id) {
        Intent i = new Intent(packageContext, ScenicspotActivity.class);
        i.putExtra(SCENICSPOTEXTRA_ID, id);

        return i;
    }
}
