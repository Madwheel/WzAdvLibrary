package com.iwangzhe.wzadvlibrary.tool;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:57
 * desc   :
 */
public class WzAdvTool {
    private static WzAdvTool mWzAdvTool = null;
    public static WzAdvTool getInstance() {
        synchronized (WzAdvTool.class) {
          if (mWzAdvTool == null) {
             mWzAdvTool = new WzAdvTool();
          }
        }
        return mWzAdvTool;
    }
    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return 转为像素后的值
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return 转为像素后的值
     */
    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 提供getMD5(String)方法
     *
     * @param val 需要进行MD5加密的字符串
     * @return 加密后返回的字符串
     */
    public String formatToMD5(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
        }
        md5.update(val.getBytes());
        byte[] bytes = md5.digest();//加密
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append(bytes[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * 比较两个list是否相同
     *
     * @param arraylist1
     * @param arraylist2
     * @return
     */
    public boolean compareList(List<String> arraylist1, List<String> arraylist2) {
        int size = arraylist1.size();
        if (size == arraylist2.size()) {
            for (int i = 0; i < size; i++) {
                if (arraylist1.get(i).equals(arraylist2.get(i))) {
                    if (i == size - 1) {
                        return true;
                    }
                    continue;
                }

            }
        }
        return false;
    }

    public JSONObject getJSONObject(String json) {
        return (JSONObject) JSONObject.parse(json);
    }

    public boolean isNormalWindow(Activity activity) {
        float size = ontailWindowSize(activity);
        if (Math.abs(size - 1.78) <= Math.abs(size - 2.17)) {
            return true;
        } else {
            return false;
        }
    }

    public int getImgFromStr(Context context, String imgUrl) {
        int img;
        img = context.getResources().getIdentifier(imgUrl, "drawable", context.getPackageName());
        return img;
    }

    public float ontailWindowSize(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format((float) heightPixel / widthPixel);
        Float aFloat = Float.valueOf(format);
        return aFloat;
    }

}
