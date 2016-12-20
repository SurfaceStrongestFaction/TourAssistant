package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

public class SearchMemberActivity extends BaseActivity {
    private TextView cancelTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caizihuan_activity_search_member);
        cancelTextView = (TextView)findViewById(R.id.Tv_activitysearchmember_cancel);
        cancelTextView.setOnClickListener(cancelListener);
    }
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ChatTableActivity.startChatTableActivity(SearchMemberActivity.this);
        }
    };
    public static void startSearchMemberActivity(Context c){
        Intent i = new Intent(c,SearchMemberActivity.class);
        c.startActivity(i);
    }


}
