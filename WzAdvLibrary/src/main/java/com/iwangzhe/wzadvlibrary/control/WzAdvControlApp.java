package com.iwangzhe.wzadvlibrary.control;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iwangzhe.wzadvlibrary.WzAdvApplication;
import com.iwangzhe.wzadvlibrary.model.AdvertplanList;
import com.iwangzhe.wzadvlibrary.model.JAdvInfo;
import com.iwangzhe.wzadvlibrary.serv.ILoadAdvListener;
import com.iwangzhe.wzadvlibrary.serv.IResParseCallback;
import com.iwangzhe.wzadvlibrary.serv.OnWzAdvStartPagerListener;
import com.iwangzhe.wzadvlibrary.serv.OnWzAdvViewPagerListener;
import com.iwangzhe.wzadvlibrary.serv.WzAdvServApi;
import com.iwangzhe.wzadvlibrary.tool.CornerTransform;
import com.iwangzhe.wzadvlibrary.tool.WzAdvTool;
import com.iwangzhe.wzadvlibrary.view.AdvView;
import com.iwangzhe.wzadvlibrary.view.WzAdvStartPager;
import com.iwangzhe.wzadvlibrary.view.WzAdvViewPager;
import com.iwangzhe.wzcorelibrary.IRouter;
import com.iwangzhe.wzcorelibrary.WzNetCallback;
import com.iwangzhe.wzcorelibrary.base.CommonRes;
import com.iwangzhe.wzcorelibrary.base.app.ControlApp;

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
public class WzAdvControlApp extends ControlApp {
    private static WzAdvControlApp mWzAdvControlApp = null;
    private WzAdvApplication mMain;

    private WzAdvControlApp(WzAdvApplication main) {
        super(main);
        mMain = main;
        reportMap = new HashMap<>();
    }

    public static WzAdvControlApp getInstance(WzAdvApplication main) {
        synchronized (WzAdvControlApp.class) {
            if (mWzAdvControlApp == null) {
                mWzAdvControlApp = new WzAdvControlApp(main);
            }
        }
        return mWzAdvControlApp;
    }

    private IRouter mRouter;
    private Map<String, Long> reportMap;

    public void init(IRouter router) {
        this.mRouter = router;
    }

    /**
     * 初始化广告信息
     *
     * @param pageKey
     * @param posKey
     */
    public void createAdv(final Activity activity, final String pageKey, final String posKey, final AdvView view, final Map<Object, Object> option) {
        boolean isJustUseCache = false;
        if (option != null && option.containsKey("isJustUseCache")) {
            isJustUseCache = (boolean) option.get("isJustUseCache");
        }
        //1、查看缓存中是否有，有则显示
        displayAdvView(activity, pageKey, posKey, view, option);
        if (isJustUseCache) {
            loadAdv(pageKey, posKey, option, null);
        } else {
            //2、网络请求，拿到数据刷新页面
            loadAdv(pageKey, posKey, option, new ILoadAdvListener() {
                @Override
                public void onFinish() {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            displayAdvView(activity, pageKey, posKey, view, option);
                        }
                    });
                }
            });
        }
    }

    /**
     * 网络请求
     *
     * @param pageKey
     * @param posKey
     * @param option
     * @param listener
     */
    public void loadAdv(final String pageKey, final String posKey, final Map<Object, Object> option, final ILoadAdvListener listener) {
        mMain.mServApi.getAdverts(JAdvInfo.class, pageKey, posKey, new IResParseCallback<JAdvInfo>() {
            @Override
            public void onFinish(CommonRes<JAdvInfo> res) {
                Map<String, JAdvInfo> advInfoMap = mMain.mModelApi.getAdvInfoMap();
                JAdvInfo resObj = new JAdvInfo();
                if (res.isOk()) {
                    resObj = res.getResObj();
                    advInfoMap.put(pageKey + posKey, resObj);

                } else {
                    advInfoMap.put(pageKey + posKey, new JAdvInfo());
                }
                if (option != null && option.size() > 0 && option.containsKey("isCacheToDb")) {
                    boolean isCacheToDb = (boolean) option.get("isCacheToDb");
                    if (isCacheToDb) {
                        mMain.mServApi.setAdvInfoToDb(pageKey + posKey, resObj);
                    }
                }
                mMain.mModelApi.setAdvInfoMap(advInfoMap);
                if (listener != null) {
                    listener.onFinish();
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
    public boolean isAdvExist(String pageKey, String posKey) {
        CommonRes<JAdvInfo> jAdvInfoFromDb = mMain.mServApi.getJAdvInfoFromDb(pageKey + posKey);
        if (jAdvInfoFromDb.isOk()) {
            JAdvInfo resObj = jAdvInfoFromDb.getResObj();
            if (resObj != null && resObj.getPlanList().size() > 0) {
                Map<String, JAdvInfo> advInfoMap = mMain.mModelApi.getAdvInfoMap();
                advInfoMap.put(pageKey + posKey, resObj);
                mMain.mModelApi.setAdvInfoMap(advInfoMap);
                return true;
            }
        }
        Map<String, JAdvInfo> advInfoMap = mMain.mModelApi.getAdvInfoMap();
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

    private void displayAdvView(Activity activity, String pageKey, String posKey, AdvView view, Map<Object, Object> option) {
        if (view == null) {
            return;
        }
        if (isAdvExist(pageKey, posKey)) {
            AdvView adView = getView(pageKey, posKey, activity, option);
            if (adView == null) {
                view.setVisibility(View.GONE);
                return;
            }
            view.setVisibility(View.VISIBLE);
            view.removeAllViews();
            view.addView(adView);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private AdvView getView(String pageKey, String posKey, Activity activity, Map<Object, Object> option) {
        boolean foundAdv = isAdvExist(pageKey, posKey);
        if (!foundAdv) {
            return null;
        }
        JAdvInfo jAdvInfo = mMain.mModelApi.getAdvInfoMap().get(pageKey + posKey);
        int advType = jAdvInfo.getPositionInfo().getAdvType();
        if (advType == 4) {
            return getWzAdvViewPager(activity, jAdvInfo, option);
        } else if (advType == 2) {//开屏广告
            return getWzStartPagerView(activity, jAdvInfo, option);
        }
        return null;
    }

    private AdvView getWzStartPagerView(final Activity activity, JAdvInfo jAdvInfo, Map<Object, Object> option) {
        WzAdvStartPager startPager = new WzAdvStartPager(activity);
        ArrayList<AdvertplanList> planList = jAdvInfo.getPlanList();
        if (planList.size() > 0) {
            JSONObject jsonObject = WzAdvTool.getInstance().getJSONObject(planList.get(0).getPics());
            String jumpUrl = planList.get(0).getJumpUrl();
            final String title = planList.get(0).getTitle();
            int mapId = planList.get(0).getMapId();
            String imageUrl = jsonObject.getString("img_1242x2208");
            if (WzAdvTool.getInstance().isNormalWindow(activity)) {
                String url = jsonObject.getString("img_1080x1600");
                if (!TextUtils.isEmpty(url)) {
                    imageUrl = url;
                }
            }
            startPager.setData(jumpUrl, imageUrl, title, mapId, new OnWzAdvStartPagerListener() {
                @Override
                public void onCountDownViewClick() {
                    jumpToMain();
                    activity.finish();
                }

                @Override
                public void onCountDownViewFinish() {
                    jumpToMain();
                    activity.finish();
                }

                @Override
                public void onItemClick(String url, String titile) {
                    startWebview(url, true, title);
                    activity.finish();
                }

                @Override
                public void onItemShow(int mapId) {
                    reportAdvDisplay(new ArrayList<Integer>(), mapId, 0, 1);
                }
            });
        }
        return startPager;
    }

    private void startWebview(String url, boolean isEndToHome, String title) {
        mRouter.startWebview(url, title, isEndToHome, false);
    }

    private void jumpToMain() {
        mRouter.jumpToMain(null, "", false);
    }

    @NonNull
    private WzAdvViewPager getWzAdvViewPager(Activity activity, JAdvInfo jAdvInfo, Map<Object, Object> option) {
        WzAdvViewPager slideShowView = new WzAdvViewPager(activity);
        final List<Integer> reportList = new ArrayList<>();
        ArrayList<AdvertplanList> planList = jAdvInfo.getPlanList();
        int indicatorType = 0;
        int indicatorLocation = 0;
        int indicatorLaoutBottom = 0;
        boolean isShowTitle = false;
        boolean isAutoPlay = true;
        if (option.containsKey("indicatorType")) {
            indicatorType = (int) option.get("indicatorType");
        }
        if (option.containsKey("indicatorLocation")) {
            indicatorLocation = (int) option.get("indicatorLocation");
        }
        if (option.containsKey("indicatorLaoutBottom")) {
            indicatorLaoutBottom = (int) option.get("indicatorLaoutBottom");
        }
        if (option.containsKey("isShowTitle")) {
            isShowTitle = (boolean) option.get("isShowTitle");
        }
        if (option.containsKey("isAutoPlay")) {
            isAutoPlay = (boolean) option.get("isAutoPlay");
        }
        if (planList.size() > 0) {
            //添加图片到图片列表里
            List<String> imageUrlList = new ArrayList<>();
            List<Integer> imageMapIdList = new ArrayList<>();
            List<String> jumpUrlList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();
            for (int i = 0; i < planList.size(); i++) {
                jumpUrlList.add(planList.get(i).getJumpUrl());
                JSONObject jsonObject = WzAdvTool.getInstance().getJSONObject(planList.get(i).getPics());
                String string = jsonObject.getString("img_" + jAdvInfo.getPositionInfo().getWidth() + "x" + jAdvInfo.getPositionInfo().getHeight());
                imageUrlList.add(string);
                imageMapIdList.add(planList.get(i).getMapId());
                titleList.add(planList.get(i).getTitle());
            }
            slideShowView.bindData(jumpUrlList, imageUrlList, imageMapIdList, titleList, indicatorType, indicatorLocation
                    , indicatorLaoutBottom, isShowTitle, isAutoPlay, new OnWzAdvViewPagerListener() {
                        @Override
                        public void onItemClick(int position, String imgUrl, String url, String resourEntryName) {
                            startWebview(url, false, "");
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
            slideShowView.bindData(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<String>(),
                    indicatorType, indicatorLocation, indicatorLaoutBottom, isShowTitle, isAutoPlay, null);
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
        mMain.mServApi.reportAdvDisplay(mapId, position, total, new WzNetCallback() {
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
