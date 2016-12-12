package com.daoshengwanwu.android.tourassistant.shenyue;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.leekuo.BaseActivity;

public class ScenicspotActivity extends BaseActivity {
    private static final String SCENICSPOTEXTRA_ID = "ScenicspotActivity.EXTRA_ID";
    private ImageView backimg;
    private RelativeLayout picturerl;
    private ImageView showimg;
    private TextView nametv;
    private TextView positiontv;
    private TextView picturenumtv;
    private ImageView star1img;
    private ImageView star2img;
    private ImageView star3img;
    private ImageView star4img;
    private ImageView star5img;
    private TextView introducetv;
    private TextView gotv;
    private TextView findtv;
    private Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_scenicspot);
        getSupportActionBar().hide();
        getViews();
        setListener();
        getIntent().getIntExtra(SCENICSPOTEXTRA_ID, 0);
    }

    public static Intent actionStartActivity(Context packageContext, String id) {
        Intent i = new Intent(packageContext, ScenicspotActivity.class);
        i.putExtra(SCENICSPOTEXTRA_ID, id);

        return i;
    }
    /**
     * 获取界面控件
     */
    private void getViews(){
        backimg = (ImageView)findViewById(R.id.scenicspot_back_img);
        picturerl = (RelativeLayout)findViewById(R.id.scenicspot_picture_rl);
        showimg = (ImageView)findViewById(R.id.scenicspot_show_img);
        nametv = (TextView)findViewById(R.id.scenicspot_name_tv);
        positiontv = (TextView)findViewById(R.id.scenicspot_position_tv);
        picturenumtv = (TextView)findViewById(R.id.scenicspot_picturenum_tv);
        star1img = (ImageView)findViewById(R.id.scenicspot_star1_img);
        star2img = (ImageView)findViewById(R.id.scenicspot_star2_img);
        star3img = (ImageView)findViewById(R.id.scenicspot_star3_img);
        star4img = (ImageView)findViewById(R.id.scenicspot_star4_img);
        star5img = (ImageView)findViewById(R.id.scenicspot_star5_img);
        introducetv = (TextView)findViewById(R.id.scenicspot_introduce_tv);
        gotv = (TextView)findViewById(R.id.scenicspot_go_tv);
        findtv = (TextView)findViewById(R.id.scenicspot_find_tv);
    }
    /**
     * 注册事件监听器
     */
    private void setListener(){
        backimg.setOnClickListener(new MyListener());
        picturerl.setOnClickListener(new MyListener());
        gotv.setOnClickListener(new MyListener());
        findtv.setOnClickListener(new MyListener());
    }
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.scenicspot_back_img:

                    break;
                case R.id.scenicspot_picture_rl:

                    break;
                case R.id.scenicspot_go_tv:

                    break;
                case R.id.scenicspot_find_tv:

                    break;
            }
        }
    }
}
