package com.daoshengwanwu.android.tourassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends Activity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);
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

}
