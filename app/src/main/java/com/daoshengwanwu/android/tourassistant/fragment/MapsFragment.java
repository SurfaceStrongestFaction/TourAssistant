package com.daoshengwanwu.android.tourassistant.fragment;


import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
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
import com.daoshengwanwu.android.tourassistant.model.Frag;
import com.daoshengwanwu.android.tourassistant.model.MapsFragmentSaveData;
import com.daoshengwanwu.android.tourassistant.model.User;
import com.daoshengwanwu.android.tourassistant.model.UserWarehouse;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.service.SharingService;
import com.daoshengwanwu.android.tourassistant.utils.ToastUtil;
import com.daoshengwanwu.android.tourassistant.view.MyView;
import com.luolc.emojirain.EmojiRainLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

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
        AMap.OnMapClickListener, PoiSearch.OnPoiSearchListener
        , AMap.OnMarkerClickListener{
    //------------------------------鑳滆揪-------------------------------
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
    private TextureMapView mapView;

    private PoiResult poiResult; // poi杩斿洖鐨勭粨鏋�
    private int currentPage = 0;// 褰撳墠椤甸潰锛屼粠0寮�濮嬭鏁�
    private PoiSearch.Query query;// Poi鏌ヨ鏉′欢绫�
    private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);// 116.472995,39.993743
    private Marker locationMarker; // 閫夋嫨鐨勭偣
    private Marker detailMarker;
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;// poi鍥惧眰
    private List<PoiItem> poiItems;// poi鏁版嵁

    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private EditText mSearchText;
    private Button mQuit_fog_btn;
    //--------------------------------------------------------------------

    //------------------------------娴╃劧-----------------------------------
    private static final String TAG = "MapsFragment";
    private static final String KEY_BINDER = "MapsFragment.KEY_BINDER";
    private static final int WHAT_LOCATION_CHANGE = 0;
    private static final int WHAT_MEMBER_LOC_INFOS_ARRIVE = 1;
    private static final String MSG_DATA_LATITUDE = "msg_data_latitude";
    private static final String MSG_DATA_LONGITUDE = "msg_data_longitude";
    private static final String MSG_DATA_MEMBER_LOC_INFOS = "msg_data_member_loc_infos";
    private static final String KEY_START_LOCATION = "MapsFragment.KEY_START_LOCATION";
    private static final String KEY_START_UPLOAD = "MapsFragment.KEY_START_UPLOAD";
    private static final String KEY_START_BLACK = "MapsFragment.KEY_START_BLACK";
    private static final String KEY_SAVE_DATA = "MapsFragment.KEY_SAVE_DATA";
    private static final String KEY_SPOT_ID = "MapsFragment.KEY_SPOT_ID";

    private Map<String, Marker> mMemberMarkers = new HashMap<>();
    private boolean mIsDroppingGift = false;
    private boolean mIsStartLocation = false;
    private boolean mIsStartUpload = false;
    private Button mStartLocation;
    private Button mStopLocation;
    private Button mStartUpload;
    private List<Frag> mFrags = new ArrayList<>();
    private Button mStopUpload;
    private boolean mIsFirstLoc = true;
    private Marker mUserMarker = null;
    private SharingService.SharingBinder mSharingBinder;
    private boolean mIsStartBlack = false;
    private TextView mUserNickName = null;
    private TextView mGroupName = null;
    private String mSpotId = "";
    private EmojiRainLayout mEmojiRainLayout = null;
    private Map<Marker, String> mMarkerIdReflection = new HashMap<>();
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
                        Log.d(TAG, "handleMessage: shoudaoweizhixinxi");
                        double latitude = data.getDouble(MSG_DATA_LATITUDE);
                        double longitude = data.getDouble(MSG_DATA_LONGITUDE);
                        LatLng currentLoc = new LatLng(latitude, longitude);

                        Log.d(TAG, "handleMessage: 鎺ユ敹鍒板潗鏍囦俊鎭細latitude锛�" + latitude + ", longitude:" + longitude);

                        if (mIsFirstLoc) {
                            Log.d(TAG, "handleMessage: 绗竴娆″畾浣�");
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15.0f));
                            mUserMarker = aMap.addMarker(new MarkerOptions().position(currentLoc)
                                    .title(AppUtil.User.USER_NAME)
                                    .snippet("浣犲湪杩欓噷"));
                            mMarkerIdReflection.put(mUserMarker, AppUtil.User.USER_ID);
                            mIsFirstLoc = false;
                        }

                        //鍒ゆ柇鏄惁鎺夎惤绯栨灉
                            for (Frag frag : mFrags) {
                                double distance = Math.sqrt(Math.pow((frag.getLatitude() - latitude), 2) + Math.pow(frag.getLongitude() - longitude, 2));
                                if (distance <= 0.001) {
                                    mIsDroppingGift = true;
                                    startEmoji();
                                    new AlertDialog.Builder(getActivity()).setTitle("鍙戠幇绀肩墿锛�").setView(R.layout.baihaoran_dialog_baoxiang)
                                            .setPositiveButton("鏀跺叆鎬�涓�", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).create().show();
                                    frag.setLatitude(0.0);
                                    frag.setLongitude(0.0);
                                }
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
                                        .title("濂藉弸")
                                        .snippet("浣犵殑濂藉弸鍦ㄨ繖閲寏"));
                                mMarkerIdReflection.put(marker, user_id);
                                mMemberMarkers.put(user_id, marker);
                            }
                        }
                    }
                } break;
                default: break;//0.000002881364,0.00169745809963014992825543408831
            }
        }
    };
    //----------------------------------------------------------------------


    private void initFragCoorderData() {
        mFrags.add(new Frag(37.997319,114.521594,R.drawable.yuanbao));//114.521594,37.997319
        mFrags.add(new Frag(37.997293,114.520564,R.drawable.yuanbao));//114.520564,37.997293

    }//initFragCoorderData

    public MapsFragmentSaveData getCurrentState() {
        MapsFragmentSaveData data = new MapsFragmentSaveData();
        data.setStartBlack(mIsStartBlack);
        data.setStartLocation(mIsStartLocation);
        data.setStartUpload(mIsStartUpload);

        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        //-------------------------鑳滆揪-------------------------------------------------
        View v = inflater.inflate(R.layout.jiangshengda_fragment_maps, container, false);

        btn = (Button) v.findViewById(R.id.Fog_btn);
        btn.setVisibility(btn.GONE);
        act_main = (FrameLayout)v.findViewById(R.id.fragment_maps);
        mQuit_fog_btn = (Button) v.findViewById(R.id.Quit_Fog_btn);
        mQuit_fog_btn.setVisibility(mQuit_fog_btn.GONE);
        mapView = (TextureMapView) v.findViewById(R.id.map);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View infoWindow = getLayoutInflater(savedInstanceState).inflate(
                            R.layout.custom_marker, null, false);

                    ImageView img = (ImageView)infoWindow.findViewById(R.id.head_pic);
                    TextView nickName = (TextView)infoWindow.findViewById(R.id.nick_name);

                    User user = UserWarehouse.getInstance(getActivity()).getUserById(mMarkerIdReflection.get(marker));
                    if (null != user) {
                        nickName.setText(UserWarehouse.getInstance(getActivity()).getUserById(mMarkerIdReflection.get(marker)).getNickName());
                        ImageLoader.getInstance().displayImage(UserWarehouse.getInstance(getActivity()).getUserById(mMarkerIdReflection.get(marker)).getHeadPicUrl(), img);
                    }

                    return infoWindow;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            aMap.setOnMapClickListener(this);
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

        //鍦╝ctivity鎵цonCreate鏃舵墽琛宮MapView.onCreate(savedInstanceState)锛屽疄鐜板湴鍥剧敓鍛藉懆鏈熺鐞�
        mapView.onCreate(savedInstanceState);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFogModel();
            }
        });

        mQuit_fog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopFogModel();
            }
        });

        removeAMapLogo(); //鍒犻櫎楂樺痉logo
        //璁剧疆浣跨敤鏅�氬湴鍥�
        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);//澶滄櫙鍦板浘妯″紡
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        //---------------------------------------------------------------------------------


        //-------------------------------娴╃劧----------------------------------------------
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
        //-------------------------------鑳滆揪-----------------------------------------------
        mStopLocation.setVisibility(mStopLocation.GONE);
        mStopUpload.setVisibility(mStopUpload.GONE);
        //---------------------------------------------------------------------------------

        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mStartUpload.setOnClickListener(this);
        mStopUpload.setOnClickListener(this);

        updateCurrentInfomation();

        mSharingBinder.registerTeamChangeListener(new SharingService.OnTeamChangeListener() {
            @Override
            public void onTeamChange(String team_id) {
                updateCurrentInfomation();
            }
        });

        MapsFragmentSaveData saveData = (MapsFragmentSaveData)getArguments().getSerializable(KEY_SAVE_DATA);
        if (null != saveData) {
            if (saveData.isStartLocation()) {
                mStartLocation.callOnClick();
            }

            if (saveData.isStartUpload()) {
                mStartUpload.callOnClick();
            }

            if (saveData.isStartBlack()) {
                startFogModel();
            }
        }

//        //鑾峰彇鏅偣淇℃伅
//        String url = "http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/spot/getrecommend";
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//
//        mSpotId = getArguments().getString(KEY_SPOT_ID);
//        params.add("id", mSpotId);
//
//        client.get(getActivity().getApplicationContext(), url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//        });

        initFragCoorderData(); //鍒濆鍖栧僵铔嬬殑浣嶇疆淇℃伅
        initEmojiRainData(v); //鍒濆鍖栬〃鎯呴洦鐨勬暟鎹俊鎭�

        return v;
    }

    private void initEmojiRainData(View v) {
        mEmojiRainLayout = (EmojiRainLayout)v.findViewById(R.id.emoji_rain_layout);
        mEmojiRainLayout.addEmoji(R.drawable.emoji_1_3);
        mEmojiRainLayout.addEmoji(R.drawable.emoji_2_3);
        mEmojiRainLayout.addEmoji(R.drawable.emoji_3_3);
        mEmojiRainLayout.addEmoji(R.drawable.emoji_4_3);
        mEmojiRainLayout.addEmoji(R.drawable.emoji_5_3);
        mEmojiRainLayout.addEmoji(R.drawable.yuanbao);
    }

    private void startEmoji() {
        mEmojiRainLayout.startDropping();
    }

    private void stopEmoji() {
        mEmojiRainLayout.stopDropping();
    }

    private void startFogModel(){
        btn.setVisibility(btn.GONE);
        mQuit_fog_btn.setVisibility(mQuit_fog_btn.VISIBLE);
        //Fog
        mIsStartBlack = true;
        myView = new MyView(getActivity().getApplicationContext());
        act_main.addView(myView);
        i = 0;//鍒濆鍖栬鏁板櫒
        aMap.getUiSettings().setAllGesturesEnabled(false);//绂佹鎵�鏈夋墜鍔挎搷浣�
    }

    private void stopFogModel(){
        myView.setVisibility(myView.GONE);
        btn.setVisibility(btn.VISIBLE);
        mQuit_fog_btn.setVisibility(mQuit_fog_btn.GONE);
        aMap.getUiSettings().setAllGesturesEnabled(true);//鍏佽鎵�鏈夋墜鍔挎搷浣�
        aMap.getUiSettings().setRotateGesturesEnabled(false);//绂佹鍦板浘鏃嬭浆鎵嬪娍
        aMap.getUiSettings().setTiltGesturesEnabled(false);//绂佹鍊炬枩鎵嬪娍
    }

//-----------------------------Serach--------------------------------------------------
    /*
     * 寮�濮嬭繘琛宲oi鎼滅储
     */
    protected void doSearchQuery() {
        keyWord = mSearchText.getText().toString().trim();
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 绗竴涓弬鏁拌〃绀烘悳绱㈠瓧绗︿覆锛岀浜屼釜鍙傛暟琛ㄧずpoi鎼滅储绫诲瀷锛岀涓変釜鍙傛暟琛ㄧずpoi鎼滅储鍖哄煙锛堢┖瀛楃涓蹭唬琛ㄥ叏鍥斤級
        query.setPageSize(20);// 璁剧疆姣忛〉鏈�澶氳繑鍥炲灏戞潯poiitem
        query.setPageNum(currentPage);// 璁剧疆鏌ョ涓�椤�

        if (lp != null) {
            poiSearch = new PoiSearch(getActivity(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
            // 璁剧疆鎼滅储鍖哄煙涓轰互lp鐐逛负鍦嗗績锛屽叾鍛ㄥ洿5000绫宠寖鍥�
            poiSearch.searchPOIAsyn();// 寮傛鎼滅储
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 鎼滅储poi鐨勭粨鏋�
                if (result.getQuery().equals(query)) {// 鏄惁鏄悓涓�鏉�
                    poiResult = result;
                    poiItems = poiResult.getPois();// 鍙栧緱绗竴椤电殑poiitem鏁版嵁锛岄〉鏁颁粠鏁板瓧0寮�濮�
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 褰撴悳绱笉鍒皃oiitem鏁版嵁鏃讹紝浼氳繑鍥炲惈鏈夋悳绱㈠叧閿瓧鐨勫煄甯備俊鎭�
                    if (poiItems != null && poiItems.size() > 0) {
                        //娓呴櫎POI淇℃伅鏄剧ず
                        whetherToShowDetailInfo(false);
                        //骞惰繕鍘熺偣鍑籱arker鏍峰紡
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //娓呯悊涔嬪墠鎼滅储缁撴灉鐨刴arker
                        if (poiOverlay !=null) {
                            poiOverlay.removeFromMap();
                        }
                        aMap.clear();
                        poiOverlay = new myPoiOverlay(aMap, poiItems);
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        /*
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
                                .strokeWidth(2));*/

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
                    // 灏嗕箣鍓嶈鐐瑰嚮鐨刴arker缃负鍘熸潵鐨勭姸鎬�
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

    // 灏嗕箣鍓嶈鐐瑰嚮鐨刴arker缃负鍘熸潵鐨勭姸鎬�
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
     * poi娌℃湁鎼滅储鍒版暟鎹紝杩斿洖涓�浜涙帹鑽愬煄甯傜殑淇℃伅
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "鎺ㄨ崘鍩庡競\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "鍩庡競鍚嶇О:" + cities.get(i).getCityName() + "鍩庡競鍖哄彿:"
                    + cities.get(i).getCityCode() + "鍩庡競缂栫爜:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(getActivity(), infomation);

    }


    /*
     * 鑷畾涔塒oiOverlay
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
         * 娣诲姞Marker鍒板湴鍥句腑銆�
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
         * 鍘绘帀PoiOverlay涓婃墍鏈夌殑Marker銆�
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /*
         * 绉诲姩闀滃ご鍒板綋鍓嶇殑瑙嗚銆�
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                //mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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
         * 浠巑arker涓緱鍒皃oi鍦╨ist鐨勪綅缃��
         *
         * @param marker 涓�涓爣璁扮殑瀵硅薄銆�
         * @return 杩斿洖璇arker瀵瑰簲鐨刾oi鍦╨ist鐨勪綅缃��
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
         * 杩斿洖绗琲ndex鐨刾oi鐨勪俊鎭��
         * @param index 绗嚑涓猵oi銆�
         * @return poi鐨勪俊鎭�俻oi瀵硅薄璇﹁鎼滅储鏈嶅姟妯″潡鐨勫熀纭�鏍稿績鍖咃紙com.amap.api.services.core锛変腑鐨勭被 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core涓殑绫�">PoiItem</a></strong>銆�
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
    璁剧疆涓�浜沘map鐨勫睘鎬�
     */
    private void setUpMap() {
        aMap.getUiSettings().setRotateGesturesEnabled(false);//绂佹鍦板浘鏃嬭浆鎵嬪娍
        aMap.getUiSettings().setTiltGesturesEnabled(false);//绂佹鍊炬枩鎵嬪娍
        //aMap.setLocationSource(this);// 璁剧疆瀹氫綅鐩戝惉
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 璁剧疆榛樿瀹氫綅鎸夐挳鏄惁鏄剧ず
        aMap.getUiSettings().setScaleControlsEnabled(true);//鏄剧ず姣斾緥灏烘帶浠�
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//璁剧疆姣斾緥灏猴紝3-19
        aMap.setMyLocationEnabled(true);// 璁剧疆涓簍rue琛ㄧず鏄剧ず瀹氫綅灞傚苟鍙Е鍙戝畾浣嶏紝false琛ㄧず闅愯棌瀹氫綅灞傚苟涓嶅彲瑙﹀彂瀹氫綅锛岄粯璁ゆ槸false
        // 璁剧疆瀹氫綅鐨勭被鍨嬩负瀹氫綅妯″紡 锛屽彲浠ョ敱瀹氫綅銆佽窡闅忔垨鍦板浘鏍规嵁闈㈠悜鏂瑰悜鏃嬭浆鍑犵
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);//璺熼殢妯″紡
       // aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE); //瀹氫綅妯″紡
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE); // 璁剧疆瀹氫綅鐨勭被鍨嬩负鏍规嵁鍦板浘闈㈠悜鏂瑰悜鏃嬭浆
    }

    private void removeAMapLogo() {
        //杩欎袱琛屼唬鐮佸彲浠ラ殣钘忛珮寰峰湴鍥緇ogo
        UiSettings uiSettings =  aMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-50);//闅愯棌logo
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Log.d(TAG, "onDestroy: ");


        if (mIsStartUpload) {
            mSharingBinder.stopUploadLocation();
        }

        if (mIsStartBlack) {
            stopFogModel();
        }

        if (mIsStartLocation) {
            mSharingBinder.stopLocationService();
            Log.d(TAG, "onStop: guanbi location");
        }

        mIsFirstLoc = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //鍦╝ctivity鎵цonResume鏃舵墽琛宮MapView.onResume ()锛屽疄鐜板湴鍥剧敓鍛藉懆鏈熺鐞�
        mapView.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        //鍦╝ctivity鎵цonPause鏃舵墽琛宮MapView.onPause ()锛屽疄鐜板湴鍥剧敓鍛藉懆鏈熺鐞�
        mapView.onPause();
        Log.d(TAG, "onPause: ");
    }

    /*
    瀹氫綅鎴愬姛鍚庡洖璋冨嚱鏁�
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        x = amapLocation.getLatitude();//鑾峰彇绾害
        y = amapLocation.getLongitude();//鑾峰彇缁忓害
        //涓庨珮寰稤emo鐨勬帴鍙ｏ紝閫氳繃鏇存敼瀹氱偣lp鐨勫�艰揪鍒颁笉鐢ㄤ慨鏀瑰師浠ｇ爜鐨勭洰鐨�
        lp.setLatitude(x);
        lp.setLongitude(y);
        //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));
        if (mIsStartBlack) {
            if (amapLocation.getErrorCode() == 0) {
                //杩烽浘杩借釜閮ㄥ垎浠ｇ爜
                pos = new LatLng(x,y);
                Projection projection = aMap.getProjection();
                //灏嗗湴鍥剧殑鐐癸紝杞崲涓哄睆骞曚笂鐨勭偣聽
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
                //Toast.makeText(getActivity(), "Latitude:" + x + ", Longitude:" + y, Toast.LENGTH_SHORT).show();

                //amapLocation.getLocationType();//鑾峰彇褰撳墠瀹氫綅缁撴灉鏉ユ簮锛屽缃戠粶瀹氫綅缁撴灉锛岃瑙佸畾浣嶇被鍨嬭〃
                //amapLocation.getLatitude();//鑾峰彇绾害
                //amapLocation.getLongitude();//鑾峰彇缁忓害
                //amapLocation.getAccuracy();//鑾峰彇绮惧害淇℃伅
                //amapLocation.getAddress();//鍦板潃锛屽鏋渙ption涓缃甶sNeedAddress涓篺alse锛屽垯娌℃湁姝ょ粨鏋滐紝缃戠粶瀹氫綅缁撴灉涓細鏈夊湴鍧�淇℃伅锛孏PS瀹氫綅涓嶈繑鍥炲湴鍧�淇℃伅銆�
                //amapLocation.getCountry();//鍥藉淇℃伅
                //amapLocation.getProvince();//鐪佷俊鎭�
                //amapLocation.getCity();//鍩庡競淇℃伅
                //amapLocation.getDistrict();//鍩庡尯淇℃伅
                //amapLocation.getStreet();//琛楅亾淇℃伅
                //amapLocation.getStreetNum();//琛楅亾闂ㄧ墝鍙蜂俊鎭�
                //amapLocation.getCityCode();//鍩庡競缂栫爜
                //amapLocation.getAdCode();//鍦板尯缂栫爜
                //amapLocation.getAoiName();//鑾峰彇褰撳墠瀹氫綅鐐圭殑AOI淇℃伅
                //amapLocation.getGpsStatus();//鑾峰彇GPS鐨勫綋鍓嶇姸鎬�
            }
        }

        Message msg = new Message();
        msg.what = WHAT_LOCATION_CHANGE;

        Bundle data = new Bundle();
        data.putDouble(MSG_DATA_LATITUDE, amapLocation.getLatitude());
        data.putDouble(MSG_DATA_LONGITUDE, amapLocation.getLongitude());

        msg.setData(data);
        mHandler.sendMessage(msg);
    }


    private void updateCurrentInfomation() {
        if (null != USER_NAME && !USER_NAME.equals("")) {
            mUserNickName.setText("褰撳墠鐢ㄦ埛锛�" + USER_NAME);
        }
        if (null != GROUP_NAME && !GROUP_NAME.equals("")) {
            mGroupName.setText("鎵�鍦ㄥ垎缁勶細" + GROUP_NAME);
        }
    }


    public static MapsFragment newInstance(SharingService.SharingBinder binder, MapsFragmentSaveData saveData, String spot_id) {
        MapsFragment fragment = new MapsFragment();

        Bundle data = new Bundle();
        data.putBinder(KEY_BINDER, binder);
        data.putSerializable(KEY_SAVE_DATA, saveData);
        data.putString(KEY_SPOT_ID, spot_id);
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
                //-------------------------鑳滆揪-------------------------------------
                btn.setVisibility(btn.VISIBLE);
                mStartLocation.setVisibility(mStartLocation.GONE);
                mStopLocation.setVisibility(mStopLocation.VISIBLE);
                //-----------------------------------------------------------------
                updateCurrentInfomation();

                if (mIsStartLocation) {
                    Toast.makeText(getActivity(), "鎮ㄥ紑鍚畾浣嶆湇鍔℃棤闇�閲嶅寮�鍚�...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "瀹氫綅鏈嶅姟寮�鍚腑...", Toast.LENGTH_SHORT).show();
                    mSharingBinder.startLocationService();
                    mIsStartLocation = true;
                }
                break;
            case R.id.stop_location:
                //-------------------------鑳滆揪-------------------------------------
                if (btn.getVisibility() == View.GONE){
                    myView.setVisibility(myView.GONE);
                    mQuit_fog_btn.setVisibility(btn.GONE);
                }else {
                    btn.setVisibility(btn.GONE);
                }
                mStartLocation.setVisibility(mStartLocation.VISIBLE);
                mStopLocation.setVisibility(mStopLocation.GONE);
                aMap.getUiSettings().setAllGesturesEnabled(true);//鍏佽鎵�鏈夋墜鍔挎搷浣�
                aMap.getUiSettings().setRotateGesturesEnabled(false);//绂佹鍦板浘鏃嬭浆鎵嬪娍
                aMap.getUiSettings().setTiltGesturesEnabled(false);//绂佹鍊炬枩鎵嬪娍
                //-----------------------------------------------------------------
                if (mIsStartLocation) {
                    mSharingBinder.stopLocationService();
                    mUserMarker.remove();
                    mUserMarker = null;
                    mIsFirstLoc = true;
                    mIsStartLocation = false;
                    Toast.makeText(getActivity(), "鍏抽棴瀹氫綅鏈嶅姟...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "鎮ㄥ皻鏈紑鍚畾浣嶆湇鍔�...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.start_upload:
                //-----------------------鑳滆揪------------------------------------------------
                mStartUpload.setVisibility(mStartUpload.GONE);
                mStopUpload.setVisibility(mStopUpload.VISIBLE);
                //--------------------------------------------------------------------------
                updateCurrentInfomation();

                if (!AppUtil.User.USER_ID.equals("") && !AppUtil.Group.GROUP_ID.equals("")) {
                    try {
                        if (!mIsStartUpload) {
                            Toast.makeText(getActivity(), "寮�鍚綅缃叡浜�...", Toast.LENGTH_SHORT).show();
                            mSharingBinder.startUploadLocation();
                            mIsStartUpload = true;
                        } else {
                            Toast.makeText(getActivity(), "鎮ㄥ凡寮�鍚綅缃叡浜�,鏃犻渶閲嶅寮�鍚�...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else  if (AppUtil.User.USER_ID.equals("")){
                    Toast.makeText(getActivity(), "璇峰厛鐧诲綍鍐嶅紑鍚鍔熻兘...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "璇峰厛鍔犲叆闃熶紞鍐嶅紑鍚鍔熻兘", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stop_upload:
                //-----------------------鑳滆揪------------------------------------------------
                mStartUpload.setVisibility(mStartUpload.VISIBLE);
                mStopUpload.setVisibility(mStopUpload.GONE);
                //--------------------------------------------------------------------------
                if (mIsStartUpload) {
                    Toast.makeText(getActivity(), "姝ｅ湪鍏抽棴浣嶇疆鍏变韩...", Toast.LENGTH_SHORT).show();
                    mSharingBinder.stopUploadLocation();
                    for (String user_id : mMemberMarkers.keySet()) {
                        mMemberMarkers.get(user_id).remove();
                        mMemberMarkers.remove(user_id);
                        mMemberMarkers.clear();
                    }
                    mIsStartUpload = false;
                } else {
                    Toast.makeText(getActivity(), "鎮ㄥ皻鏈紑鍚綅缃叡浜�", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //鍦╝ctivity鎵цonSaveInstanceState鏃舵墽琛宮MapView.onSaveInstanceState (outState)锛屽疄鐜板湴鍥剧敓鍛藉懆鏈熺鐞�
        mapView.onSaveInstanceState(outState);

        outState.putBoolean(KEY_START_BLACK, mIsStartBlack);
        outState.putBoolean(KEY_START_LOCATION, mIsStartLocation);
        outState.putBoolean(KEY_START_UPLOAD, mIsStartUpload);

        Log.d(TAG, "onSaveInstanceState: ");
    }
}
