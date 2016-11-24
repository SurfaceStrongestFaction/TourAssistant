package com.daoshengwanwu.android.tourassistant.baihaoran;


import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;


public class LauncherActivity extends AppCompatActivity {
    private ImageView mTabsHomeImg;
    private ImageView mTabsMapImg;
    private ImageView mTabsRanksImg;
    private ImageView mTabsMyImg;
    private TextView mTabsHomeText;
    private TextView mTabsMapText;
    private TextView mTabsRanksText;
    private TextView mTabsMyText;
    private LinearLayout mTabsHomePage;
    private LinearLayout mTabsMapPage;
    private LinearLayout mTabsRanksPage;
    private LinearLayout mTabsMyPage;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //去掉工具栏
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }

        getWidgetsReferences(); //获取所需组件的引用
        setListenersToWidgets(); //为组件设置监听器
    }

    private void getWidgetsReferences() {
        mTabsHomeImg = (ImageView)findViewById(R.id.tabs_home_img);
        mTabsMapImg = (ImageView)findViewById(R.id.tabs_map_img);
        mTabsRanksImg = (ImageView)findViewById(R.id.tabs_ranks_img);
        mTabsMyImg = (ImageView)findViewById(R.id.tabs_my_img);
        mTabsHomeText = (TextView)findViewById(R.id.tabs_home_text);
        mTabsMapText = (TextView)findViewById(R.id.tabs_map_text);
        mTabsRanksText = (TextView)findViewById(R.id.tabs_ranks_text);
        mTabsMyText = (TextView)findViewById(R.id.tabs_my_text);
        mTabsHomePage = (LinearLayout)findViewById(R.id.tabs_home_page);
        mTabsMapPage = (LinearLayout)findViewById(R.id.tabs_map_page);
        mTabsRanksPage = (LinearLayout)findViewById(R.id.tabs_ranks_page);
        mTabsMyPage = (LinearLayout)findViewById(R.id.tabs_my_page);
        mFragmentManager = getSupportFragmentManager();
    }

    private void setListenersToWidgets() {
        OnTabClickListener onTabClickListener = new OnTabClickListener();
        mTabsHomePage.setOnClickListener(onTabClickListener);
        mTabsMapPage.setOnClickListener(onTabClickListener);
        mTabsRanksPage.setOnClickListener(onTabClickListener);
        mTabsMyPage.setOnClickListener(onTabClickListener);
    }

    private class OnTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mTabsHomeImg.setImageResource(R.drawable.home);
            mTabsMapImg.setImageResource(R.drawable.map);;
            mTabsRanksImg.setImageResource(R.drawable.ranks);;
            mTabsMyImg.setImageResource(R.drawable.my);;
            mTabsHomeText.setTextColor(ContextCompat.getColor(LauncherActivity.this, android.R.color.black));
            mTabsMapText.setTextColor(ContextCompat.getColor(LauncherActivity.this, android.R.color.black));
            mTabsRanksText.setTextColor(ContextCompat.getColor(LauncherActivity.this, android.R.color.black));
            mTabsMyText.setTextColor(ContextCompat.getColor(LauncherActivity.this, android.R.color.black));

            switch (view.getId()) {
                case R.id.tabs_home_page:
                    mTabsHomeImg.setImageResource(R.drawable.home1);
                    mTabsHomeText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));
                    break;
                case R.id.tabs_map_page:
                    mTabsMapImg.setImageResource(R.drawable.map1);
                    mTabsMapText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));
                    break;
                case R.id.tabs_ranks_page:
                    mTabsRanksImg.setImageResource(R.drawable.ranks1);
                    mTabsRanksText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));
                    break;
                case R.id.tabs_my_page:
                    mTabsMyImg.setImageResource(R.drawable.my1);
                    mTabsMyText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));
                    break;
            }
        }
    }
}
