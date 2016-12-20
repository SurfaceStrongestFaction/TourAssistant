package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daoshengwanwu.android.tourassistant.R;

public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caizihuan_activity_chat);
    }
    public static void startChatActivity(Context c){
        Intent i = new Intent(c,ChatActivity.class);
        c.startActivity(i);
    }
}
