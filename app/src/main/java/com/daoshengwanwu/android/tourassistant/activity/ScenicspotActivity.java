package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.activity.BaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView pricenumtv;
    private TextView timeinf;
    private TextView introducetv;
    private TextView gotv;
    private TextView findtv;
    private Drawable drawable;
    String recommendimgs;
    String cnname;
    String recommendindex;
    String position;
    String price;
    String time;
    String introduction;
    String imgs;
    String[] urls;
    String scenicspotid;
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_scenicspot);
        getViews();
        setListener();
        synhttprequest();
        getintent();
    }
    public  void getintent(){
        Intent i = getIntent();
        scenicspotid = i.getStringExtra(SCENICSPOTEXTRA_ID);
    }
    public void synhttprequest(){
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = "http://139.199.28.184/spot/getrecommend";

        RequestParams params = new RequestParams();
        params.add("id", scenicspotid);
        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    recommendimgs = response.getString("recommend_imgs");
                    cnname = response.getString("cn_name");
                    recommendindex = response.getString("recommend_index");
                    position = response.getString("position");
                    price = response.getString("price");
                    time = response.getString("time");
                    introduction = response.getString("introduction");
                    imgs = response.getString("imgs");
                    urls = imgs.split(",");
                    num = urls.length;
//                    System.out.println(recommendimgs);
//                    System.out.println(cnname);
//                    System.out.println(recommendindex);
//                    System.out.println(position);
//                    System.out.println(price);
//                    System.out.println(time);
//                    System.out.println(introduction);
//                    System.out.println(imgs);
//                    System.out.println(urls);
//                    System.out.println(num);
                    setViews();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        });
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
        pricenumtv = (TextView)findViewById(R.id.scenicspot_pricenum_tv);
        timeinf = (TextView)findViewById(R.id.scenicspot_timeinf_tv);
        introducetv = (TextView)findViewById(R.id.scenicspot_introduce_tv);
        gotv = (TextView)findViewById(R.id.scenicspot_go_tv);
        findtv = (TextView)findViewById(R.id.scenicspot_find_tv);
    }
    /**
     * 给界面控件赋值
     */
    private void setViews(){
        nametv.setText(cnname);
        positiontv.setText(position);
        picturenumtv.setText(num+"张");
        pricenumtv.setText(price);
        timeinf.setText(time);
        introducetv.setText(introduction);
        int i=0;
        if (recommendindex.equals("0")){
            i=0;
        }
        if (recommendindex.equals("1")){
            i=1;
        }
        if (recommendindex.equals("2")){
            i=2;
        }
        if (recommendindex.equals("3")){
            i=3;
        }
        if (recommendindex.equals("4")){
            i=4;
        }
        if (recommendindex.equals("5")){
            i=5;
        }
        switch (i){
            case 0:
                star1img.setImageResource(R.drawable.star1);
                star2img.setImageResource(R.drawable.star1);
                star3img.setImageResource(R.drawable.star1);
                star4img.setImageResource(R.drawable.star1);
                star5img.setImageResource(R.drawable.star1);
                break;
            case 1:
                star2img.setImageResource(R.drawable.star1);
                star3img.setImageResource(R.drawable.star1);
                star4img.setImageResource(R.drawable.star1);
                star5img.setImageResource(R.drawable.star1);
                break;
            case 2:
                star3img.setImageResource(R.drawable.star1);
                star4img.setImageResource(R.drawable.star1);
                star5img.setImageResource(R.drawable.star1);
                break;
            case 3:
                star4img.setImageResource(R.drawable.star1);
                star5img.setImageResource(R.drawable.star1);
                break;
            case 4:
                star5img.setImageResource(R.drawable.star1);
                break;
            default:
                break;

        }
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
                    LauncherActivity.actionStartActivity(ScenicspotActivity.this);
                    break;
                case R.id.scenicspot_picture_rl:
                    PictureActivity.actionStartActivity(ScenicspotActivity.this, scenicspotid);
                    break;
                case R.id.scenicspot_go_tv:

                    break;
                case R.id.scenicspot_find_tv:

                    break;
            }
        }
    }
}
