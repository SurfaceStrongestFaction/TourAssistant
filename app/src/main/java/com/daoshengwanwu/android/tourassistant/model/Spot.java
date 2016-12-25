package com.daoshengwanwu.android.tourassistant.model;


public class Spot {
    private String mId;
    private String mImgUrl;
    private String mLargeImg;
    private String mSpotName;
    private String mSpotEnName;
    private int mRecommandNum;
    private int mDistance;


    public Spot(String spot_id, String imgUrl, String largeImg) {
        this(spot_id, imgUrl, largeImg, "", "", 0, 0);
    }

    public Spot(String spot_id, String imgUrl, String largeImg, String spotName, String spotEnName, int recommandNum, int distance) {
        mId = spot_id;
        mImgUrl = imgUrl;
        mSpotName = spotName;
        mSpotEnName = spotEnName;
        mRecommandNum = recommandNum;
        mLargeImg = largeImg;
        mDistance = distance;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLargeImg() {
        return mLargeImg;
    }

    public void setLargeImg(String largeImg) {
        mLargeImg = largeImg;
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
