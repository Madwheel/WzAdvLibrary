package com.iwangzhe.wzadvlibrary.model;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:53
 * desc   :
 */
public class SplashAdInfo {
    private String url;
    private String title;
    private String Img_1080_1920;
    private String Img_1080_1600;
    private String Img_1125_2436;
    private int mid;

    public SplashAdInfo() {
        url = "";
        title = "";
        Img_1080_1920 = "";
        Img_1080_1600 = "";
        Img_1125_2436 = "";
        mid = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_1125_2436() {
        return Img_1125_2436;
    }

    public void setImg_1125_2436(String img_1125_2436) {
        Img_1125_2436 = img_1125_2436;
    }

    public String getImg_1080_1920() {
        return Img_1080_1920;
    }

    public void setImg_1080_1920(String img_1080_1920) {
        Img_1080_1920 = img_1080_1920;
    }

    public String getImg_1080_1600() {
        return Img_1080_1600;
    }

    public void setImg_1080_1600(String img_1080_1600) {
        Img_1080_1600 = img_1080_1600;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
