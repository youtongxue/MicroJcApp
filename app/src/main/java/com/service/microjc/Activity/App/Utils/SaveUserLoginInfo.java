package com.service.microjc.Activity.App.Utils;

import android.util.Log;
import com.service.microjc.InterFace.AppApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.stType.SecurityContent;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveUserLoginInfo {


    public static void setUserInfo(SecurityContent securityContent){

        Log.e("上传用户信息>>>>>>>>>>>>>>>>>", "被调用" );
        //发起网络访问
        //实例化一个请求对象 api
        AppApi api = NetworkFactory.getAppUserInfo();
        Call<String> Y = api.setAppUserInfo(securityContent);
        Y.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                String content = response.body();

                Log.e("上传用户信息>>>>>>>>>>>>>>>>>", "onResponse: "+content );

            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Log.e("上传用户信息>>>>>>>>>>>>>>>>>", "网络错误");
            }
        });

    }


}
