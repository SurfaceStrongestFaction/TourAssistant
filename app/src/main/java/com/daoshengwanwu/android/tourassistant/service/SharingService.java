package com.daoshengwanwu.android.tourassistant.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil;
import com.daoshengwanwu.android.tourassistant.utils.AppUtil.SharingServer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SharingService extends Service {
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private SharingBinder mUniqueBinder = null;
    private AMapLocationClient mLocationClient = null;
    private boolean mNeedRequestTeam = true;
    private Set<OnTeamChangeListener> mOnTeamChangeListeners = new HashSet<>();
    private Set<OnTeamMemberChangeListener> mOnTeamMemberChangeListeners = new HashSet<>();
    private static final String TAG = "SharingService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (null == mUniqueBinder) {
            mUniqueBinder = new SharingBinder();
        }

        return mUniqueBinder;
    }

    @Override
    public void onDestroy() {
        if (null == mUniqueBinder) {
            super.onDestroy();
            mLatitude = 0.0;
            mLongitude = 0.0;
            mLocationClient = null;
            return;
        }

        if (mUniqueBinder.isUploadStart()) {
            mUniqueBinder.stopUploadLocation();
        }

        if (null != mLocationClient) {
            if (mUniqueBinder.isStartLocation()) {
                mLocationClient.stopLocation();
            }

            mLocationClient.onDestroy();
            mLocationClient = null;
        }

        mUniqueBinder = null;

        stopRequestTeamInfo();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startRequestTeamInfo();
    }

    public void stopRequestTeamInfo() {
        mNeedRequestTeam = false;
    }

    public void startRequestTeamInfo() {
        mNeedRequestTeam = true;

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {

                    OkHttpClient client = new OkHttpClient();
                    List<String> oldMemInfo = generateMemIdsList(new String[0]);

                    while (mNeedRequestTeam) {
                        String user_id = AppUtil.User.USER_ID;

                        if (user_id != null && !user_id.equals("")) {
                            RequestBody requestBody = new FormBody.Builder().add("user_id", user_id).build();
                            Request request = new Request.Builder()
                                    .url("http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/user/getInformation")
                                    .post(requestBody)
                                    .build();

                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();

                            JSONObject jObj = new JSONObject(responseData);
                            String team_id = (String)jObj.get("team_id");

                            String old_team_id = AppUtil.Group.GROUP_ID;
                            if (!team_id.equals(old_team_id)) {
                                AppUtil.Group.GROUP_ID = team_id;

                                synchronized (mOnTeamChangeListeners) {
                                    for (OnTeamChangeListener listener : mOnTeamChangeListeners) {
                                        listener.onTeamChange(team_id);
                                    }
                                }//sync
                            }//if

                            requestBody = new FormBody.Builder().add("team_id", team_id).build();
                            request = new Request.Builder()
                                    .url("http://" + AppUtil.JFinalServer.HOST + ":" + AppUtil.JFinalServer.PORT + "/team/getInformation")
                                    .post(requestBody)
                                    .build();

                            response = client.newCall(request).execute();
                            responseData = response.body().string();

                            Log.d(TAG, "run: team:" + responseData);
                            jObj = new JSONObject(responseData);

                            String membersInfo = jObj.getString("members");
                            List<String> memInfos = generateMemIdsList(membersInfo.split(","));

                            Log.d(TAG, "run: membersInfo: " + membersInfo);

                            if (!memInfos.equals(oldMemInfo)) {
                                Log.d(TAG, "run: 进入if");
                                for (OnTeamMemberChangeListener listener : mOnTeamMemberChangeListeners) {
                                    listener.onTeamMemberChange(team_id, memInfos);
                                }

                                oldMemInfo = memInfos;
                            }
                        }//if

                        Thread.sleep(1000);
                    }//while
                } catch (Exception e) {
                    e.printStackTrace();
                }//try-catch
            } //run
        }.start();
    }//startRequestTeamInfo

    private List<String> generateMemIdsList(String[] members_id_array) {
        List<String> list = new ArrayList<>();

        for (String member_id : members_id_array) {
            list.add(member_id);
        }

        return list;
    }

    public static Intent newIntent(Context applicationContext) {
        return new Intent(applicationContext, SharingService.class);
    }

    public static void actionStartService(Context applicationContext) {
        applicationContext.startService(new Intent(applicationContext, SharingService.class));
    }

    public static boolean actionBindService(Context packageContext, ServiceConnection serviceConnection) {
        return packageContext.bindService(newIntent(packageContext), serviceConnection, BIND_AUTO_CREATE);
    }


    public interface SharingLocationListener {
        void onLocationsDataArrived(HashMap<String, double[]> locationsData);
    }


    public interface OnTeamChangeListener {
        void onTeamChange(String team_id);
    }


    public interface OnTeamMemberChangeListener {
        void onTeamMemberChange(String team_id, List<String> memberIds);
    }


    public final class SharingBinder extends Binder implements Serializable, AMapLocationListener {
        private boolean mIsUploadStarting = false; //标识当前是否正在开启上传服务
        private boolean mIsStartingLocation = false; //标识当前是否正在开启定位服务
        private boolean mIsUploadStart = false; //标识当前是否已经开启上传服务
        private boolean mIsStartLocation = false; //标识当前是否已经开启定位服务
        private Socket mClient = null; //连接服务器的套接字
        private boolean mNeedLoop = false; //标识上传位置线程以及读取指令两个线程是否需要继续循环。
        private BufferedReader mInfoReader = null; //接收服务器端发来的指令
        private BufferedWriter mInfoReporter = null; //向服务器发送指令
        //记录着每个成员的位置信息，key为user的user_id, value是一个double数组，其中double[0]位latitude， double[1]为longitude
        //保存着所有注册到该服务的监听者
        private Set<AMapLocationListener> mLocationListeners = new HashSet<>();
        private Set<SharingLocationListener> mSharingLocationListeners = new HashSet<>();


        public boolean isStartingLocation() {
            return mIsStartingLocation;
        }

        public boolean isUploadStarting() {
            return mIsUploadStarting;
        }

        public boolean isStartLocation() {
            return mIsStartLocation;
        }

        public boolean isUploadStart() {
            return mIsUploadStart;
        }

        public void registerLocationListener(AMapLocationListener locationListener) {
            mLocationListeners.add(locationListener);
        }

        public void unregisterLocationListener(AMapLocationListener locationListener) {
            mLocationListeners.remove(locationListener);
        }

        public void registerSharingLocationListener(SharingLocationListener listener) {
            mSharingLocationListeners.add(listener);
        }

        public void unregisterSharginLocationListener(SharingLocationListener listener) {
            mSharingLocationListeners.remove(listener);
        }

        public void registerTeamChangeListener(OnTeamChangeListener listener) {
            synchronized (mOnTeamChangeListeners) {
                mOnTeamChangeListeners.add(listener);
            }
        }

        public void unregisterTeamChangeListener(OnTeamChangeListener listener) {
            synchronized (mOnTeamChangeListeners) {
                mOnTeamChangeListeners.remove(listener);
            }
        }

        public void registerOnTeamMemberChangeListener(OnTeamMemberChangeListener listener) {
            mOnTeamMemberChangeListeners.add(listener);
        }

        public void unregisterOnTeamMemberChangeListener(OnTeamMemberChangeListener listener) {
            mOnTeamMemberChangeListeners.remove(listener);
        }

        public void startLocationService() {
            mIsStartingLocation = true;
            //获取定位客户端以及开启定位服务
            if (!mIsStartLocation) {
                if (null == mLocationClient) {
                    mLocationClient = new AMapLocationClient(getApplicationContext()); //首先实例化一个定位客户端（由高德SDK提供）
                }

                if (!mLocationClient.isStarted()) {
                    mLocationClient.setLocationListener(this);
                    mLocationClient.startLocation();
                }

                mIsStartLocation = true;
            }
        }

        public void stopLocationService() {
            Log.d(TAG, "stopLocationService: mLocationClient:" + mLocationClient
                + ", isStartingLocation:" + isStartingLocation() + ", isUploadStarting:" + isUploadStarting()
                + ", isUploadStart:" + isUploadStart() + ", isLocationStarted:" + mLocationClient.isStarted());

            if (null == mLocationClient ||  isUploadStart()) {
                return;
            }

            mLocationClient.stopLocation();
            mLocationClient = null;
            mIsStartingLocation = false;
            mIsStartLocation = false;
            Log.d(TAG, "stopLocationService: 成功关闭定位信息");
        }

        public void stopUploadLocation() {
            if (mIsUploadStart && null != mClient) {
                mNeedLoop = false; //将循环标志置为false
            }
        }

        public void startUploadLocation() throws IOException { //开启上传位置服务
            if (!mIsUploadStarting && !mIsUploadStart && isStartLocation()) {
                mIsUploadStarting = true;
                //在新线程中实例化与ServerSocket服务器通讯的客户端Socket
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            instanceSocket(); //实例化Socket对象
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /*
                            在这里应该有这样一个步骤：
                                获取当前登录的唯一标识用户的user_id
                            由于登录功能还没有实现，所以这里先假定一个user_id
                         */
                        String user_id = AppUtil.User.USER_ID; //假定一个user_id
                        String group_id = AppUtil.Group.GROUP_ID; //假定一个group_id

                        //向服务器发送user_id信息,该步骤为必须步骤，服务器必须接收到该唯一id才可将Socket与id绑定
                        try {
                            sendCommandToServer(SharingServer.COMMAND_SET_USERID, user_id);
                            sendCommandToServer(SharingServer.COMMAND_SET_GROUPID, group_id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //开启一个新线程，用来监听服务端发来的消息
                        mNeedLoop = true; //首先要将循环标志置为true

                        //开启一个接收ServerSocket服务器指令的线程，该线程只有在接收到服务器结束指令时才会停止.
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    while (true) {
                                        String info = getInfoFromServer(); //从服务器读取一条指令
                                        switch (getInfoCommand(info)) {
                                            case SharingServer.RECEIVED_MEMBER_LOCATIONS:
                                                String content = getInfoContent(info);
                                                HashMap<String, double[]> groupMemberLocations = analysisLocationData(content);
                                                for (SharingLocationListener listener : mSharingLocationListeners) {
                                                    listener.onLocationsDataArrived(groupMemberLocations);
                                                }
                                                break;
                                            case SharingServer.REQUEST_STOP:
                                                mClient.close();
                                                mClient = null;
                                                mInfoReader = null;
                                                mInfoReporter = null;
                                                mIsUploadStart = false;
                                                mIsUploadStarting = false;
                                                return;
                                            default:
                                                break;
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                        //开启一个线程，每秒向服务器上传自己的位置，并且请求一次组内成员的位置
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    while (mNeedLoop) {
                                        //向服务器上传自己的位置
                                        sendCommandToServer(SharingServer.COMMAND_SET_LOCATION, getCurrentLocationString());
                                        //向服务器请求组内成员的位置
                                        sendRequestToServer(SharingServer.REQUEST_MEMBER_LOCATION);

                                        Thread.sleep(2000);
                                    }

                                    //当循环跳出时说明mNeedLoop已经置为false，此时说明外部已经调用stopSharingLocation()方法
                                    //所以向服务发送结束请求
                                    sendRequestToServer(SharingServer.REQUEST_STOP);
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }.start();

                mIsUploadStart = true;
            }
        }

        private String getCurrentLocationString() {
            return mLatitude + SharingServer.SEPARATOR_LOCATION_LAT_LON + mLongitude;
        }

        private void clearLocationListeners() {
            mLocationListeners.clear();
        }

        private void clearSharingLocationListeners() {
            mSharingLocationListeners.clear();
        }

        private void closeStream() throws IOException {
            mInfoReporter.close();
            mInfoReader.close();
            mInfoReporter = null;
            mInfoReader = null;
        }

        private void sendCommandToServer(String commandCode, String info) throws IOException { //向服务器发送一条信息
            info = commandCode + SharingServer.SEPARATOR_COMMAND_CONTENT + info + "\n";
            mInfoReporter.write(info);
            mInfoReporter.flush();
        }

        private void sendRequestToServer(String requestCode) throws IOException {
            mInfoReporter.write(requestCode + "\n");
            mInfoReporter.flush();
        }

        private String getInfoFromServer() throws IOException { //从服务器获取一条信息
            return mInfoReader.readLine();
        }

        private void instanceSocket() throws IOException { //实例化Socket对象
            mClient = new Socket(SharingServer.HOST, SharingServer.PORT);
            mInfoReader = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
            mInfoReporter = new BufferedWriter(new OutputStreamWriter(mClient.getOutputStream()));
        }

        private String getInfoCommand(String info) {
            if (null == info) {
                return "";
            }

            return info.split(":")[0];
        }

        private String getInfoContent(String info) {
            return info.split(":")[1];
        }

        /**
         * 功能：解析位置信息
         * @param locationData
         *      locationData: 记录着定位信息的字符串，格式："user_id->latitude,longitude#user_id->..."
         *
         * 执行结果：填充私有成员：mGroupMemberLocations
         */
        private HashMap<String, double[]> analysisLocationData(String locationData) {
            HashMap<String, double[]> groupMemberLocations = new HashMap<>();

            String[] membersLocation = locationData.split(SharingServer.SEPARATOR_LOCATION_DIFFER_MEMBER);
            for (String memberLoc : membersLocation) {
                String[] userId_Location = memberLoc.split(SharingServer.SEPARATOR_LOCATION_ID_LOC);
                String[] lat_lng_str = userId_Location[1].split(SharingServer.SEPARATOR_LOCATION_LAT_LON);

                String user_id = userId_Location[0];
                double latitude = Double.parseDouble(lat_lng_str[0]);
                double longitude = Double.parseDouble(lat_lng_str[1]);
                double[] lat_lng = {latitude, longitude};

                groupMemberLocations.put(user_id, lat_lng);
            }

            return groupMemberLocations;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mLatitude = aMapLocation.getLatitude();
            mLongitude = aMapLocation.getLongitude();

            //调用每个注册的监听者的onLocationChanged方法
            //这么做的好处是对于位置改变的回调定制可以写在主调对象中
            // Toast.makeText(SharingService.this, "aMapLocation: " + aMapLocation.toString(), Toast.LENGTH_SHORT).show();
            for (AMapLocationListener locationListener : mLocationListeners) {
                locationListener.onLocationChanged(aMapLocation);
            }
        }
    }
}
