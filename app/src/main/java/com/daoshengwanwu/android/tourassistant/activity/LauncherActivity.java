package com.daoshengwanwu.android.tourassistant.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.daoshengwanwu.android.tourassistant.fragment.HomeFragment;
import com.daoshengwanwu.android.tourassistant.fragment.MapsFragment;
import com.daoshengwanwu.android.tourassistant.fragment.MeFragment;
import com.daoshengwanwu.android.tourassistant.fragment.TeamFragment;
import com.daoshengwanwu.android.tourassistant.model.MapsFragmentSaveData;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;


public class LauncherActivity extends BaseActivity {
    static public boolean fog_draw_pause_judge = true;
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
    private HomeFragment mHomeFragment;
    private TeamFragment mTeamFragment;
    private MeFragment mMeFragment;
    private MapsFragment mMapsFragment;
    private MapsFragmentSaveData mMapsFragmentSaveData = null;
    private SharingService.SharingBinder mSharingBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mSharingBinder = (SharingService.SharingBinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baihaoran_activity_launcher);

        //环信easeUI初始化
        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);

        //应用启动时即绑定服务
        bindService(SharingService.newIntent(this), mServiceConnection, BIND_AUTO_CREATE);
        getWidgetsReferences(); //获取所需组件的引用
        setListenersToWidgets(); //为组件设置监听器

        initFragment();
    }
//---------------------------胜达--------------------------------------------------------------------
    @Override
    protected void onPause() {
        super.onPause();
        fog_draw_pause_judge = false;
    }

    public static  void actionStartActivity(Context packageContext) {
        Intent intent = new Intent(packageContext,  LauncherActivity.class);
        packageContext.startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        fog_draw_pause_judge = true;
    }
//--------------------------------------------------------------------------------------------------

    private void initFragment() {
        mHomeFragment = HomeFragment.newInstance();

        Fragment fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
        if (null != fragment) {
            mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mHomeFragment).commit();
        } else {
            mFragmentManager.beginTransaction().add(R.id.launcher_fragment_container, mHomeFragment).commit();
        }
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
        private void clearIcon() {
            mTabsHomeImg.setImageResource(R.drawable.home);
            mTabsMapImg.setImageResource(R.drawable.map);;
            mTabsRanksImg.setImageResource(R.drawable.ranks);;
            mTabsMyImg.setImageResource(R.drawable.my);;
            mTabsHomeText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
            mTabsMapText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
            mTabsRanksText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
            mTabsMyText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tabs_home_page:
                    clearIcon();

                    mTabsHomeImg.setImageResource(R.drawable.home1);
                    mTabsHomeText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));

                    //判断当前显示的Fragment是否是MapsFragment
                    Fragment fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        mMapsFragmentSaveData = mMapsFragment.getCurrentState();
                    } else if (fragment instanceof HomeFragment) {
                        break;
                    }

                    mHomeFragment = HomeFragment.newInstance();
                    mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mHomeFragment).commit();
                    break;
                case R.id.tabs_map_page:
                    clearIcon();

                    mTabsMapImg.setImageResource(R.drawable.map1);
                    mTabsMapText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));

                    //判断当前显示的Fragment是否是MapsFragment
                    fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        break;
                    }

                    mMapsFragment = MapsFragment.newInstance(mSharingBinder, mMapsFragmentSaveData, "abcd");
                    mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mMapsFragment).commit();
                    break;
                case R.id.tabs_ranks_page:
                    //进入队伍页面时首先要登录
                    String user_id = AppUtil.User.USER_ID;
                    if (null == user_id || user_id.equals("") || user_id.equals("null")) {
                        Toast.makeText(LauncherActivity.this, "请先登录再使用队伍功能", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    clearIcon();
                    mTabsRanksImg.setImageResource(R.drawable.ranks1);
                    mTabsRanksText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));

                    //判断当前显示的Fragment是否是MapsFragment
                    fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        mMapsFragmentSaveData = mMapsFragment.getCurrentState();
                    } else if (fragment instanceof TeamFragment) {
                        break;
                    }

                    mTeamFragment = TeamFragment.newInstance(mSharingBinder);
                    mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mTeamFragment).commit();
                    break;
                case R.id.tabs_my_page:
                    //判断当前显示的Fragment是否是MapsFragment
                    fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        mMapsFragmentSaveData = mMapsFragment.getCurrentState();
                    }

                    if ("".equals(AppUtil.User.USER_ID) || AppUtil.User.USER_ID == null) {
                        //说明还没有登陆，应该跳转到登录界面
                        LoginActivity.actionStartActivity(LauncherActivity.this);
                        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                    } else {
                        clearIcon();

                        //说明已经登录，进入我的界面
                        if (fragment instanceof MeFragment) {
                            break;
                        }
                        mMeFragment = new MeFragment();
                        mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mMeFragment).commit();

                        mTabsMyImg.setImageResource(R.drawable.my1);
                        mTabsMyText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));
                    }
                    break;
            }
        }
    }
}
