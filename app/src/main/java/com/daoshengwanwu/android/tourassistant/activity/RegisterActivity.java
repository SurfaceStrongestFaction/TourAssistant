package com.daoshengwanwu.android.tourassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daoshengwanwu.android.tourassistant.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button bt;
    private String user_name;
    private String user_pwd;
    private String user_repwd;
    private String registresult;
    private EditText email;
    private EditText pwd;
    private EditText repwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_register1);
        findViews();
        setListener();
    }
    Thread regist = new Thread(){
        @Override
        public void run() {
            super.run();
            String result = "";
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                //注册
                URL url = new URL("http://192.168.191.1/user/regist");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                out = new PrintWriter(con.getOutputStream());
                out.print(user_name+"\n"+user_pwd+"\n"+user_repwd);
                out.flush();
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = in.readLine()) != null){
                    result += "\n" +line;
                }
                registresult = result;
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
    private void findViews() {
        bt = (Button)findViewById(R.id.rs_phoners);

    }
    private void setListener() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                startActivity(intent);
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
                user_name = email.getText().toString();
                user_pwd = pwd.getText().toString();
                user_repwd = repwd.getText().toString();
                regist.start();
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
