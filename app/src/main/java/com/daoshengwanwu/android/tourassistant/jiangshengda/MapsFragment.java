package com.daoshengwanwu.android.tourassistant.jiangshengda;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Point;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;//定位信息类
import com.amap.api.location.AMapLocationListener;//定位回调接口
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil;
import com.daoshengwanwu.android.tourassistant.baihaoran.SharingService;
import com.daoshengwanwu.android.tourassistant.jiangshengda.poisearch.PoiAroundSearchActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil.Group.GROUP_NAME;
import static com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil.User.USER_ID;
import static com.daoshengwanwu.android.tourassistant.baihaoran.AppUtil.User.USER_NAME;


public class MapsFragment extends Fragment implements AMapLocationListener, SharingService.SharingLocationListener, View.OnClickListener{
    //------------------------------胜达-------------------------------
    private int i = 0;
    private MyView myView;
    private double x;
    private double y;
    private float dot_x = 0;
    private float dot_y = 0;
    private FrameLayout act_main;
    private Button btn;
    private AMap aMap;
    private MapView mapView;
    //--------------------------------------------------------------------

    //------------------------------浩然-----------------------------------
    private static final String TAG = "MapsFragment";
    private static final String KEY_BINDER = "MapsFragment.KEY_BINDER";
    private static final int WHAT_LOCATION_CHANGE = 0;
    private static final int WHAT_MEMBER_LOC_INFOS_ARRIVE = 1;
    private static final String MSG_DATA_LATITUDE = "msg_data_latitude";
    private static final String MSG_DATA_LONGITUDE = "msg_data_longitude";
    private static final String MSG_DATA_MEMBER_LOC_INFOS = "msg_data_member_loc_infos";

    private Map<String, Marker> mMemberMarkers = new HashMap<>();
    private boolean mIsStartLocation = false;
    private boolean mIsStartUpload = false;
    private Button mStartLocation;
    private Button mStopLocation;
    private Button mStartUpload;
    private Button mStopUpload;
    private boolean mIsFirstLoc = true;
    private Marker mUserMarker = null;
    private SharingService.SharingBinder mSharingBinder;
    private boolean mIsStartBlack = false;
    private TextView mUserNickName = null;
    private TextView mGroupName = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            if (null == data) {
                return;
            }

            switch (msg.what) {
                case WHAT_LOCATION_CHANGE: {
                    if (mIsStartLocation) {
                        double latitude = data.getDouble(MSG_DATA_LATITUDE);
                        double longitude = data.getDouble(MSG_DATA_LONGITUDE);
                        LatLng currentLoc = new LatLng(latitude, longitude);

                        if (mIsFirstLoc) {
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15.0f));
                            mUserMarker = aMap.addMarker(new MarkerOptions().position(currentLoc)
                                    .title(AppUtil.User.USER_NAME)
                                    .snippet("你在这里"));
                            mIsFirstLoc = false;
                        }

                        mUserMarker.setPosition(currentLoc);
                    }
                } break;
                case WHAT_MEMBER_LOC_INFOS_ARRIVE: {
                    if (mIsStartUpload) {
                        Serializable seria = data.getSerializable(MSG_DATA_MEMBER_LOC_INFOS);
                        if (seria instanceof Map) {
                            Map<String, double[]> groupMemberLocations = (Map<String, double[]>) seria;
                            groupMemberLocations.remove(USER_ID);

                            for (String user_id : mMemberMarkers.keySet()) {
                                if (!groupMemberLocations.containsKey(user_id)) {
                                    mMemberMarkers.get(user_id).remove();
                                    mMemberMarkers.remove(user_id);
                                } else {
                                    double[] latlngs = groupMemberLocations.get(user_id);
                                    groupMemberLocations.remove(user_id);
                                    LatLng latlng = new LatLng(latlngs[0], latlngs[1]);
                                    mMemberMarkers.get(user_id).setPosition(latlng);
                                }
                            }

                            for (String user_id : groupMemberLocations.keySet()) {
                                double[] latlngs = groupMemberLocations.get(user_id);
                                LatLng currentLoc = new LatLng(latlngs[0], latlngs[1]);
                                Marker marker = aMap.addMarker(new MarkerOptions().position(currentLoc)
                                        .title("好友")
                                        .snippet("你的好友在这里~"));

                                mMemberMarkers.put(user_id, marker);
                            }
                        }
                    }
                } break;
                default: break;
            }
        }
    };
    //----------------------------------------------------------------------


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //-------------------------胜达-------------------------------------------------
        View v = inflater.inflate(R.layout.jiangshengda_fragment_maps, container, false);

        Button sousou = (Button) v.findViewById(R.id.sousou);
        sousou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PoiAroundSearchActivity.class);
                startActivity(i);
            }
        });

        btn = (Button) v.findViewById(R.id.Fog_btn);
        act_main = (FrameLayout)v.findViewById(R.id.fragment_maps);
        mapView = (MapView) v.findViewById(R.id.map);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fog
                mIsStartBlack = true;
                myView = new MyView(getActivity().getApplicationContext());
                act_main.addView(myView);
                i = 0;//初始化计数器
                aMap.getUiSettings().setAllGesturesEnabled(false);//禁止所有手势操作
            }
        });

        removeAMapLogo(); //删除高德logo
        //设置使用普通地图
        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图模式
        //aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //---------------------------------------------------------------------------------


        //-------------------------------浩然----------------------------------------------
        Bundle args = getArguments();
        mSharingBinder = (SharingService.SharingBinder)args.getBinder(KEY_BINDER);
        if (null != mSharingBinder) {
            mSharingBinder.registerSharingLocationListener(this);
            mSharingBinder.registerLocationListener(this);
        }

        mStartLocation = (Button)v.findViewById(R.id.start_location);
        mStopLocation = (Button)v.findViewById(R.id.stop_location);
        mStartUpload = (Button)v.findViewById(R.id.start_upload);
        mStopUpload = (Button)v.findViewById(R.id.stop_upload);
        mUserNickName = (TextView) v.findViewById(R.id.user_nick_name);
        mGroupName = (TextView)v.findViewById(R.id.group_name);

        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mStartUpload.setOnClickListener(this);
        mStopUpload.setOnClickListener(this);

        updateCurrentInfomation();

        return v;
    }

    /*
    设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁止倾斜手势
        //aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setScaleControlsEnabled(true);//显示比例尺控件
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//设置比例尺，3-19
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);//跟随模式
       // aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE); //定位模式
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE); // 设置定位的类型为根据地图面向方向旋转
    }

    private void removeAMapLogo() {
        //这两行代码可以隐藏高德地图logo
        UiSettings uiSettings =  aMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-50);//隐藏logo
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
        Log.d(TAG, "onPause: ");
    }

    /*
    定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && mIsStartBlack) {
            if (amapLocation.getErrorCode() == 0) {
                x = amapLocation.getLatitude();//获取纬度
                y = amapLocation.getLongitude();//获取经度
                LatLng pos = new LatLng(x,y);
                Projection projection = aMap.getProjection();
                //将地图的点，转换为屏幕上的点 
                Point dot = projection.toScreenLocation(pos);
                dot_x = dot.x;
                dot_y = dot.y;
                if (i == 0){
                    myView.start_pot(dot_x,dot_y);
                } else{
                    myView.line(dot_x, dot_y);
                }
                i++;
                //amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                //amapLocation.getLatitude();//获取纬度
                //amapLocation.getLongitude();//获取经度
                //amapLocation.getAccuracy();//获取精度信息
                //amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                //amapLocation.getCountry();//国家信息
                //amapLocation.getProvince();//省信息
                //amapLocation.getCity();//城市信息
                //amapLocation.getDistrict();//城区信息
                //amapLocation.getStreet();//街道信息
                //amapLocation.getStreetNum();//街道门牌号信息
                //amapLocation.getCityCode();//城市编码
                //amapLocation.getAdCode();//地区编码
                //amapLocation.getAoiName();//获取当前定位点的AOI信息
                //amapLocation.getGpsStatus();//获取GPS的当前状态
            }
        }

        if (null != amapLocation) {
            Message msg = new Message();
            msg.what = WHAT_LOCATION_CHANGE;

            Bundle data = new Bundle();
            data.putDouble(MSG_DATA_LATITUDE, amapLocation.getLatitude());
            data.putDouble(MSG_DATA_LONGITUDE, amapLocation.getLongitude());

            msg.setData(data);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    private void updateCurrentInfomation() {
        if (null != USER_NAME && !USER_NAME.equals("")) {
            mUserNickName.setText("当前用户：" + USER_NAME);
        }
        if (null != GROUP_NAME && !GROUP_NAME.equals("")) {
            mGroupName.setText("所在分组：" + GROUP_NAME);
        }
    }


    public static MapsFragment newInstance(SharingService.SharingBinder binder) {
        MapsFragment fragment = new MapsFragment();
        Bundle data = new Bundle();
        data.putBinder(KEY_BINDER, binder);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onLocationsDataArrived(HashMap<String, double[]> locationsData) {
        Message msg = new Message();
        msg.what = WHAT_MEMBER_LOC_INFOS_ARRIVE;

        Bundle data = new Bundle();
        data.putSerializable(MSG_DATA_MEMBER_LOC_INFOS, locationsData);
        msg.setData(data);

        mHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_location:
                updateCurrentInfomation();

                if (mIsStartLocation) {
                    Toast.makeText(getActivity(), "您开启定位服务无需重复开启...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "定位服务开启中...", Toast.LENGTH_SHORT).show();
                    mSharingBinder.startLocationService();
                    mIsStartLocation = true;
                }
                break;
            case R.id.stop_location:
                if (mIsStartLocation) {
                    mSharingBinder.stopLocationService();
                    mUserMarker.remove();
                    mUserMarker = null;
                    mIsFirstLoc = true;
                    mIsStartLocation = false;
                    Toast.makeText(getActivity(), "关闭定位服务...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "您尚未开启定位服务...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.start_upload:
                updateCurrentInfomation();

                if (!AppUtil.User.USER_ID.equals("") && !AppUtil.Group.GROUP_ID.equals("")) {
                    try {
                        if (!mIsStartUpload) {
                            Toast.makeText(getActivity(), "开启位置共享...", Toast.LENGTH_SHORT).show();
                            mSharingBinder.startUploadLocation();
                            mIsStartUpload = true;
                        } else {
                            Toast.makeText(getActivity(), "您已开启位置共享,无需重复开启...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else  if (AppUtil.User.USER_ID.equals("")){
                    Toast.makeText(getActivity(), "请先登录再开启此功能...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "请先加入队伍再开启此功能", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stop_upload:
                if (mIsStartUpload) {
                    Toast.makeText(getActivity(), "正在关闭位置共享...", Toast.LENGTH_SHORT).show();
                    mSharingBinder.stopUploadLocation();
                    for (String user_id : mMemberMarkers.keySet()) {
                        mMemberMarkers.get(user_id).remove();
                        mMemberMarkers.remove(user_id);
                        mMemberMarkers.clear();
                    }
                    mIsStartUpload = false;
                } else {
                    Toast.makeText(getActivity(), "您尚未开启位置共享", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
