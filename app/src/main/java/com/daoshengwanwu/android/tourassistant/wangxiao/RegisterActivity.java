package com.daoshengwanwu.android.tourassistant.wangxiao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.leekuo.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_register1);
        findViews();
        setListener();
    }

    private void findViews() {
        bt = (Button)findViewById(R.id.rs_phoners);

    }
    private void setListener() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                startActivity(intent);
            }
        });

    }

    public static void actionStartActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

}
