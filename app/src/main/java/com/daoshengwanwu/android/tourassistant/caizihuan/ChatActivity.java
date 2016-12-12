package com.daoshengwanwu.android.tourassistant.caizihuan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daoshengwanwu.android.tourassistant.R;

public class ChatActivity extends Activity {

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
