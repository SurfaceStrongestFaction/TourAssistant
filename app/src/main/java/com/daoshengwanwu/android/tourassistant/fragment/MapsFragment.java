package com.daoshengwanwu.android.tourassistant.fragment;


import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;//定位信息类
import com.amap.api.location.AMapLocationListener;//定位回调接口
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;

import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.daoshengwanwu.android.tourassistant.R;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.ToastUtil;
import com.daoshengwanwu.android.tourassistant.view.MyView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daoshengwanwu.android.tourassistant.activity.LauncherActivity.fog_draw_pause_judge;
import static com.daoshengwanwu.android.tourassistant.utils.AppUtil.Group.GROUP_NAME;
import static com.daoshengwanwu.android.tourassistant.utils.AppUtil.User.USER_ID;
import static com.daoshengwanwu.android.tourassistant.utils.AppUtil.User.USER_NAME;


public class MapsFragment extends Fragment implements AMapLocationListener,
        SharingService.SharingLocationListener, View.OnClickListener,
        AMap.OnMapClickListener, AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener {
    //------------------------------胜达-------------------------------
    private LatLng pos;
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

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);// 116.472995,39.993743
    private Marker locationMarker; // 选择的点
    private Marker detailMarker;
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据

    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private EditText mSearchText;
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

        btn = (Button) v.findViewById(R.id.Fog_btn);
        act_main = (FrameLayout)v.findViewById(R.id.fragment_maps);
        mapView = (MapView) v.findViewById(R.id.map);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnInfoWindowClickListener(this);
            aMap.setInfoWindowAdapter(this);
            TextView searchButton = (TextView) v.findViewById(R.id.btn_search);
            searchButton.setOnClickListener(this);
            /*
            locationMarker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point4)))
                    .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
            locationMarker.showInfoWindow();
            */
            setUpMap();
        }
        mPoiDetail = (RelativeLayout) v.findViewById(R.id.poi_detail);
        mPoiDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(PoiSearchActivity.this,
//						SearchDetailActivity.class);
//				intent.putExtra("poiitem", mPoi);
//				startActivity(intent);

            }
        });
        mPoiName = (TextView) v.findViewById(R.id.poi_name);
        mPoiAddress = (TextView) v.findViewById(R.id.poi_address);
        mSearchText = (EditText) v.findViewById(R.id.input_edittext);
        //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));

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
                //aMap.getUiSettings().setAllGesturesEnabled(false);//禁止所有手势操作
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

//-----------------------------Serach--------------------------------------------------
    /*
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        keyWord = mSearchText.getText().toString().trim();
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(getActivity(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        //清除POI信息显示
                        whetherToShowDetailInfo(false);
                        //并还原点击marker样式
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //清理之前搜索结果的marker
                        if (poiOverlay !=null) {
                            poiOverlay.removeFromMap();
                        }
                        aMap.clear();
                        poiOverlay = new myPoiOverlay(aMap, poiItems);
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        aMap.addMarker(new MarkerOptions()
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory
                                        .fromBitmap(BitmapFactory.decodeResource(
                                                getResources(), R.drawable.point4)))
                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));

                        aMap.addCircle(new CircleOptions()
                                .center(new LatLng(lp.getLatitude(),
                                        lp.getLongitude())).radius(5000)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.argb(50, 1, 1, 1))
                                .strokeWidth(2));

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(getActivity(),
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil
                        .show(getActivity(), R.string.no_result);
            }
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getObject() != null) {
            whetherToShowDetailInfo(true);
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                if (mlastMarker == null) {
                    mlastMarker = marker;
                } else {
                    // 将之前被点击的marker置为原来的状态
                    resetlastmarker();
                    mlastMarker = marker;
                }
                detailMarker = marker;
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.poi_marker_pressed)));

                setPoiItemDisplayContent(mCurrentPoi);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }else {
            whetherToShowDetailInfo(false);
            resetlastmarker();
        }


        return true;
    }

    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        }else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
        }
        mlastMarker = null;

    }


    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        mPoiName.setText(mCurrentPoi.getTitle());
        mPoiAddress.setText(mCurrentPoi.getSnippet()+mCurrentPoi.getDistance());
    }


    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    private int[] markers = {R.drawable.poi_marker_1,
            R.drawable.poi_marker_2,
            R.drawable.poi_marker_3,
            R.drawable.poi_marker_4,
            R.drawable.poi_marker_5,
            R.drawable.poi_marker_6,
            R.drawable.poi_marker_7,
            R.drawable.poi_marker_8,
            R.drawable.poi_marker_9,
            R.drawable.poi_marker_10
    };

    private void whetherToShowDetailInfo(boolean isToShow) {
        if (isToShow) {
            mPoiDetail.setVisibility(View.VISIBLE);

        } else {
            mPoiDetail.setVisibility(View.GONE);

        }
    }


    @Override
    public void onMapClick(LatLng arg0) {
        whetherToShowDetailInfo(false);
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    /*
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(getActivity(), infomation);

    }


    /*
     * 自定义PoiOverlay
     *
     */

    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
        public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /*
         * 添加Marker到地图中。
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /*
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /*
         * 移动镜头到当前的视角。
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /*
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /*
         * 返回第index的poi的信息。
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            }else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight));
                return icon;
            }
        }
    }
//--------------------------------------------------------------------------------------------------

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
                //迷雾追踪部分代码
                x = amapLocation.getLatitude();//获取纬度
                y = amapLocation.getLongitude();//获取经度
                pos = new LatLng(x,y);

                //与高德Demo的接口，通过更改定点lp的值达到不用修改原代码的目的
                lp.setLatitude(x);
                lp.setLongitude(y);
                //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));

                Projection projection = aMap.getProjection();
                //将地图的点，转换为屏幕上的点 
                Point dot = projection.toScreenLocation(pos);
                dot_x = dot.x;
                dot_y = dot.y;
                if (fog_draw_pause_judge){
                    if (i == 0){
                        myView.start_pot(dot_x,dot_y);
                    } else{
                        myView.line(dot_x, dot_y);
                    }
                    i++;
                }

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
            case R.id.btn_search:
                doSearchQuery();
                break;
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
