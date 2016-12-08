package zhu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.daoshengwanwu.android.tourassistant.R;

public class SetActivity extends AppCompatActivity {
    private static final String EXTRA_USER_NAME = "SecondActivity.EXTRA_USER_NAME";
    private ImageView back;
    private RelativeLayout personaldata;
    private RelativeLayout model;
    private RelativeLayout aboutus;
    private Switch on_off;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhu_activity_set);

        //获取传递过来的数据：
        String news = getIntent().getStringExtra(EXTRA_USER_NAME);

        findView();
        setListener();
    }
    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, SetActivity.class);
        i.putExtra(EXTRA_USER_NAME, userName);
        return i;
    }
    private void setListener() {
        MyListener myListener=new MyListener();
        personaldata.setOnClickListener(myListener);
        model.setOnClickListener(myListener);
        aboutus.setOnClickListener(myListener);
    }

    private void findView() {
        personaldata=(RelativeLayout)findViewById(R.id.rl_set_personaldata);
        model=(RelativeLayout)findViewById(R.id.rl_set_model);
        aboutus=(RelativeLayout)findViewById(R.id.rl_set_aboutus);
        back=(ImageView)findViewById(R.id.iv_set_back);
        on_off=(Switch)findViewById(R.id.swi_set_on_off);
    }
    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_set_personaldata:
                    Intent i = PersonalDataActivity.newIntent(SetActivity.this, "消息");
                    startActivity(i);
                    break;
                case R.id.rl_set_model:

                    break;
                case R.id.rl_set_aboutus:

                    break;
                case R.id.swi_set_on_off:

                    break;
            }
        }
    }
}
