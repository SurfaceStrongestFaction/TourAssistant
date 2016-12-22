package com.daoshengwanwu.android.tourassistant.model;


public class Spot {
    private String mId;
    private int mDrawableResId;
    private String mSpotName;
    private String mSpotEnName;
    private int mRecommandNum;
    private int mDistance;


    public Spot(String spot_id, int drawableResId) {
        this(spot_id, drawableResId, "", "", 0, 0);
    }

    public Spot(String spot_id, int drawableResId, String spotName, String spotEnName, int recommandNum, int distance) {
        mId = spot_id;
        mDrawableResId = drawableResId;
        mSpotName = spotName;
        mSpotEnName = spotEnName;
        mRecommandNum = recommandNum;
        mDistance = distance;
    }

    public int getDrawableResId() {
        return mDrawableResId;
    }

    public void setDrawableResId(int drawableResId) {
        mDrawableResId = drawableResId;
    }

    public String getSpotName() {
        return mSpotName;
    }

    public void setSpotName(String spotName) {
        mSpotName = spotName;
    }

    public String getSpotEnName() {
        return mSpotEnName;
    }

    public void setSpotEnName(String spotEnName) {
        mSpotEnName = spotEnName;
    }

    public int getRecommandNum() {
        return mRecommandNum;
    }

    public void setRecommandNum(int recommandNum) {
        mRecommandNum = recommandNum;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public String getId() {
        return mId;
    }
}
