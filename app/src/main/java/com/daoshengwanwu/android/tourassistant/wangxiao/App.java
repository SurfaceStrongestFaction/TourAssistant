package com.daoshengwanwu.android.tourassistant.wangxiao;

import android.app.Application;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by asus on 2016/1/16.
 */
public class App extends Application {

    public static final String WX_APPID = "wxe495693dd346acad";
    public static final String WX_APPSecret = "4dd6186bc477d2077609106d224a6e31";


    private IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.registerApp(WX_APPID);
    }
}


