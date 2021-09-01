package com.service.microjc.InterFace;

import com.service.microjc.stType.RecordsBean;
import com.service.microjc.stType.YktUserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YktApi {
    //定义请求接口部分URL地址以及请求方法
    @GET("/microjc/yktlogin")
    Call<YktUserInfo> getYktUserInfo(@Query("username") String username, @Query("password") String password);

    //获取消费记录
    @GET("/microjc/yktrecords")
    Call<RecordsBean> getLibraryUserInfo(@Query("username") String username, @Query("password") String password, @Query("start") String start, @Query("end") String end);

    //挂失
    @GET("/microjc/yktgs")
    Call<ResponseBody> getState(@Query("username") String username, @Query("password") String password, @Query("state") String state);
    //这里有个坑，因为设置了Gson接收json格式实体类，所以如果后台直接返回String类型，这里的Call<>泛型不能填String，要填ResponseBody再转String

    //设置消费限额
    @GET("/microjc/yktlimit")
    Call<ResponseBody> setLimitMoney(@Query("username") String username, @Query("password") String password, @Query("money") String money);
}
