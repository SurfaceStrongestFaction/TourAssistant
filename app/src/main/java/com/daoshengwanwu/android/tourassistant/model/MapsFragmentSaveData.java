package com.daoshengwanwu.android.tourassistant.model;

import java.io.Serializable;

/**
 * Created by 白浩然 on 2016/12/22.
 */

public class MapsFragmentSaveData implements Serializable {
    private boolean mIsStartLocation = false;
    private boolean mIsStartUpload = false;
    private boolean mIsStartBlack = false;

    public boolean isStartBlack() {
        return mIsStartBlack;
    }

    public void setStartBlack(boolean startBlack) {
        mIsStartBlack = startBlack;
    }

    public boolean isStartLocation() {
        return mIsStartLocation;
    }

    public void setStartLocation(boolean startLocation) {
        mIsStartLocation = startLocation;
    }

    public boolean isStartUpload() {
        return mIsStartUpload;
    }

    public void setStartUpload(boolean startUpload) {
        mIsStartUpload = startUpload;
    }
}
