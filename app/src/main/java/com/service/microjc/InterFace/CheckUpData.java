package com.service.microjc.InterFace;

import com.service.microjc.stType.AppInfo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CheckUpData {
    //定义请求接口部分URL地址以及请求方法
    @GET("/microjc/getversion")
    Call<AppInfo> getVersion();
}
