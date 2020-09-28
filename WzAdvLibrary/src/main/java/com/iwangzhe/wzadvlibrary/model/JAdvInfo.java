package com.iwangzhe.wzadvlibrary.model;


import com.iwz.WzFramwork.base.JBase;

import java.util.ArrayList;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:43
 * desc   :
 */
public class JAdvInfo extends JBase {
    private PositionInfo positionInfo;
    private ArrayList<AdvertplanList> planList;

    public JAdvInfo() {
        this.positionInfo = new PositionInfo();
        this.planList = new ArrayList<>();
    }

    public PositionInfo getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(PositionInfo positionInfo) {
        this.positionInfo = positionInfo;
    }

    public ArrayList<AdvertplanList> getPlanList() {
        return planList;
    }

    public void setPlanList(ArrayList<AdvertplanList> planList) {
        this.planList = planList;
    }

}
