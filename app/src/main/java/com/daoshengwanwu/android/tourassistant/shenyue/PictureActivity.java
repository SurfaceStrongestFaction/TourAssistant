package com.daoshengwanwu.android.tourassistant.shenyue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    List<Integer> ls = new ArrayList<>();
    GridView picgv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenyue_activity_picture);
        picgv = (GridView)findViewById(R.id.picture_picture_gv);
        getData();

    }
    /**
     * 添加数据
     */
    private void getData(){
        ls.add(R.drawable.picture1);
        ls.add(R.drawable.picture2);
        ls.add(R.drawable.picture3);
        ls.add(R.drawable.picture4);
        ls.add(R.drawable.picture5);
        ls.add(R.drawable.picture6);
        ls.add(R.drawable.picture7);
        ls.add(R.drawable.picture8);
        ls.add(R.drawable.picture9);
        ls.add(R.drawable.picture10);

    }
}
