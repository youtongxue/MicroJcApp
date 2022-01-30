package com.service.microjc.InterFace;

import com.service.microjc.stType.SecurityContent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppApi {
    //上传用户信息
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/setappuserinfo")
    Call<String> setAppUserInfo(@Body SecurityContent securityContent);

    //获取用户信息
    @POST("/microjc/getappuserinfo")
    Call<SecurityContent> getAppUserInfo(@Query("openID") String openID);


}
