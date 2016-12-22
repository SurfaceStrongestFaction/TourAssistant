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

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.fragment.HomeFragment;
import com.daoshengwanwu.android.tourassistant.fragment.MapFragment;
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
        if (null == mHomeFragment) {
            mHomeFragment = HomeFragment.newInstance();
        }

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
        @Override
        public void onClick(View view) {
            mTabsHomeImg.setImageResource(R.drawable.home);
            mTabsMapImg.setImageResource(R.drawable.map);;
            mTabsRanksImg.setImageResource(R.drawable.ranks);;
            mTabsMyImg.setImageResource(R.drawable.my);;
            mTabsHomeText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
            mTabsMapText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
            mTabsRanksText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));
            mTabsMyText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_text_color));

            switch (view.getId()) {
                case R.id.tabs_home_page:
                    mTabsHomeImg.setImageResource(R.drawable.home1);
                    mTabsHomeText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));

                    //判断当前显示的Fragment是否是MapsFragment
                    Fragment fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        mMapsFragmentSaveData = mMapsFragment.getCurrentState();
                    }

                    if (null == mHomeFragment) {
                        mHomeFragment = HomeFragment.newInstance();
                    }
                    mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mHomeFragment).commit();

                    break;
                case R.id.tabs_map_page:
                    mTabsMapImg.setImageResource(R.drawable.map1);
                    mTabsMapText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));

                    //判断当前显示的Fragment是否是MapsFragment
                    fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        break;
                    }

                    mMapsFragment = MapsFragment.newInstance(mSharingBinder, mMapsFragmentSaveData);

                    mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mMapsFragment).commit();
                    break;
                case R.id.tabs_ranks_page:
                    mTabsRanksImg.setImageResource(R.drawable.ranks1);
                    mTabsRanksText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));

                    //判断当前显示的Fragment是否是MapsFragment
                    fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        mMapsFragmentSaveData = mMapsFragment.getCurrentState();
                    }

                    if (null == mTeamFragment) {
                        mTeamFragment = TeamFragment.newInstance(mSharingBinder);
                    }
                    mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mTeamFragment).commit();
                    break;
                case R.id.tabs_my_page:
                    //判断当前显示的Fragment是否是MapsFragment
                    fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                    if (fragment instanceof MapsFragment) {
                        //如果当前显示的Fragment是MapsFragment的话，就先取得该Fragment的状态
                        mMapsFragmentSaveData = mMapsFragment.getCurrentState();
                    }

                    if ("".equals(AppUtil.User.USER_ID)) {
                        //说明还没有登陆，应该跳转到登录界面
                        LoginActivity.actionStartActivity(LauncherActivity.this);
                        overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
                    } else {
                        //说明已经登录，进入我的界面
                        if (null == mMeFragment) {
                            mMeFragment = new MeFragment();
                        }
                        fragment = mFragmentManager.findFragmentById(R.id.launcher_fragment_container);
                        if (null == fragment) {
                            mFragmentManager.beginTransaction().add(R.id.launcher_fragment_container, mMeFragment).commit();
                        } else {
                            mFragmentManager.beginTransaction().replace(R.id.launcher_fragment_container, mMeFragment).commit();
                        }
                    }

                    mTabsMyImg.setImageResource(R.drawable.my1);
                    mTabsMyText.setTextColor(ContextCompat.getColor(LauncherActivity.this, R.color.bhr_tabs_green));
                    break;
            }
        }
    }
}
