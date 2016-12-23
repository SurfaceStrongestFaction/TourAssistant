package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daoshengwanwu.android.tourassistant.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

public class ConversationActivity extends AppCompatActivity {
    EaseConversationListFragment conversationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        conversationFragment=new EaseConversationListFragment();
        conversationFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(ConversationActivity.this, ECChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_conversation, conversationFragment).commit();

    }
}
