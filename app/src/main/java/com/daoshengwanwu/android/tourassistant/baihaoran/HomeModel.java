package com.daoshengwanwu.android.tourassistant.baihaoran;


import com.daoshengwanwu.android.tourassistant.R;

import java.util.ArrayList;
import java.util.List;


public class HomeModel {
    private List<Spot> mSpots = new ArrayList<>();
    private static HomeModel sHomeModel = null;


    private HomeModel() { //初始化添加死数据
        for (int i = 0; i < 100; i++) {
            Spot spot = new Spot(R.drawable.imperialpalace);
            spot.setDistance(i * 100 + 1020);
            spot.setRecommandNum(5);
            spot.setSpotEnName("Imperial palace");
            spot.setSpotName("故宫");
            mSpots.add(spot);
        }
    }

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
