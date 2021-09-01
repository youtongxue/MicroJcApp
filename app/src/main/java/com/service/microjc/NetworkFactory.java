package com.service.microjc;

import com.service.microjc.InterFace.CheckUpData;
import com.service.microjc.InterFace.JwApi;
import com.service.microjc.InterFace.LibraryApi;
import com.service.microjc.InterFace.Pure;
import com.service.microjc.InterFace.YktApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkFactory {
    private static LibraryApi libraryApi;
    private static YktApi yktApi;
    private static CheckUpData checkUpData;
    private static Pure pureInterFace;
    private static JwApi jwApi;
    private static final Retrofit retrofit;
    private static final Retrofit pure;


    //创建retrofit实例
    static {
        retrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl("http://1.14.68.248:8090/")//这里是服务器的IP地址，域名映射需要ICN备案，更没有SSL证书
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static {
        pure = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl("https://update.xfy9326.top/")//
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //图书馆
    public static LibraryApi LibraryApi(){
        if (null == libraryApi){
            libraryApi = retrofit.create(LibraryApi.class);
        }
        return libraryApi;
    }

    //一卡通
    public static YktApi YktApi(){
        if (null == yktApi){
            yktApi = retrofit.create(YktApi.class);
        }
        return yktApi;

    }

    //App检测更新
    public static CheckUpData checkUpData(){
        if (null == checkUpData){
            checkUpData = retrofit.create(CheckUpData.class);
        }
        return checkUpData;
    }

    //教务网登录
    public static JwApi jwApi(){
        if (null == jwApi){
            jwApi = retrofit.create(JwApi.class);
        }
        return jwApi;
    }

    //App检测更新
    public static Pure getPureInfo(){
        if (null == pureInterFace){
            pureInterFace = pure.create(Pure.class);
        }
        return pureInterFace;
    }

}
