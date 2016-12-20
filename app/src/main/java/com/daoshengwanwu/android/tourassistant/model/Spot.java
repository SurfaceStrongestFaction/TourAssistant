package com.daoshengwanwu.android.tourassistant.model;


import java.util.UUID;


public class Spot {
    private UUID mId;
    private int mDrawableResId;
    private String mSpotName;
    private String mSpotEnName;
    private int mRecommandNum;
    private int mDistance;


    public Spot(int drawableResId) {
        this(drawableResId, "", "", 0, 0);
    }

    public Spot(int drawableResId, String spotName, String spotEnName, int recommandNum, int distance) {
        mId = UUID.randomUUID();
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

    public UUID getId() {
        return mId;
    }
}
