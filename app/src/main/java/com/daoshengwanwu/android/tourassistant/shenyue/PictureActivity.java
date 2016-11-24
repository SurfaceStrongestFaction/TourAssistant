package com.daoshengwanwu.android.tourassistant.shenyue;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import com.daoshengwanwu.android.tourassistant.R;
import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    List<PicgvItem> ls = new ArrayList<>();
    GridView picgv;
    PicturegvAdapter picturegvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_picture);
        getSupportActionBar().hide();
        picgv = (GridView)findViewById(R.id.picture_picture_gv);
        getData();
        picturegvAdapter = new PicturegvAdapter(this, ls);
        picgv.setAdapter(picturegvAdapter);
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

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, PictureActivity.class);
        return i;
    }
}
