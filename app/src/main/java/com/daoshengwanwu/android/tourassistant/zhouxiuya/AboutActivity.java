package com.daoshengwanwu.android.tourassistant.zhouxiuya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.daoshengwanwu.android.tourassistant.R;

public class AboutActivity extends AppCompatActivity {
    private static final String EXTRA_USER_NAME = "AboutActivity.EXTRA_USER_NAME";

    private RelativeLayout about_evaluate;//给我评价
    private RelativeLayout about_version;//版权信息
    private RelativeLayout about_agreement;//软件许可使用协议
    private RelativeLayout about_illustration;//特别说明
    private ImageView close;
    private ImageView about_back;

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
                        versionPopupView();
                    break;
                case R.id.about_agreement:
                    agreementPopupView();
                    break;
                case R.id.about_illustration:
                    illustrationPopupView();
                    break;
                case R.id.about_back:
                    //返回设置页面
                    //
                    //
                    //
                    //
                    //
                    break;

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
        final LinearLayout evaluate = (LinearLayout)getLayoutInflater()
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
                        //将输入的文字提交到服务器
                        //获取文字
                        String text = "";
                        EditText Et_evaluate = (EditText)evaluate.findViewById(R.id.Et_evaluate);
                        text = Et_evaluate.getText().toString();
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


    //弹出版权信息
    private void versionPopupView() {
        //加载R.layout.popup对应的界面布局文件
        View root = this.getLayoutInflater().inflate(R.layout.zhouxiuya_version_popup,null);
            //创建PopupWindow对象
//        final PopupWindow popup = new PopupWindow(root,720,720);
            final PopupWindow popup = new PopupWindow(root, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
               //将PopupWindow显示在指定位置
               popup.showAtLocation(about_version, Gravity.CENTER,0,0);

            root.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关闭popuppwindow
                    popup.dismiss();
                }
            });

    }

    //弹出协议信息
    private void agreementPopupView() {
        //加载R.layout.popup对应的界面布局文件
        View root = this.getLayoutInflater().inflate(R.layout.zhouxiuya_agreement_popup,null);
        //创建PopupWindow对象
        final PopupWindow popup = new PopupWindow(root,RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将PopupWindow显示在指定位置
        popup.showAtLocation(about_version, Gravity.CENTER,0,0);
        root.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭popuppwindow
                popup.dismiss();
            }
        });
    }

    //弹出特别说明
    private void illustrationPopupView() {
        //加载R.layout.popup对应的界面布局文件
        View root = this.getLayoutInflater().inflate(R.layout.zhouxiuya_illustration_popup,null);
        //创建PopupWindow对象
        final PopupWindow popup = new PopupWindow(root,RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将PopupWindow显示在指定位置
        popup.showAtLocation(about_version, Gravity.CENTER,0,0);
        root.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭popuppwindow
                popup.dismiss();
            }
        });
    }


    public void findViews(){
        about_evaluate = (RelativeLayout)findViewById(R.id.about_evaluate);
        about_version = (RelativeLayout)findViewById(R.id.about_version);
        about_agreement = (RelativeLayout)findViewById(R.id.about_agreement);
        about_illustration = (RelativeLayout)findViewById(R.id.about_illustration);
        close = (ImageView)findViewById(R.id.close);
        about_back = (ImageView)findViewById(R.id.about_back);
    }
    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, AboutActivity.class);
        i.putExtra(EXTRA_USER_NAME, userName);
        return i;
    }
}
