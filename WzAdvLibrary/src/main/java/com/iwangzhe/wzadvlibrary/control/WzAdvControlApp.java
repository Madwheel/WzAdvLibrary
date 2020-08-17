package com.iwangzhe.wzadvlibrary.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;


import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iwangzhe.wzadvlibrary.WzAdvApplication;
import com.iwangzhe.wzadvlibrary.model.AdvertplanList;
import com.iwangzhe.wzadvlibrary.model.CommonRes;
import com.iwangzhe.wzadvlibrary.model.JAdvInfo;
import com.iwangzhe.wzadvlibrary.model.WzAdvModelApi;
import com.iwangzhe.wzadvlibrary.serv.IResParseCallback;
import com.iwangzhe.wzadvlibrary.serv.OnWzAdvViewPagerListener;
import com.iwangzhe.wzadvlibrary.serv.WzAdvServApi;
import com.iwangzhe.wzadvlibrary.tool.CornerTransform;
import com.iwangzhe.wzadvlibrary.tool.WzAdvTool;
import com.iwangzhe.wzadvlibrary.view.AdvView;
import com.iwangzhe.wzadvlibrary.view.WzAdvViewPager;
import com.iwangzhe.wzcorelibrary.IResCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:38
 * desc   :
 */
public class WzAdvControlApp {
    private static WzAdvControlApp mWzAdvControlApp = null;

    public static WzAdvControlApp getInstance() {
        synchronized (WzAdvControlApp.class) {
            if (mWzAdvControlApp == null) {
                mWzAdvControlApp = new WzAdvControlApp();
            }
        }
        return mWzAdvControlApp;
    }

    private Map<String, Long> reportMap;

    public WzAdvControlApp() {
        reportMap = new HashMap<>();
    }

    /**
     * 初始化广告信息
     *
     * @param pageKey
     * @param posKey
     */
    public void createAdv(final Context context, final String pageKey, final String posKey, final AdvView view) {
        //1、查看缓存中是否有，有则显示
        displayAdvView(context, pageKey, posKey, view);
        //2、网络请求，拿到数据刷新页面
        WzAdvServApi.getInstance().getAdverts(JAdvInfo.class, pageKey, posKey, new IResParseCallback<JAdvInfo>() {
            @Override
            public void onFinish(CommonRes<JAdvInfo> res) {
                if (res.isOk()) {
                    JAdvInfo resObj = res.getResObj();
                    Map<String, JAdvInfo> advInfoMap = WzAdvModelApi.getInstance().getAdvInfoMap();
                    advInfoMap.put(pageKey + posKey, resObj);
                    WzAdvModelApi.getInstance().setAdvInfoMap(advInfoMap);
                    WzAdvServApi.getInstance().setAdvInfoToDb(pageKey + posKey, resObj);
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            displayAdvView(context, pageKey, posKey, view);
                        }
                    });
                }
            }
        });
    }

    /**
     * 是否存在广告
     *
     * @param pageKey
     * @param posKey
     * @return
     */
    public boolean foundAdv(String pageKey, String posKey) {
        CommonRes<JAdvInfo> jAdvInfoFromDb = WzAdvServApi.getInstance().getJAdvInfoFromDb(pageKey + posKey);
        if (jAdvInfoFromDb.isOk()) {
            JAdvInfo resObj = jAdvInfoFromDb.getResObj();
            if (resObj != null && resObj.getPlanList().size() > 0) {
                Map<String, JAdvInfo> advInfoMap = WzAdvModelApi.getInstance().getAdvInfoMap();
                advInfoMap.put(pageKey + posKey, resObj);
                WzAdvModelApi.getInstance().setAdvInfoMap(advInfoMap);
                return true;
            }
        }
        Map<String, JAdvInfo> advInfoMap = WzAdvModelApi.getInstance().getAdvInfoMap();
        if (advInfoMap == null && advInfoMap.size() == 0) {
            return false;
        }
        JAdvInfo jAdvInfo = advInfoMap.get(pageKey + posKey);
        if (jAdvInfo != null && jAdvInfo.getPlanList().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void displayAdvView(Context context, String pageKey, String posKey, AdvView view) {
        if (foundAdv(pageKey, posKey)) {
            AdvView adView = getView(pageKey, posKey, context);
            view.setVisibility(View.VISIBLE);
            view.removeAllViews();
            view.addView(adView);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    private AdvView getView(String pageKey, String posKey, Context context) {
        boolean foundAdv = foundAdv(pageKey, posKey);
        if (!foundAdv) {
            return null;
        }
        JAdvInfo jAdvInfo = WzAdvModelApi.getInstance().getAdvInfoMap().get(pageKey + posKey);
        int advType = jAdvInfo.getPositionInfo().getAdvType();
        if (advType == 4) {
            return getWzAdvViewPager(context, jAdvInfo);
        } else if (advType == 6) {//开屏广告
            return getWzAdvLaunchView(context, jAdvInfo);
        }
        return null;
    }

    private AdvView getWzAdvLaunchView(Context context, JAdvInfo jAdvInfo) {
        return null;
    }

    @NonNull
    private WzAdvViewPager getWzAdvViewPager(Context context, JAdvInfo jAdvInfo) {
        WzAdvViewPager slideShowView = new WzAdvViewPager(context);
        final List<Integer> reportList = new ArrayList<>();
        ArrayList<AdvertplanList> planList = jAdvInfo.getPlanList();
        if (planList.size() > 0) {
            //添加图片到图片列表里
            List<String> imageUrlList = new ArrayList<>();
            List<Integer> imageMapIdList = new ArrayList<>();
            List<String> jumpUrlList = new ArrayList<>();
            for (int i = 0; i < planList.size(); i++) {
                jumpUrlList.add(planList.get(i).getJumpUrl());
                JSONObject jsonObject = WzAdvTool.getInstance().getJSONObject(planList.get(i).getPics());
                String string = jsonObject.getString("img_" + jAdvInfo.getPositionInfo().getWidth() + "x" + jAdvInfo.getPositionInfo().getHeight());
                imageUrlList.add(string);
                imageMapIdList.add(planList.get(i).getMapId());
            }
            slideShowView.bindData(jumpUrlList, imageUrlList, imageMapIdList, new OnWzAdvViewPagerListener() {
                @Override
                public void onItemClick(int position, String imgUrl, String url, String resourEntryName) {
                    WzAdvApplication.getInstance().getmRouter().startWebview(url, "", false);
                }

                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    loadImage(context, path, imageView);
                }

                @Override
                public void onItemSelected(int mapId, int position, int total) {
                    reportAdvDisplay(reportList, mapId, position, total);
                }
            });
        } else {
            slideShowView.bindData(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Integer>(), null);
        }
        return slideShowView;
    }

    /**
     * 广告位展示统计上报
     *
     * @param mapId    (可选）广告素材id
     * @param position 位置 0，1，2，。。
     * @param total    轮播图总数（多连图）
     */
    private void reportAdvDisplay(List<Integer> reportList, int mapId, int position, int total) {
        if (reportList != null && reportList.contains(position)) {
            return;
        }
        reportList.add(position);
        String key = "" + mapId + position + total;
        if (reportMap != null && reportMap.size() > 0 && reportMap.containsKey(key)) {
            Long aLong = reportMap.get(key);
            if (System.currentTimeMillis() - aLong < 5 * 1000) {
                return;
            }
        }
        reportMap.put(key, System.currentTimeMillis());
        WzAdvServApi.getInstance().reportAdvDisplay(mapId, position, total, new IResCallback() {
            @Override
            public void onResult(String result) {

            }
        });
    }

    private void loadImage(Context context, Object path, ImageView imageView) {
        CornerTransform transformation = new CornerTransform(context, WzAdvTool.getInstance().dip2px(context, 5));
        transformation.setExceptCorner(false, false, false, false);
        Glide.with(context)
                .asBitmap()
                .load(path)
                .apply(RequestOptions.bitmapTransform(transformation).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }
}
