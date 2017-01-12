package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ECChatActivity extends AppCompatActivity {
    private final String xyurl1="http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/team/creatChat";

    // 当前聊天的 ID
    private String mChatId;
    private EaseChatFragment chatFragment;
    /** group中一开始就有的成员 */
    private List<String> existMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhu_activity_chat);

        // 这里直接使用EaseUI封装好的聊天界面
        chatFragment = new EaseChatFragment();
        // 将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());
        Log.i("zhu", "ECChat: "+getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();

        save();



        //添加新成员按钮监听
        EaseChatFragment.EaseChatFragmentHelper myCharHelper = new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                //设置消息拓展
                String name= AppUtil.User.USER_NAME;
                String myHeadImg=AppUtil.User.USER_IMG;
                message.setAttribute("myHeadImg",myHeadImg);
                message.setAttribute("name",name);
            }
            //点击标题栏右侧图表
            @Override
            public void onEnterToChatDetails() {
                //如果是群聊获取群聊成Members
                String id=getIntent().getExtras().getString("userId");
                EMGroup group = EMClient.getInstance().groupManager().getGroup(id);
                existMembers = group.getMembers();
                //Log.i("zhu", "test ");
                //Log.i("zhu", "test "+existMembers.size()+existMembers.contains("zhu1024")+existMembers.contains("zhu1001"));

            }

            @Override
            public void onAvatarClick(String username) {
                if(!username.equals(AppUtil.User.USER_ID)) {
                    Intent intent = new Intent(ECChatActivity.this, ECChatActivity.class);
                    intent.putExtra("userId", username);
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                    startActivity(intent);
                }
            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        };
        chatFragment.setChatFragmentListener(myCharHelper);


        initView();
    }

    private void save() {
        //保存myGroupId
        AsyncHttpClient gclient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("team_id", AppUtil.Group.GROUP_ID);
        params.add("chat_team_id", AppUtil.Group.CHAT_TEAM_ID);
        gclient.get(getApplicationContext(),xyurl1,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(!TextUtils.isEmpty(response.getString("chat_team_id "))){
                        Log.i("zhu", "onSuccess: "+"保存AppUtil.Group.CHAT_TEAM_ID成功");
                    };
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
