package com.daoshengwanwu.android.tourassistant.model;


import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;
import java.util.List;


public class HomeModel {
    private List<Spot> mSpots = new ArrayList<>();
    private static HomeModel sHomeModel = null;


    public List<Spot> getSpots() {
        return mSpots;
    }

    public static HomeModel getInstance() {
        if (null == sHomeModel) {
            sHomeModel = new HomeModel();
        }

        return sHomeModel;
    }
}
