package com.iwangzhe.wzadvlibrary.model;

import java.util.HashMap;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:52
 * desc   :
 */
public class WzAdvModelApi {
    private static WzAdvModelApi mWzAdvModelApi = null;


    public static WzAdvModelApi getInstance() {
        synchronized (WzAdvModelApi.class) {
            if (mWzAdvModelApi == null) {
                mWzAdvModelApi = new WzAdvModelApi();
            }
        }
        return mWzAdvModelApi;
    }

    private Map<String, JAdvInfo> advInfoMap;
    private SplashAdInfo splashAdInfo;

    public WzAdvModelApi() {
        advInfoMap = new HashMap<>();
        splashAdInfo = new SplashAdInfo();
    }

    public Map<String, JAdvInfo> getAdvInfoMap() {
        return advInfoMap;
    }

    public SplashAdInfo getSplashAdInfo() {
        return splashAdInfo;
    }

    public void setSplashAdInfo(SplashAdInfo splashAdInfo) {
        this.splashAdInfo = splashAdInfo;
    }

    public void setAdvInfoMap(Map<String, JAdvInfo> advInfoMap) {
        this.advInfoMap = advInfoMap;
    }

}
