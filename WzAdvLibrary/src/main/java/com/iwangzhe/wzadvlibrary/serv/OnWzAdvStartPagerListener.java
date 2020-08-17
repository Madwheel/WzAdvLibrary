package com.iwangzhe.wzadvlibrary.serv;

import android.content.Context;
import android.widget.ImageView;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1714:15
 * desc   :
 */
public interface OnWzAdvStartPagerListener {
    void onCountDownViewClick();

    void onCountDownViewFinish();

    void onItemClick(String url, String titile);

    void onItemShow(int mapId);
}
