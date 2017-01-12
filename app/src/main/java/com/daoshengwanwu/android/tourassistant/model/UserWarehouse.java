package com.daoshengwanwu.android.tourassistant.model;


import android.content.Context;

import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 白浩然 on 2016/12/27.
 */


public class UserWarehouse {
    private static UserWarehouse sInstance = null;
    private static final String REQUEST_TEAM_ID_URL = "http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/team/getInformation";
    private static final String PARAM_KEY_TEAM_ID = "team_id";

    private List<User> mUsers = new ArrayList<>();
    private Context mApplicationContext = null;
    private int mCount = 0;
    private boolean mIsDataUpdated = false;


    public static UserWarehouse getInstance(Context applicationContext) {
        if (null == sInstance) {
            sInstance = new UserWarehouse(applicationContext);
        }

        return sInstance;
    }

    private UserWarehouse(Context applicationContext) {
        mApplicationContext = applicationContext;
    }; //私有构造函数

    public User getUserById(String userId) {
        for (User user : mUsers) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }

        return null;
    }

    public void clearUsers() {
        mUsers.clear();
    }

    public int getUserNum() {
        return mUsers.size();
    }

    public void updateUsersInfo(String teamId, final OnUsersInfoUpdatedListener listener) {
        mIsDataUpdated = false;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add(PARAM_KEY_TEAM_ID, teamId);
        client.get(mApplicationContext, REQUEST_TEAM_ID_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String[] members = response.getString("members").split(",");
                    updateUsersInfo(generateMemIdsList(members), listener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateUsersInfo(List<String> userIdList, final OnUsersInfoUpdatedListener listener) {
        if (null == userIdList) {
            return;
        }

        mIsDataUpdated = false;

        mCount = 0;
        mUsers.clear();
        final int userNum = userIdList.size();
        for (String user_id : userIdList) {
            User user = new User(mApplicationContext, user_id);
            mUsers.add(user);
            user.updateUserInfoFromServer(new User.OnUserInfoUpdatedListener() {
                @Override
                public void onUserInfoUpdated(User user) {
                    mCount++ ;
                    if (mCount == userNum) {
                        mIsDataUpdated = true;

                        if (null != listener) {
                            listener.onUsersInfoUpdated(UserWarehouse.this);
                        }
                    }
                }
            });
        }

        if (userNum == 0) {
            mIsDataUpdated = true;
        }
    }

    private List<String> generateMemIdsList(String[] members_id_array) {
        List<String> list = new ArrayList<>();

        for (String member_id : members_id_array) {
            list.add(member_id);
        }

        return list;
    }

    public List<User> getUsers() {
        return mUsers;
    }


    public interface OnUsersInfoUpdatedListener {
        void onUsersInfoUpdated(UserWarehouse userWarehouse);
    }
}
