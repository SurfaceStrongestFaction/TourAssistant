package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daoshengwanwu.android.tourassistant.R;

public class Register2Activity extends BaseActivity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_register2);
        findViews();
        setListener();
    }

    private void findViews() {
        bt = (Button)findViewById(R.id.button3);

    }
    private void setListener() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register2Activity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                finish();
            }
        });

    }

    public static void actionStartActivity(Context context) {
        context.startActivity(new Intent(context, Register2Activity.class));
    }

}
