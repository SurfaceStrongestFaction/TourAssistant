package com.daoshengwanwu.android.tourassistant.model;


public class HomeListSpotItem extends SpotItemBase {
    private String mId;
    private int mSpotRate;
    private int mDistance;
    private String mSpotImgUrl;
    private String mSpotName_cn;
    private String mSpotName_en;


    public HomeListSpotItem(String id, String spotImgUrl, String spotName_cn, String spotName_en, int spotRate, int distance) {
        super(SpotItemType.SPOT_ITEM);

        mId = id;
        mSpotRate = spotRate;
        mDistance = distance;
        mSpotImgUrl = spotImgUrl;
        mSpotName_cn = spotName_cn;
        mSpotName_en = spotName_en;
    }

    public int getSpotRate() {
        return mSpotRate;
    }

    public void setSpotRate(int spotRate) {
        mSpotRate = spotRate;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public String getSpotImgUrl() {
        return mSpotImgUrl;
    }

    public void setSpotImgUrl(String spotImgUrl) {
        mSpotImgUrl = spotImgUrl;
    }

    public String getSpotName_cn() {
        return mSpotName_cn;
    }

    public void setSpotName_cn(String spotName_cn) {
        mSpotName_cn = spotName_cn;
    }

    public String getSpotName_en() {
        return mSpotName_en;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setSpotName_en(String spotName_en) {
        mSpotName_en = spotName_en;
    }
}
