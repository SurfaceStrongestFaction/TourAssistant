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
    private TextureMapView mapView;

    private PoiResult poiResult;
    private int currentPage = 0;
    private PoiSearch.Query query;
    private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);// 116.472995,39.993743
    private Marker locationMarker;
    private Marker detailMarker;
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;
    private List<PoiItem> poiItems;

    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private EditText mSearchText;
    private Button mQuit_fog_btn;
    //--------------------------------------------------------------------

    //------------------------------浩然-----------------------------------
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

                        Log.d(TAG, "handleMessage: 接收到坐标信息：latitude：" + latitude + ", longitude:" + longitude);

                        if (mIsFirstLoc) {
                            Log.d(TAG, "handleMessage: 第一次定位");
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15.0f));
                            mUserMarker = aMap.addMarker(new MarkerOptions().position(currentLoc)
                                    .title(AppUtil.User.USER_NAME)
                                    .snippet("你在这里"));
                            mMarkerIdReflection.put(mUserMarker, AppUtil.User.USER_ID);
                            mIsFirstLoc = false;
                        }

                        //鍒ゆ柇鏄惁鎺夎惤绯栨灉
                            for (Frag frag : mFrags) {
                                double distance = Math.sqrt(Math.pow((frag.getLatitude() - latitude), 2) + Math.pow(frag.getLongitude() - longitude, 2));
                                if (distance <= 0.001) {
                                    mIsDroppingGift = true;
                                    startEmoji();
                                    new AlertDialog.Builder(getActivity()).setTitle("发现礼物！").setView(R.layout.baihaoran_dialog_baoxiang)
                                            .setPositiveButton("收入怀中", new DialogInterface.OnClickListener() {
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
                                        .title("好友")
                                        .snippet("你的好友在这里~"));
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
        //-------------------------胜达------------------------------------------------
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

        removeAMapLogo();
        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        //---------------------------------------------------------------------------------


        //-----------------------------------------------------------------------------
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
        //------------------------------------------------------------------------------
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

        initFragCoorderData();
        initEmojiRainData(v);

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
        i = 0;
        aMap.getUiSettings().setAllGesturesEnabled(false);
    }

    private void stopFogModel(){
        myView.setVisibility(myView.GONE);
        btn.setVisibility(btn.VISIBLE);
        mQuit_fog_btn.setVisibility(mQuit_fog_btn.GONE);
        aMap.getUiSettings().setAllGesturesEnabled(true);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
    }

//-----------------------------Serach--------------------------------------------------
    /*
     * 寮�濮嬭繘琛宲oi鎼滅储
     */
    protected void doSearchQuery() {
        keyWord = mSearchText.getText().toString().trim();
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");
        query.setPageSize(20);
        query.setPageNum(currentPage);

        if (lp != null) {
            poiSearch = new PoiSearch(getActivity(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//

            poiSearch.searchPOIAsyn();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {
                    poiResult = result;
                    poiItems = poiResult.getPois();
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();
                    if (poiItems != null && poiItems.size() > 0) {

                        whetherToShowDetailInfo(false);

                        if (mlastMarker != null) {
                            resetlastmarker();
                        }

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


    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(getActivity(), infomation);

    }


    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
        public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }


        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }


        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }


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


        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }


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
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        //aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setMyLocationEnabled(true); //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
       // aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
    }

    private void removeAMapLogo() {

        UiSettings uiSettings =  aMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-50);
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

        mapView.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();

        mapView.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        x = amapLocation.getLatitude();
        y = amapLocation.getLongitude();

        lp.setLatitude(x);
        lp.setLongitude(y);
        //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));
        if (mIsStartBlack) {
            if (amapLocation.getErrorCode() == 0) {

                pos = new LatLng(x,y);
                Projection projection = aMap.getProjection();

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
            mUserNickName.setText("当前用户：" + USER_NAME);
        }
        if (null != GROUP_NAME && !GROUP_NAME.equals("")) {
            mGroupName.setText("所在分组：" + GROUP_NAME);
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
                //--------------------------------------------------------------
                btn.setVisibility(btn.VISIBLE);
                mStartLocation.setVisibility(mStartLocation.GONE);
                mStopLocation.setVisibility(mStopLocation.VISIBLE);
                //-----------------------------------------------------------------
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
                //-------------------------鑳滆揪-------------------------------------
                if (btn.getVisibility() == View.GONE){
                    myView.setVisibility(myView.GONE);
                    mQuit_fog_btn.setVisibility(btn.GONE);
                }else {
                    btn.setVisibility(btn.GONE);
                }
                mStartLocation.setVisibility(mStartLocation.VISIBLE);
                mStopLocation.setVisibility(mStopLocation.GONE);
                aMap.getUiSettings().setAllGesturesEnabled(true);
                aMap.getUiSettings().setRotateGesturesEnabled(false);
                aMap.getUiSettings().setTiltGesturesEnabled(false);
                //-----------------------------------------------------------------
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
                //-----------------------鑳滆揪------------------------------------------------
                mStartUpload.setVisibility(mStartUpload.GONE);
                mStopUpload.setVisibility(mStopUpload.VISIBLE);
                //--------------------------------------------------------------------------
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
                //-----------------------鑳滆揪------------------------------------------------
                mStartUpload.setVisibility(mStartUpload.VISIBLE);
                mStopUpload.setVisibility(mStopUpload.GONE);
                //--------------------------------------------------------------------------
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

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

        outState.putBoolean(KEY_START_BLACK, mIsStartBlack);
        outState.putBoolean(KEY_START_LOCATION, mIsStartLocation);
        outState.putBoolean(KEY_START_UPLOAD, mIsStartUpload);

        Log.d(TAG, "onSaveInstanceState: ");
    }
}
