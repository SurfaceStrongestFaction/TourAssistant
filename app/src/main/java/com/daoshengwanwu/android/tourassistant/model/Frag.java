package com.daoshengwanwu.android.tourassistant.model;

/**
 * Created by 白浩然 on 2016/12/24.
 */

public class Frag {
    private double mLatitude;
    private double mLongitude;
    private int mResId;

    public Frag(double latitude, double longitude, int resId) {
        mLatitude = latitude;
        mLongitude = longitude;
        mResId = resId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }
}
