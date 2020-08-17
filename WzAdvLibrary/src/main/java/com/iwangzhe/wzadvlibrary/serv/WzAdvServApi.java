package com.iwangzhe.wzadvlibrary.serv;

import com.alibaba.fastjson.JSON;
import com.iwangzhe.wzadvlibrary.WzAdvApplication;
import com.iwangzhe.wzadvlibrary.model.CommonRes;
import com.iwangzhe.wzadvlibrary.model.JAdvInfo;
import com.iwangzhe.wzadvlibrary.model.JBase;
import com.iwangzhe.wzcorelibrary.IResCallback;
import com.snappydb.SnappydbException;

import java.util.HashMap;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:38
 * desc   :
 */
public class WzAdvServApi {
    private static WzAdvServApi mWzAdvServApi = null;

    public static WzAdvServApi getInstance() {
        synchronized (WzAdvServApi.class) {
            if (mWzAdvServApi == null) {
                mWzAdvServApi = new WzAdvServApi();
            }
        }
        return mWzAdvServApi;
    }

    public <T extends JBase> void getAdverts(final Class<T> clazz, String pageKey, String posKey, final IResParseCallback<T> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageKey", pageKey);
        params.put("posKey", posKey);
        WzAdvApplication.getInstance().getmNetHttp().reqGetResByWzApi("adv/position/fetch/", params, new IResCallback() {
            @Override
            public void onResult(String result) {
                CommonRes<T> cRes;
                T resObj;
                try {
                    resObj = JSON.parseObject(result, clazz);
                } catch (Exception e) {
                    resObj = null;
                }
                if (resObj == null) {
                    cRes = new CommonRes<>(false, "");
                } else {
                    JBase tmp = (JBase) resObj;
                    cRes = new CommonRes<>(true, tmp.getErrorCode(), resObj, result);
                }
                callback.onFinish(cRes);
            }
        });
    }

    public void reportAdvDisplay(int mapId, int pos, int total, final IResCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("mapId", "" + mapId);
        params.put("pos", "" + pos);
        params.put("total", "" + total);
        WzAdvApplication.getInstance().getmNetHttp().reqGetResByWzApi("adv/position/show/", params, new IResCallback() {
            @Override
            public void onResult(String result) {
                callback.onResult(result);
            }
        });
    }

    public CommonRes<JAdvInfo> getJAdvInfoFromDb(String key) {
        JAdvInfo jAdvInfo;
        try {
            jAdvInfo = WzAdvApplication.getInstance().getmIoKvdb().getObject(WzAdvApplication.getInstance().getModName() + ":" + key, JAdvInfo.class);
        } catch (SnappydbException e) {
            return new CommonRes<>(false);
        }
        if (jAdvInfo == null) {
            return new CommonRes<>(true, 10001);
        }
        return new CommonRes<>(true, 0, jAdvInfo);
    }

    public CommonRes<JAdvInfo> setAdvInfoToDb(String key, JAdvInfo item) {
        try {
            WzAdvApplication.getInstance().getmIoKvdb().put(WzAdvApplication.getInstance().getModName() + ":" + key, item);
        } catch (SnappydbException e) {
            return new CommonRes<>(false);
        }
        return new CommonRes<>(true);
    }

}
