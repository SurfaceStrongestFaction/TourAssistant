package com.daoshengwanwu.android.tourassistant.model;


import android.content.Context;

import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 白浩然 on 2016/12/27.
 */


public class User {
    private static final String REQUEST_URL = "http://" + AppUtil.JFinalServer.HOST
            + ":" + AppUtil.JFinalServer.PORT + "user/getInformation";
    private static final String PARAM_KEY_USER_ID = "user_id";

    private Context mApplicationContext = null;
    private String mUserId = null;
    private String mNickName = null;
    private String mSex = null;
    private String mTelephone = null;
    private String mHeadPicUrl = null;
    private String mTeamId = null;


    public User(Context applicationContext, String userId) {
        mApplicationContext = applicationContext;
        mUserId = userId;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }

    public String getHeadPicUrl() {
        return mHeadPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        mHeadPicUrl = headPicUrl;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public void setTelephone(String telephone) {
        mTelephone = telephone;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public void updateUserInfoFromServer(final OnUserInfoUpdatedListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add(PARAM_KEY_USER_ID, mUserId);
        client.get(mApplicationContext, REQUEST_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (null != response) {
                        mNickName = response.getString("nick_name");
                        mSex = response.getString("sex");
                        mTelephone = response.getString("tel");
                        mHeadPicUrl = response.getString("head_pic");
                        mTeamId = response.getString("team_id");

                        if (null != listener) {
                            listener.onUserInfoUpdated(User.this);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateUserInfoFromServer() {
        updateUserInfoFromServer(null);
    }


    public interface OnUserInfoUpdatedListener {
        void onUserInfoUpdated(User user);
    }
}
