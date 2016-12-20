package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

public class ViewspotActivity extends BaseActivity {

    private static final String VIEWSPOTEXTRA_ID = "ViewspotActivity.EXTRA_ID";
    private ImageView backimg;
    private RelativeLayout picturerl;
    private ImageView showimg;
    private TextView nametv;
    private TextView picturenumtv;
    private ImageView star1img;
    private ImageView star2img;
    private ImageView star3img;
    private ImageView star4img;
    private ImageView star5img;
    private TextView pricenumtv;
    private TextView timeinftv;
    private TextView introducetv;
    private TextView gotv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_viewspot);
        getIntent().getIntExtra(VIEWSPOTEXTRA_ID, 0);
        getSupportActionBar().hide();
    }
    public static void actionStartActivity(Context packageContext, int id) {
        Intent i = new Intent(packageContext, ViewspotActivity.class);
        i.putExtra(VIEWSPOTEXTRA_ID, id);
        packageContext.startActivity(i);
    }
    /**
     * 获取界面控件
     */
    private void getViews(){
        backimg = (ImageView)findViewById(R.id.viewspot_back_img);
        picturerl = (RelativeLayout)findViewById(R.id.viewspot_picture_rl);
        showimg = (ImageView)findViewById(R.id.viewspot_show_img);
        nametv = (TextView)findViewById(R.id.viewspot_name_tv);
        picturenumtv = (TextView)findViewById(R.id.viewspot_picturenum_tv);
        star1img = (ImageView)findViewById(R.id.viewspot_star1_img);
        star2img = (ImageView)findViewById(R.id.viewspot_star2_img);
        star3img = (ImageView)findViewById(R.id.viewspot_star3_img);
        star4img = (ImageView)findViewById(R.id.viewspot_star4_img);
        star5img = (ImageView)findViewById(R.id.viewspot_star5_img);
        pricenumtv = (TextView)findViewById(R.id.viewspot_picturenum_tv);
        timeinftv = (TextView)findViewById(R.id.viewspot_timeinf_tv);
        introducetv = (TextView)findViewById(R.id.viewspot_introduce_tv);
        gotv = (TextView)findViewById(R.id.viewspot_go_tv);
    }
    /**
     * 注册事件监听器
     */
    private void setListener(){
        backimg.setOnClickListener(new MyListener());
        picturerl.setOnClickListener(new MyListener());
        gotv.setOnClickListener(new MyListener());
    }
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.viewspot_back_img:

                    break;
                case R.id.viewspot_picture_rl:

                    break;
                case R.id.viewspot_go_tv:

                    break;
            }
        }
    }
}