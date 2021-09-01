package com.service.microjc.InterFace;

import com.service.microjc.stType.LibraryUserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LibraryApi {
    //定义请求接口部分URL地址以及请求方法
    @GET("/microjc/librarylogin")
    Call<LibraryUserInfo> getLibraryUserInfo(@Query("username") String username, @Query("password") String password);



//    test(@Body User u);//传json对象
}
