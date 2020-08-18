package com.iwangzhe.wzadvlibrary;

import android.app.Activity;

import com.iwangzhe.wzadvlibrary.control.WzAdvControlApp;
import com.iwangzhe.wzadvlibrary.model.WzAdvModelApi;
import com.iwangzhe.wzadvlibrary.serv.WzAdvServApi;
import com.iwangzhe.wzadvlibrary.tool.WzAdvTool;
import com.iwangzhe.wzadvlibrary.view.AdvView;
import com.iwangzhe.wzcorelibrary.IIoKvdb;
import com.iwangzhe.wzcorelibrary.INetHttp;
import com.iwangzhe.wzcorelibrary.IRouter;

import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:29
 * desc   :
 */
public class WzAdvApplication {
    private static WzAdvApplication mWzAdvApplication = null;
    private Map<Object, Object> mOption;

    public static WzAdvApplication getInstance() {
        synchronized (WzAdvApplication.class) {
            if (mWzAdvApplication == null) {
                mWzAdvApplication = new WzAdvApplication();
            }
        }
        return mWzAdvApplication;
    }

    public WzAdvApplication() {
        WzAdvTool.getInstance();
        WzAdvModelApi.getInstance();
        WzAdvServApi.getInstance();
        WzAdvControlApp.getInstance();
    }

    public void init(INetHttp netHttp, IIoKvdb ioKvdb, IRouter router, Map<Object, Object> option) {
        this.mOption = option;
        WzAdvServApi.getInstance().init(netHttp, ioKvdb, router);

    }

    public void preLoadAdv(String pageKey, String posKey, Map<Object, Object> option) {
        WzAdvControlApp.getInstance().loadAdv(pageKey, posKey, option, null);
    }

    public void createAdv(Activity activity, String pageKey, String posKey, AdvView view, Map<Object, Object> option) {
        WzAdvControlApp.getInstance().createAdv(activity, pageKey, posKey, view, option);
    }

    public boolean isAdvExist(String pageKey, String posKey) {
        return WzAdvControlApp.getInstance().isAdvExist(pageKey, posKey);
    }

    public Map<Object, Object> getmOption() {
        return mOption;
    }

    public void setmOption(Map<Object, Object> mOption) {
        this.mOption = mOption;
    }
}
