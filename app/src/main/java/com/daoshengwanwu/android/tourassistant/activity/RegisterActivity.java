package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.daoshengwanwu.android.tourassistant.utils.AppUtil.User.USER_ID;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button bt;
    private Button registbt;
    private String user_name="";
    private String user_pwd="";
    private String user_repwd="";
    private String registresult="";
    private EditText email;
    private EditText pwd;
    private EditText repwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_register1);
        initViews();
        findViews();
        setListener();
    }
    public void synhttprequestlogin(){
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = "http://"+AppUtil.JFinalServer.HOST+":"+ AppUtil.JFinalServer.PORT+ "/user/regist";
        RequestParams params = new RequestParams();
        params.add("user_name", user_name);
        params.add("user_pwd", user_pwd);
        params.add("user_repwd", user_repwd);
        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String result = response.getString("result");
                    Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
                    if(result.equals("注册成功")){
                        LoginActivity.actionStartActivityRegister(RegisterActivity.this, user_name, user_pwd);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    private void findViews() {
        bt = (Button)findViewById(R.id.rs_phoners);
        registbt = (Button)findViewById(R.id.rs2_bt);
    }
    private void setListener() {
        registbt.setOnClickListener(this);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                finish();
            }
        });

    }

    public static void actionStartActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rs2_bt:
//                Toast.makeText(this, email.getText().toString(), Toast.LENGTH_LONG).show();
                user_name = email.getText().toString();
                user_pwd = pwd.getText().toString();
                user_repwd = repwd.getText().toString();
                synhttprequestlogin();
            default:
                break;


        }
    }

    private void initViews() {
        email = (EditText)findViewById(R.id.rs2_yx);
        pwd = (EditText)findViewById(R.id.rs_et2);
        repwd = (EditText)findViewById(R.id.rs_et3);
    }

}
