package com.daoshengwanwu.android.tourassistant.caizihuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

public class SearchMemberActivity extends Activity {
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
            Intent intent = new Intent(SearchMemberActivity.this,ChatTableActivity.class);
        }
    };
}
