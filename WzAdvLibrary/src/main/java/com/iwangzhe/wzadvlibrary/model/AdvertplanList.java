package com.iwangzhe.wzadvlibrary.model;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:51
 * desc   :
 */
public class AdvertplanList {
    private int mapId;
    private String title;
    private String content;
    private String jumpUrl;
    private String pics;

    public AdvertplanList() {
        this.mapId = 0;
        this.title = "";
        this.content = "";
        this.jumpUrl = "";
        this.pics = "";
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }
}
