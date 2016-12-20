package com.daoshengwanwu.android.tourassistant.wangxiao;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil;
import com.daoshengwanwu.android.tourassistant.baihaoran.LauncherActivity;
import com.daoshengwanwu.android.tourassistant.jiangshengda.CircleImageView;
import com.daoshengwanwu.android.tourassistant.wangxiao.utils.HttpCallBackListener;
import com.daoshengwanwu.android.tourassistant.wangxiao.utils.HttpUtil;
import com.daoshengwanwu.android.tourassistant.wangxiao.utils.PrefParams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil.Group.GROUP_ID;
import static com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil.User.USER_ID;


public class LoginActivity extends Activity implements OnClickListener{
    private AuthInfo mAuthInfo;
    private ImageView btnweibo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    private UsersAPI mUsersAPI;
    private Button bt;
    private SharedPreferences s,s1;
    private String name1;
    private String pwd1;
    private String user_id;
    private String user_name;
    private String user_pwd;
    private JSONObject response1;
    public static String  qqresult;
    public static CircleImageView bimp;
    private final String xyurl = new String("http://10.7.88.30/user/getInformation");
            //("http://"+AppUtil.SharingServer.HOST2+":"+AppUtil.SharingServer.PORT2+"/user/getInformation");
    private String xyuser_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_login);
        initweibologin();
        initViews();
        initViews2();
        initViews3();
        initEvents();
        initEvents2();
        wechatlogin();
        s1 = getSharedPreferences("ty_user",Context.MODE_PRIVATE);
        name1 = s1.getString("name", "");
        pwd1 = s1.getString("pwd","");
        name.setText(name1);
        pwd.setText(pwd1);
    }
    Thread login = new Thread(){
        @Override
        public void run() {
            super.run();
            String result = "";
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                //登录
                    URL url = new URL("http://192.168.191.1/user/login");
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    out = new PrintWriter(con.getOutputStream());
                    out.print(user_name+"\n"+user_pwd);
                    out.flush();
                    //定义BufferedReader输入流读取URL响应
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null){
                        result += line+"\n";
                    }
                    String[]results = result.split("/n");
                    USER_ID = results[0];
                    GROUP_ID = results[1];
            }  catch (Exception e) {
                System.out.println("发送POST请求出现异常！" + e);
                e.printStackTrace();
            }  finally {
                try{
                    if (out != null){
                        out.close();
                    }
                    if (in != null){
                        in.close();
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    };


    private void initViews() {
        bt = (Button)findViewById(R.id.lg_bt2);

        btnweibo = (ImageView) findViewById(R.id.lg_weibo);

    }

    private void initEvents() {
        btnweibo.setOnClickListener(this);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public static void actionStartActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    /**
     * 进行微博授权初始化操作
     */
    private void initweibologin() {
        // 初始化授权类对象，将应用的信息保存
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lg_weibo:// SSO 授权, ALL IN ONE
                // 如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
                mSsoHandler.authorize(new AuthListener());
                break;
            case R.id.lg_bt:
                user_name = name.getText().toString();
                user_pwd = pwd.getText().toString();
                login.start();
            default:
                break;


        }
    }

    /**
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                getUserInfo();
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this,
                        mAccessToken);
                Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT)
                        .show();

            } else {
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
//	e		Toast.makeText(LoginActivity.this,
//					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
//					.show();
        }

    }

    /**
     * 获取用户个人信息
     */
    private void getUserInfo() {
        //获取用户信息接口
        mUsersAPI = new UsersAPI(LoginActivity.this, Constants.APP_KEY, mAccessToken);
        System.out.println("mUsersAPI  ----->   " + mUsersAPI.toString());

        //调用接口
        long uid = Long.parseLong(mAccessToken.getUid());
        System.out.println("--------------uid-------------->    " + uid);
        mUsersAPI.show(uid, mListener);//将uid传递到listener中，通过uid在listener回调中接收到该用户的json格式的个人信息
    }
    /**
     * 实现异步请求接口回调，并在回调中直接解析User信息
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {

            if (!TextUtils.isEmpty(response)) {
                //调用User#parse将JSON串解析成User对象
                User user = User.parse(response);
                String gender;
                if(user.gender.equals("f")) {
                    gender = "女";
                } else {
                    gender = "男";
                }
                String name = user.screen_name;
                String id = user.id;
                new Userty(id,name,gender);

                Toast.makeText(LoginActivity.this, "用户id： " + id + "\n用户昵称： " + name + "\n用户性别： " + gender, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * QQ第三方登录
     */
    private Tencent mTencent; //qq主操作对象
    private IUiListener iuilisten;
    private ImageView mNewLoginButton;
    public static String qqname;
    private String qqgender, qqid;


    private void initViews2() {
        mNewLoginButton = (ImageView) findViewById(R.id.lg_qq);
        mTencent = Tencent.createInstance("1105835094", getApplicationContext());
        mNewLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                System.out.println("登录");
                qqlogin();//登录

            }
        });
    }

    private void getTeamInfo() {
        if(!GROUP_ID.equals("")){
            RequestParams params1 = new RequestParams();
            params1.add("team_id", GROUP_ID);
            // 2.关闭弹出窗口
            //3.根据服务器返回值显示创建成功或失败的提示

            AsyncHttpClient gclient=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.add("user_id",xyuser_id);
            gclient.get(getApplicationContext(),xyurl,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    // System.out.print(response);
                    try {
                        String team_id=response.getString("team_id");
                        GROUP_ID = team_id;
                        Toast.makeText(LoginActivity.this, GROUP_ID, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
//            gclient.get(getApplicationContext(), xyurl,params1, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                    String teamInfo = new String(bytes);
//                 //   Toast.makeText(LoginActivity.this, teamInfo, Toast.LENGTH_SHORT).show();
//                    AlertDialog ad = new AlertDialog.Builder(LoginActivity.this).create();
//                    ad.setTitle("TeamInfo");
//                    ad.setMessage(teamInfo);
//                    ad.setButton("确定", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//                    ad.setButton2("取消", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//                    ad.show();
//
////                    JSONObject json = (JSONObject) teamInfo;
////                    JSONArray users = null;
////                    try {
////                        users = jsonObject.getJSONArray("你的key");
////                        for (int i = 0; i < users.size(); i++) {
////                            JSONObject attention = users.getJSONObject(i);
////                            String name = attention.getString("name");
////                            String account = attention.getString("account");
////
////                        }catch(JSONException e){
////                            e.printStackTrace();
////                        }
////
////                    }
//                }
//                @Override
//                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                    Toast.makeText(LoginActivity.this,"获取队伍信息失败",Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    public void getyh() {
        UserInfo userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
        IUiListener userInfoListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {

            }

            @Override
            public void onComplete(final Object arg0) {
                Message msg = new Message();
                msg.obj = arg0;
                msg.what = 0;
                new Thread() {

                    @Override
                    public void run() {
                        JSONObject json = (JSONObject) arg0;
                        //获取头像
                        if (json.has("figureurl")) {//判断字段是否为空
                            Bitmap bitmap = null;
                            try {
                                bitmap = getbitmap(json
                                        .getString("figureurl_qq_2"));
                            } catch (JSONException e) {

                            }
                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }

                }.start();

                if (msg.what == 0) {
                    JSONObject response = (JSONObject) msg.obj;
                            if (response.has("nickname")) {
                                try {
                                    qqgender = response.getString("gender").toString();
                                    qqname = response.getString("nickname").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                }

             //   Toast.makeText(LoginActivity.this, "用户id： " + qqid + "\n用户昵称： " + qqname + "\n用户性别： " + qqgender, Toast.LENGTH_SHORT).show();
                //建立连接
                AsyncHttpClient client = new AsyncHttpClient();
                String Url_add = "http://10.7.88.106:8080/qq/login";
                //获取参数
                RequestParams params = new RequestParams();
                params.add("qq",qqid);
                params.add("qq_name",qqname);
                params.add("sex",qqgender);
                //服务器获取参数
                client.get(getApplicationContext(), Url_add, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        qqresult = new String(bytes);
                        USER_ID = qqresult;
                        AppUtil.User.USER_NAME = qqname;
                        AppUtil.User.USER_GENDER = qqgender;
                        xyuser_id = qqresult;
                        Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_LONG).show();
                        getTeamInfo();
                        Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast.makeText(LoginActivity.this,"登录失败", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancel() {
            }
        };
        userInfo.getUserInfo(userInfoListener);
    }
    public void qqlogin() {

        iuilisten = new IUiListener() {

            @Override
            public void onCancel() {

            }

            public void onComplete(Object response) {

                if (null == response) {
                    System.out.println("返回为空登录失败");
                    return;
                }
                JSONObject jsonResponse = (JSONObject) response;
                if (null != jsonResponse && jsonResponse.length() == 0) {
                    System.out.println("返回为空登录失败");
                    return;
                }
                System.out.println("登录成功：=" + response);
                response1 = (JSONObject)response;
                try {
                    qqid =  response1.getString("openid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getyh() ;
            }

            @Override
            public void onError(UiError arg0) {

            }
        };
        //开始qq授权登录
        //要所有权限，不然会再次申请增量权限，这里不要设置成get_user_info,add_t
        mTencent.login(LoginActivity.this, "all", iuilisten);

    }
   Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                AppUtil.User.USER_IMG = bitmap;
            }
        }
    };

    public static Bitmap getbitmap(String imageUri) {
        Log.v("Util", "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v("Util", "image download finished." + imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Util", "getbitmap bmp fail---");
            return null;
        }
        return bitmap;
    }



    /**
     * 记住密码
     */

    private CheckBox cb;
    private EditText name;
    private EditText pwd;
    private Button lgbt;

    private void initViews3() {
        cb = (CheckBox) findViewById(R.id.lg_cb);
        name = (EditText)findViewById(R.id.lg_user);
        pwd = (EditText)findViewById(R.id.lg_pwd);
        lgbt = (Button)findViewById(R.id.lg_bt);
    }

    private void initEvents2() {
        lgbt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cb.isChecked()){
                    s = getSharedPreferences("ty_user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editer = s.edit();
                    editer.putString("name",name.getText().toString());
                    editer.putString("pwd",pwd.getText().toString());
                    editer.commit();
                    Toast.makeText(LoginActivity.this, "密码保存成功！",Toast.LENGTH_LONG).show();
                } else {
                    s = getSharedPreferences("ty_user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editer = s.edit();
                    editer.putString("name",name.getText().toString());
                    editer.putString("pwd","");
                    editer.commit();
                }
            }
        });
    }

    /**
     * 微信第三方登录
     */
    public static final String TAG1 = "WeChatLogin";

    private ImageView mLoginWeChat;
    private IWXAPI api;
    private ReceiveBroadCast receiveBroadCast;

    private void wechatlogin() {

        mLoginWeChat = (ImageView)findViewById(R.id.lg_wechat);
        mLoginWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weChatAuth();
            }
        });
    }

    private void weChatAuth() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, App.WX_APPID, true);
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login_duzun";
        api.sendReq(req);
    }

    public void getAccessToken() {
        SharedPreferences WxSp = this.getApplicationContext()
                .getSharedPreferences(PrefParams.spName, Context.MODE_PRIVATE);
        String code = WxSp.getString(PrefParams.CODE, "");
        final SharedPreferences.Editor WxSpEditor = WxSp.edit();
        Log.d(TAG1, "-----获取到的code----" + code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + App.WX_APPID
                + "&secret="
                + App.WX_APPSecret
                + "&code="
                + code
                + "&grant_type=authorization_code";
        Log.d(TAG1, "--------即将获取到的access_token的地址--------");
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {

                //解析以及存储获取到的信息
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG1, "-----获取到的json数据1-----" + jsonObject.toString());
                    String access_token = jsonObject.getString("access_token");
                    Log.d(TAG1, "--------获取到的access_token的地址--------" + access_token);
                    String openid = jsonObject.getString("openid");
                    String refresh_token = jsonObject.getString("refresh_token");
                    if (!access_token.equals("")) {
                        WxSpEditor.putString(PrefParams.ACCESS_TOKEN, access_token);
                        WxSpEditor.apply();
                    }
                    if (!refresh_token.equals("")) {
                        WxSpEditor.putString(PrefParams.REFRESH_TOKEN, refresh_token);
                        WxSpEditor.apply();
                    }
                    if (!openid.equals("")) {
                        WxSpEditor.putString(PrefParams.WXOPENID, openid);
                        WxSpEditor.apply();
                        getPersonMessage(access_token, openid);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(LoginActivity.this, "通过code获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPersonMessage(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG1, "------获取到的个人信息------" + jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(LoginActivity.this, "通过openid获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getAccessToken();
        }
    }

}
