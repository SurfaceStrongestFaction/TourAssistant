package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.activity.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhouxiuya_activity_welcome);
        ImageView OpenLogo=(ImageView)findViewById(R.id.Openlogo);
        TextView OpenSay=(TextView)findViewById(R.id.Opensay);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.my);
        OpenLogo.setAnimation(animation);
        OpenSay.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LauncherActivity.actionStartActivity(WelcomeActivity.this);
                WelcomeActivity.this.finish();
                overridePendingTransition(R.anim.fade,R.anim.hold);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
