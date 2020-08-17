package com.iwangzhe.wzadvlibrary.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1515:32
 * desc   :
 */
public class JBase {
    private int errorCode;

    public JBase(){
        errorCode = 0;
    }

    @JSONField(name="error_code")
    public int getErrorCode(){
        return errorCode;
    }

    @JSONField(name="error_code")
    public void setErrorCode(int value){
        errorCode = value;
    }
}
