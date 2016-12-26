package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.item.picgv.PicgvItem;
import com.daoshengwanwu.android.tourassistant.adapter.PicturegvAdapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends BaseActivity {
    private static final String PICTUREEXTRA_URL = "PictureActivity.EXTRA_URL";
    private static final String PICTUREEXTRA_ID = "PictureActivity.EXTRA_ID";
    List<PicgvItem> ls = new ArrayList<>();
    GridView picgv;
    PicturegvAdapter picturegvAdapter;
    ImageView backimg;
    private Context packageContext;
    private String url;
    private Drawable drawable;
    private List<Drawable> drawls;
    String[] urls;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_picture);
        drawls =new ArrayList<Drawable>();
        picgv = (GridView)findViewById(R.id.picture_picture_gv);
        getintent();
        getData();
        System.out.println(ls);
        getViews();
        setListener();
    }
    public  void getintent(){
        Intent i = getIntent();
        url = i.getStringExtra(PICTUREEXTRA_URL);
        id = i.getStringExtra(PICTUREEXTRA_ID);
    }
    public static void actionStartActivity(Context packageContext, String url, String id) {
        Intent i = new Intent(packageContext, PictureActivity.class);
        i.putExtra(PICTUREEXTRA_URL, url);
        i.putExtra(PICTUREEXTRA_ID, id);
        packageContext.startActivity(i);
    }
    /**
     * 添加数据
     */
    String u;
    private void getData(){
        r.start();

//        ls.add(new PicgvItem(1, R.drawable.picture1));
//        ls.add(new PicgvItem(1, R.drawable.picture2));
//        ls.add(new PicgvItem(1, R.drawable.picture3));
//        ls.add(new PicgvItem(1, R.drawable.picture4));
//        ls.add(new PicgvItem(1, R.drawable.picture5));
//        ls.add(new PicgvItem(1, R.drawable.picture6));
//        ls.add(new PicgvItem(1, R.drawable.picture7));
//        ls.add(new PicgvItem(1, R.drawable.picture8));
//        ls.add(new PicgvItem(1, R.drawable.picture9));
//        ls.add(new PicgvItem(1, R.drawable.picture10));
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    picturegvAdapter = new PicturegvAdapter(PictureActivity.this, ls);
                    picgv.setAdapter(picturegvAdapter);
                    break;
            }

        }
    };
    Thread r = new Thread(){
        @Override
                public void run() {
            try {
                urls = url.split("\\|\\|\\|");
                for (int i = 0; i<urls.length; i++) {
                    u = urls[i];
                    System.out.println("haha+" + u);
                    URL url1 = new URL(u);
                    HttpURLConnection con = null;
                    con = (HttpURLConnection) url1.openConnection();
                    InputStream is = con.getInputStream();
                    drawable = Drawable.createFromStream(is, null);
                    drawls.add(drawable);
                }
                for (int i = 0; i<drawls.size(); i++){
                    ls.add(new PicgvItem(1, drawls.get(i)));
                    System.out.println("ll"+drawls.get(i));
                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    };
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
           switch (v.getId()){
               case R.id.picture_back_img:
                   Intent i = ScenicspotActivity.actionStartActivity(PictureActivity.this, id);
                   startActivity(i);
                   break;
           }
        }
    }
}
