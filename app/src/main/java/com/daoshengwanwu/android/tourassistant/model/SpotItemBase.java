package com.daoshengwanwu.android.tourassistant.model;


public abstract class SpotItemBase {
    private int mSpotItemType;

    public SpotItemBase(int spotItemType) {
        mSpotItemType = spotItemType;
    }

    public int getSpotItemType() {
        return mSpotItemType;
    }


    public static final class SpotItemType {
        public static final int BANNER = 0x00000000;
        public static final int SEPARATOR = 0x00000001;
        public static final int SPOT_ITEM = 0x00000002;
    }
}
