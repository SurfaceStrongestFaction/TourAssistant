package com.daoshengwanwu.android.tourassistant.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.activity.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhouxiuya_activity_welcome);
        //加载动画
        ImageView pic=(ImageView)findViewById(R.id.welcome_pic);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.my);
        pic.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LauncherActivity.actionStartActivity(WelcomeActivity.this);
                WelcomeActivity.this.finish();
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
