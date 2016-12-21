package com.daoshengwanwu.android.tourassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.activity.MyTeamActivity;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by LK on 2016/12/14.
 */
public class TeamFragment extends Fragment {
    private static final String KEY_BINDER = "TeamFragment.KEY_BINDER";
    private SharingService.SharingBinder mBinder;

    private RelativeLayout createTeam;
    private RelativeLayout joinTeam;
    private RelativeLayout myTeam;
    private RelativeLayout talk;
    private String str; //战队名
    private final String url = new String("http://"+AppUtil.SharingServer.HOST2+":"+AppUtil.SharingServer.PORT2+"/team/create");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.lk_fragment_team,container,false);
        createTeam=(RelativeLayout)view.findViewById(R.id.lk_createTeam);
        createTeam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
                final View myDialog=getActivity().getLayoutInflater().inflate(R.layout.lk_dialogxml,null);
                ab.setView(myDialog);
                final AlertDialog dialog =ab.show();
                //ab.create().show();
                Button conmmit=(Button)myDialog.findViewById(R.id.conmmit);
                conmmit.setOnClickListener(new View.OnClickListener() {//点击确定触发的事件
                    @Override
                    public void onClick(View v) {
                           EditText ed=(EditText) myDialog.findViewById(R.id.lk_dialogxml_EditText);
                            str = ed.getText().toString();//获取队伍名
                            Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
                            //1.向特定URL传输str
                           // String captain= AppUtil.User.USER_ID;
                            String captain= "2";
                            String members = captain;
                            AsyncHttpClient client=new AsyncHttpClient();
                            RequestParams params=new RequestParams();
                            params.add("team_name",str);
                            params.add("captain",captain);
                            params.add("members",members);
                            // 2.关闭弹出窗口
                            dialog.dismiss();
                            //3.根据服务器返回值显示创建成功或失败的提示
                            client.get(getActivity().getApplicationContext(), url,params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    String str1 = new String(bytes);
                                    Toast.makeText(getActivity(),str1,Toast.LENGTH_SHORT).show();
                                    if(!str1.equals("创建队伍失败,请重新创建")&&!str1.equals("只能创建一个队伍")){
                                        AppUtil.Group.GROUP_ID=str1;
                                        Toast.makeText(getActivity()," AppUtil.Group.GROUP_ID:"+AppUtil.Group.GROUP_ID,Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    Toast.makeText(getActivity(),"创建队伍失败",Toast.LENGTH_SHORT).show();
                                }

                            });
                        }

                });
                Button cancel=(Button)myDialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });




            }
        });
        joinTeam=(RelativeLayout)view.findViewById(R.id.lk_joinTeam);
        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myTeam=(RelativeLayout)view.findViewById(R.id.lk_myTeam);
        myTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MyTeamActivity.actionStartActivity(getActivity(), mBinder);
            }
        });
        talk=(RelativeLayout)view.findViewById(R.id.lk_talk);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Bundle data = getArguments();
        if (null != data) {
            mBinder = (SharingService.SharingBinder)data.getSerializable(KEY_BINDER);
        }

        return view;
    }

    public static TeamFragment newInstance(SharingService.SharingBinder binder) {
        TeamFragment teamFragment = new TeamFragment();

        Bundle data = new Bundle();
        data.putSerializable(KEY_BINDER, binder);

        teamFragment.setArguments(data);

        return teamFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
