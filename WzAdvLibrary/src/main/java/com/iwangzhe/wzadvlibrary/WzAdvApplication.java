package com.iwangzhe.wzadvlibrary;

import android.content.Context;

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
    private INetHttp mNetHttp;
    private IIoKvdb mIoKvdb;
    private IRouter mRouter;
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

    public void init(INetHttp netHttp, IIoKvdb ioKvdb, IRouter router, Map<Object, Object> oprion) {
        this.mNetHttp = netHttp;
        this.mIoKvdb = ioKvdb;
        this.mRouter = router;
        this.mOption = oprion;

    }

    public void createAdv(Context context, String pageKey, String posKey, AdvView view) {
        WzAdvControlApp.getInstance().createAdv(context, pageKey, posKey, view);
    }

    public boolean foundAdv(String pageKey, String posKey) {
        return false;
    }

    public INetHttp getmNetHttp() {
        return mNetHttp;
    }

    public IIoKvdb getmIoKvdb() {
        return mIoKvdb;
    }


    public IRouter getmRouter() {
        return mRouter;
    }

    public Map<Object, Object> getmOption() {
        return mOption;
    }

    public String getModName() {
        return "WzAdvApplication";
    }
}
