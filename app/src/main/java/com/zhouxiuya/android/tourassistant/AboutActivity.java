package com.zhouxiuya.android.tourassistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;

public class AboutActivity extends AppCompatActivity {
    private static final String EXTRA_USER_NAME = "AboutActivity.EXTRA_USER_NAME";

    private RelativeLayout about_evaluate;//给我评价
    private RelativeLayout about_version;//版权信息
    private RelativeLayout about_agreement;//软件许可使用协议
    private RelativeLayout about_illustration;//特别说明

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhouxiuya_activity_about);
        findViews();
        setListener();
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.about_evaluate:
                    evaluateDialogView(v);
                    break;
                case R.id.about_version:
            }
        }
    }
    private void setListener() {
        MyClickListener listener = new MyClickListener();
        about_evaluate.setOnClickListener(listener);
        about_version.setOnClickListener(listener);
        about_agreement.setOnClickListener(listener);
        about_illustration.setOnClickListener(listener);
    }

    private void evaluateDialogView(View source) {
        LinearLayout evaluate = (LinearLayout)getLayoutInflater()
                .inflate(R.layout.zhouxiuya_evaluate_dialog,null);
        new AlertDialog.Builder(this)
                //设置对话框标题
                .setTitle("评价")
                //设置对话框显示的view对象
                .setView(evaluate)
                //为对话框设置一个“确定”按钮
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                //为对话框设置一个“取消”按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                //创建并显示对话框
                .create().show();
    }
    public void findViews(){
        about_evaluate = (RelativeLayout)findViewById(R.id.about_evaluate);
        about_version = (RelativeLayout)findViewById(R.id.about_version);
        about_agreement = (RelativeLayout)findViewById(R.id.about_agreement);
        about_illustration = (RelativeLayout)findViewById(R.id.about_illustration);
    }
    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, AboutActivity.class);
        i.putExtra(EXTRA_USER_NAME, userName);

        return i;
    }
}
