package com.daoshengwanwu.android.tourassistant.shenyue;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.leekuo.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends BaseActivity {
    private static final String PICTUREEXTRA_ID = "PictureActivity.EXTRA_ID";
    List<PicgvItem> ls = new ArrayList<>();
    GridView picgv;
    PicturegvAdapter picturegvAdapter;
    ImageView backimg;
    private Context packageContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_picture);
        getSupportActionBar().hide();
        picgv = (GridView)findViewById(R.id.picture_picture_gv);
        getData();
        picturegvAdapter = new PicturegvAdapter(this, ls);
        picgv.setAdapter(picturegvAdapter);
        getIntent().getIntExtra(PICTUREEXTRA_ID, 0);
    }
    public static void actionStartActivity(Context packageContext, String id) {
        Intent i = new Intent(packageContext, PictureActivity.class);
        i.putExtra(PICTUREEXTRA_ID, id);
        packageContext.startActivity(i);
    }
    /**
     * 添加数据
     */
    private void getData(){
        ls.add(new PicgvItem(1, R.drawable.picture1));
        ls.add(new PicgvItem(1, R.drawable.picture2));
        ls.add(new PicgvItem(1, R.drawable.picture3));
        ls.add(new PicgvItem(1, R.drawable.picture4));
        ls.add(new PicgvItem(1, R.drawable.picture5));
        ls.add(new PicgvItem(1, R.drawable.picture6));
        ls.add(new PicgvItem(1, R.drawable.picture7));
        ls.add(new PicgvItem(1, R.drawable.picture8));
        ls.add(new PicgvItem(1, R.drawable.picture9));
        ls.add(new PicgvItem(1, R.drawable.picture10));
    }
    /**
     * 获取界面控件
     */
    private void getViews(){
        backimg = (ImageView) findViewById(R.id.picture_back_img);
    }
    /**
     * 注册事件监听器
     */
    private void setListener(){
        backimg.setOnClickListener(new MyListener());
    }
    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, PictureActivity.class);
        return i;
    }
}
