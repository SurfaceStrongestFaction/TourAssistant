package com.daoshengwanwu.android.tourassistant.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.adapter.HomeSpotAdapter;
import com.daoshengwanwu.android.tourassistant.model.HomeListSpotItem;
import com.daoshengwanwu.android.tourassistant.model.SpotItemBase;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.utils.DisplayUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SuperHomeFragment extends Fragment {
    private static final String TAG = "SuperHomeFragment";
    private RecyclerView mRecyclerView;
    private List<SpotItemBase> mData = new ArrayList<>();

    private static final int WHAT_NOTIFY = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_NOTIFY: {
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                } break;
                default: break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.baihaoran_fragment_super_home, container, false);

        //初始化标题栏
        initTitleBar(v);

        //配置RecyclerView
        mRecyclerView = (RecyclerView)v.findViewById(R.id.bhr_home_body); //获取RecyclerView的引用
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //为RecyclerView设置LayoutManager
        mRecyclerView.setAdapter(new HomeSpotAdapter(getContext(), mData)); //为RecyclerView设置Adapter

        getDataFromServer();

        return v;
    }//onCreateView

    private void initTitleBar(final View v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                //修整titleBar
                TextView areaTV = (TextView)v.findViewById(R.id.bhr_home_title_area_tv);
                ImageView areaIV = (ImageView)v.findViewById(R.id.bhr_home_title_area_icon_iv);
                ImageView posIV = (ImageView)v.findViewById(R.id.bhr_home_title_position_iv);
                EditText titleSearch = (EditText)v.findViewById(R.id.bhr_home_title_search_et);
                PercentRelativeLayout titleBar = (PercentRelativeLayout)v.findViewById(R.id.bhr_home_title_prl);

                //获取文字高度
                float areaTextSize = areaTV.getTextSize();
                int lineHeight = (int)DisplayUtil.getTextHeightFromPx(getContext(), areaTextSize);

                //求得areaIV的高度和宽度
                int areaIVWNH = (int)(0.6 * lineHeight);
                LayoutParams params = areaIV.getLayoutParams();
                params.width = params.height = areaIVWNH;

                //修整areaIV的宽高
                areaIV.setLayoutParams(params);

                //求得posIV的宽度和高度
                int posIVWNH = (int)(1.5 * lineHeight);
                params = posIV.getLayoutParams();
                params.width = params.height = posIVWNH;

                //修整posIV的宽高
                posIV.setLayoutParams(params);

                //设置title搜索栏的搜索图标
                lineHeight = (int)(DisplayUtil.getTextHeightFromPx(getContext(), titleSearch.getTextSize()));
                Drawable drawableStart = ContextCompat.getDrawable(getContext(), R.drawable.search);
                int searchIconWNH = (int)(0.9 * lineHeight);
                drawableStart.setBounds(0, 0, searchIconWNH, searchIconWNH);
                titleSearch.setCompoundDrawables(drawableStart, null, null, null);

                //求得titleBar的宽度和高度,并修整
                int titleBarMinH = (int)(2.5 * lineHeight);
                titleBar.setMinimumHeight(titleBarMinH);
            }
        });//Runnable
    }//initTitleBar

    private void getDataFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/spot/getspotid";//10.7.88.89,192.168.191.1

        RequestParams params = new RequestParams();
        params.add("id", "111");

        client.get(getActivity().getApplicationContext(), url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                if (null == response) {
                    return;
                }

                mData.clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                Log.d(TAG, "onSuccess: responselength:" + response.length());

                new Thread() {
                    @Override
                    public void run() {
                        OkHttpClient okClient = new OkHttpClient();
                        String url = "http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/spot/getrecommend";//10.7.88.89,192.168.191.1

                        for(int i = 0; i<response.length(); i++) {
                            try {
                                String spot_id = response.getString(i);

                                RequestBody body = new FormBody.Builder().add("id", spot_id).build();
                                Request request = new Request.Builder().url(url).post(body).build();

                                Response okResponse = okClient.newCall(request).execute();
                                JSONObject response = new JSONObject(okResponse.body().string());
                                Log.d(TAG, "run: response:" + response);
                                try {
                                    HomeListSpotItem spot = new HomeListSpotItem(spot_id, response.getString("recommend_imgs"),
                                            response.getString("cn_name"), response.getString("en_name"),
                                            Integer.parseInt(response.getString("recommend_index")), 100);
                                    mData.add(spot);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "onSuccess: mListDataSize:" + mData.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Message msg = new Message();
                        msg.what = WHAT_NOTIFY;
                        mHandler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }
}//class_SuperHomeFragment
