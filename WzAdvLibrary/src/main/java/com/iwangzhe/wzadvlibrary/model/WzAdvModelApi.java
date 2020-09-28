package com.iwangzhe.wzadvlibrary.model;

import com.iwangzhe.wzadvlibrary.WzAdvApplication;
import com.iwz.WzFramwork.base.api.ModelApi;

import java.util.HashMap;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:52
 * desc   :
 */
public class WzAdvModelApi extends ModelApi {
    private static WzAdvModelApi mWzAdvModelApi = null;


    public static WzAdvModelApi getInstance(WzAdvApplication main) {
        synchronized (WzAdvModelApi.class) {
            if (mWzAdvModelApi == null) {
                mWzAdvModelApi = new WzAdvModelApi(main);
            }
        }
        return mWzAdvModelApi;
    }

    private Map<String, JAdvInfo> advInfoMap;
    private SplashAdInfo splashAdInfo;

    private WzAdvModelApi(WzAdvApplication main) {
        super(main);
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
