package com.daoshengwanwu.android.tourassistant.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.activity.ConversationActivity;
import com.daoshengwanwu.android.tourassistant.activity.MyTeamActivity;
import com.daoshengwanwu.android.tourassistant.activity.ScanActivity;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.hyphenate.chat.EMClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LK on 2016/12/14.
 */
public class TeamFragment extends Fragment {
    private static final String KEY_BINDER = "TeamFragment.KEY_BINDER";
    private static final int WHAT_ALERT_DIALOG = 1024;
    private static final String MSG_DATA_TEAM_ID = "TeamFragment.MSG_DATA_TEAM_ID";
    private SharingService.SharingBinder mBinder;

    private RelativeLayout createTeam;
    private RelativeLayout joinTeam;
    private RelativeLayout myTeam;
    private RelativeLayout talk;
    private String str; //战队名
    private final String url = new String("http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/team/create");
    private String[] memberids;
    public  String teamname;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_ALERT_DIALOG:
                    String teamid = msg.getData().getString(MSG_DATA_TEAM_ID);
                    Toast.makeText(getActivity(), "teamid:" + teamid, Toast.LENGTH_SHORT).show();
                    alertDialog(teamid);
                    break;
                default: break;
            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lk_fragment_team, container, false);
        createTeam = (RelativeLayout) view.findViewById(R.id.lk_createTeam);
        createTeam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),AppUtil.Group.GROUP_ID,Toast.LENGTH_LONG).show();
                if (AppUtil.Group.GROUP_ID.equals("null") || AppUtil.Group.GROUP_ID.isEmpty()){//还未创建队伍
                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                final View myDialog = getActivity().getLayoutInflater().inflate(R.layout.lk_dialogxml, null);
                ab.setView(myDialog);
                final AlertDialog dialog = ab.show();
                //ab.create().show();
                Button conmmit = (Button) myDialog.findViewById(R.id.conmmit);
                conmmit.setOnClickListener(new View.OnClickListener() {//点击确定触发的事件
                    @Override
                    public void onClick(View v) {
                        EditText ed = (EditText) myDialog.findViewById(R.id.lk_dialogxml_EditText);
                        str = ed.getText().toString();//获取队伍名
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                        //1.向特定URL传输str
                        String captain = AppUtil.User.USER_ID;
                        //    String captain= "2";
                        String members = captain;
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.add("team_name", str);
                        params.add("captain", captain);
                        params.add("members", members);
                        // 2.关闭弹出窗口
                        dialog.dismiss();
                        //3.根据服务器返回值显示创建成功或失败的提示
                        client.get(getActivity().getApplicationContext(), url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                String str1 = new String(bytes);
                                AppUtil.Group.GROUP_CAPTIAN = AppUtil.User.USER_ID;
                                AppUtil.Group.GROUP_ID = str1;
                                AppUtil.Group.GROUP_NAME = str;
                                String text = "id:\n" + AppUtil.Group.GROUP_ID + "captian:" + AppUtil.Group.GROUP_CAPTIAN;
                                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                if (!str1.equals("创建队伍失败,请重新创建") && !str1.equals("只能创建一个队伍")) {
                                    AppUtil.Group.GROUP_ID = str1;
                                    Toast.makeText(getActivity(), " AppUtil.Group.GROUP_ID:" + AppUtil.Group.GROUP_ID, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                Toast.makeText(getActivity(), "创建队伍失败", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }

                });
                Button cancel = (Button) myDialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }else{//否则弹出框你已加入队伍或创建队伍不能创建

                       AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
                       ab.setTitle("请求失败");
                       ab.setMessage("你已经加入或创建了队伍");
                       ab.create().show();
                }

            }
        });

        joinTeam = (RelativeLayout) view.findViewById(R.id.lk_joinTeam);
        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.Group.GROUP_ID.equals("null") || AppUtil.Group.GROUP_ID.isEmpty()) {//还未加入队伍
                    Intent intent = new Intent(getActivity(), ScanActivity.class);
                    startActivityForResult(intent, 0);
                }else{

                    AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
                    ab.setTitle("请求失败");
                    ab.setMessage("你已经加入或创建了队伍");
                    ab.create().show();
                }

            }
        });
        myTeam = (RelativeLayout) view.findViewById(R.id.lk_myTeam);
        myTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtil.Group.GROUP_ID == null || AppUtil.Group.GROUP_ID.equals("") || AppUtil.Group.GROUP_ID.equals("null")) {
                    Toast.makeText(getActivity(),"您还未加入队伍", Toast.LENGTH_SHORT).show();
                }else {
                    MyTeamActivity.actionStartActivity(getActivity(), mBinder);
                }
            }
        });
        talk = (RelativeLayout) view.findViewById(R.id.lk_talk);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(AppUtil.User.USER_ID)) {
                    Intent intent = new Intent(getActivity(), ConversationActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Bundle data = getArguments();
        if (null != data) {
            mBinder = (SharingService.SharingBinder) data.getSerializable(KEY_BINDER);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String addTeam_id = data.getStringExtra("RESULT_QRCODE_STRING");
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 0:
                addTeam(addTeam_id);
                break;
            default:
                break;
        }
    }

    private void addTeam(final String teamid) {
        getTeamname(teamid);
    }

    private void alertDialog(final String teamid) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
        dialog.setTitle("确认加入队伍");
        dialog.setMessage("您当前选择的队伍：" + AppUtil.Group.GROUP_NAME);
        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncHttpClient gclient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add("team_id", teamid);
                params.add("user_id", AppUtil.User.USER_ID);
                gclient.get(getActivity().getApplicationContext(), AppUtil.JFinalServer.xyurl3, params, new JsonHttpResponseHandler()  {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("result");
                            AppUtil.Group.GROUP_ID = teamid;
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtil.Group.GROUP_NAME = "";
            }
        });
        dialog.create().show();
    }

    private void getTeamname(final String teamid) {
            AsyncHttpClient gclient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("team_id", teamid);
            gclient.get(getActivity().getApplicationContext(), AppUtil.JFinalServer.xyurl2, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        AppUtil.Group.GROUP_NAME = response.getString("name");
                        Message msg = new Message();

                        msg.what = WHAT_ALERT_DIALOG;
                        Bundle data = new Bundle();
                        data.putString(MSG_DATA_TEAM_ID, teamid);
                        msg.setData(data);
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
    }

}
