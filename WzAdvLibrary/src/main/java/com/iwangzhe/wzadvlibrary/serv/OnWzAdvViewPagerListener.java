package com.iwangzhe.wzadvlibrary.serv;

import android.content.Context;
import android.widget.ImageView;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1514:59
 * desc   :
 */
public interface OnWzAdvViewPagerListener {
    void onItemClick(int position, String imgUrl, String url, String resourEntryName);

    void displayImage(Context context, Object path, ImageView imageView);

    void onItemSelected(int mapId, int position, int total);
}
