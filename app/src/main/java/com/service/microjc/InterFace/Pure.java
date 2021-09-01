package com.service.microjc.InterFace;

import com.service.microjc.stType.AppInfo;
import com.service.microjc.stType.PureInfo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Pure {
    //定义请求接口部分URL地址以及请求方法
    @GET("/Schedule/LatestDownload")
    Call<PureInfo> getPureInfo();
}
