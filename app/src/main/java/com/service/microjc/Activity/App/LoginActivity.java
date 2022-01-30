package com.service.microjc.Activity.App;

import static com.tencent.connect.common.Constants.KEY_ENABLE_SHOW_DOWNLOAD_URL;
import static com.tencent.connect.common.Constants.KEY_QRCODE;
import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.service.microjc.Activity.App.Utils.Util;
import com.service.microjc.Activity.App.Utils.security.AesRsa;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.InterFace.AppApi;
import com.service.microjc.InterFace.LibraryApi;
import com.service.microjc.InterFace.YktApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.AppUserInfo;
import com.service.microjc.stType.LibraryUserInfo;
import com.service.microjc.stType.QQloginInfo;
import com.service.microjc.stType.SecurityContent;
import com.service.microjc.stType.TimeInfo;
import com.service.microjc.stType.UserQQInfo;
import com.service.microjc.stType.YktUserInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.open.log.SLog;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    public static String mAppid = "101976539";

    private static final String SHARE_PREF_NAME = "openSdk.pref";
    private static final String KEY_TARGET_QQ_UIN = "target.uin";
    private static final String KEY_TARGET_QQ_MINIAPP_ID = "target.miniappid";

    private static final String OPEN_CONNECT_DEMO_MINI_APP_ID = "101976539";
    private static final String OPEN_CONNECT_DEMO_MINI_APP_PATH = "pages/tabBar/index/index";

    private CardView mNewLoginButton;
    private TextView loginText;
    private ImageView mUserLogo;
    public static Tencent mTencent;
    private static Intent mPrizeIntent = null;
    private static boolean isLogin = false;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clear();

        Log.d(TAG, "-->onCreate");
        setContentView(R.layout.activity_login);

        mNewLoginButton = findViewById(R.id.appLoginButton);//登录button
        loginText = findViewById(R.id.loginText);
        mUserLogo = (ImageView) findViewById(R.id.userImage);
        sp = getSharedPreferences("UserLoginInfo", Context.MODE_PRIVATE);
        //判断当前状态登录状态
        Boolean loginState = sp.getBoolean("loginState",false);
        Log.e(TAG, "onCreate:>>>>>>>>>>>当前登录状态 "+loginState);
        setButton(loginState);

        //实列化，登录
        mTencent = Tencent.createInstance(mAppid, LoginActivity.this, "com.service.microjc.fileprovider");
        if (mTencent == null) {
            SLog.e(TAG, "Tencent instance create fail!");
            finish();
        }


        PermissionMgr.getInstance().requestPermissions(this);
        login();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //PermissionMgr.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "-->onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "-->onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "-->onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "-->onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "-->onDestroy");
        super.onDestroy();
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new DefaultUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread(){

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            String url = null;
                            if(json.has("figureurl")){
                                //获取qq号信息
                                Gson g = new Gson();
                                UserQQInfo userQQInfo = g.fromJson(response.toString(),UserQQInfo.class);
                                Bitmap bitmap = null;
                                try {
                                    url = json.getString("figureurl_qq_2");
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {
                                    SLog.e(TAG, "Util.getBitmap() jsonException : " + e.getMessage());
                                }
                                Message msg = new Message();
                                //msg.obj = bitmap;
                                msg.obj = userQQInfo;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            UserInfo info = new UserInfo(this, mTencent.getQQToken());
            info.getUserInfo(listener);

        } else {
            mUserLogo.setVisibility(android.view.View.GONE);
        }
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
//                JSONObject response = (JSONObject) msg.obj;
//                if (response.has("nickname")) {
//                    try {
//                        mUserInfo.setVisibility(android.view.View.VISIBLE);
//                        mUserInfo.setText(response.getString("nickname"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            } else if(msg.what == 1){
//                Bitmap bitmap = (Bitmap)msg.obj;
//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(android.view.View.VISIBLE);
                UserQQInfo  userQQInfo = (UserQQInfo) msg.obj;
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                intent.putExtra("from", 1);
                intent.putExtra("userImage", userQQInfo.getFigureurl_qq_2());
                intent.putExtra("nickName", userQQInfo.getNickname());
                intent.putExtra("userXb", userQQInfo.getGender());
                startActivity(intent);

                sp = getSharedPreferences("UserLoginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("UserImageUrl", userQQInfo.getFigureurl_qq_2());
                editor.putString("nickName", userQQInfo.getNickname());
                editor.putString("userXb", userQQInfo.getGender());
                editor.apply();
            }
        }

    };

    private void onClickLogin() {
        if (isLogin){
            mTencent.logout(LoginActivity.this);
            //退出清除所有sp中信息
            sp = getSharedPreferences("UserLoginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            //


            isLogin = false;
            setButton(false);
        }else {
            //登录
            if (!mTencent.isSessionValid()) {
                Tencent.setIsPermissionGranted(true);

                HashMap<String, Object> params = new HashMap<String, Object>();
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    params.put(KEY_RESTORE_LANDSCAPE, true);
                }



                params.put(KEY_SCOPE, "all");
                params.put(KEY_QRCODE, false);
                params.put(KEY_ENABLE_SHOW_DOWNLOAD_URL, true);
                mTencent.login(this, loginListener, params);
                Log.e(TAG, "onClickLogin: >>>>>>>>>>>>>>>>>>>2被调用" );

                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
            }
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            Log.e(TAG, "initOpenidAndToken: +++++++++++++"+openId );
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    private class BaseUiListener extends DefaultUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            Util.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
            //登录成功后，将用户的openID保存本地
            Gson gson = new Gson();
            QQloginInfo qQloginInfo = gson.fromJson(response.toString(),QQloginInfo.class);
            sp = getSharedPreferences("UserLoginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("openID", qQloginInfo.getOpenid());
            editor.apply();
            //查询用户云端数据
            getAppUserInfo(qQloginInfo.getOpenid());

            isLogin = true;
            setButton(true);

            sp = getSharedPreferences("UserLoginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sp.edit();
            editor1.putBoolean("loginState",true);
            editor1.apply();

            Log.e(TAG, "onComplete: <<>>>>>>>>>>>>>.11"+response.toString());

            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginActivity.this, "onCancel: ");
            Util.dismissDialog();

        }

    }
    //
    public void login(){
        mNewLoginButton.setOnClickListener(v -> {
            onClickLogin();
        });
    }

    //点击button退出，或者登录
    @SuppressLint("ResourceAsColor")
    public void setButton(Boolean loginState){
        if (loginState){
            loginText.setText("退出账号");
            isLogin = true;
        }else {
            loginText.setText("登录账号");
        }
    }

    //当判断到用户登录完成时，根据openID去数据库中查询用户信息
    public void getAppUserInfo(String openID){
        //发起网络访问
        //实例化一个请求对象 api
        AppApi api = NetworkFactory.getAppUserInfo();
        Call<SecurityContent> Y = api.getAppUserInfo(openID);
        Y.enqueue(new Callback<SecurityContent>() {
            @Override
            public void onResponse(@NotNull Call<SecurityContent> call, @NotNull Response<SecurityContent> response) {
                SecurityContent securityContent = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象
                assert securityContent != null;
                //解密
                try {
                    String content = AesRsa.serverToClient(securityContent);
                    Gson gson = new Gson();
                    AppUserInfo appUserInfo = gson.fromJson(content,AppUserInfo.class);
                    Log.e(TAG, "onResponse: "+appUserInfo.getOpenID());
                    Log.e(TAG, "onResponse: "+appUserInfo.getJwwPass());
                    Log.e(TAG, "onResponse: "+appUserInfo.getTsgPass());
                    Log.e(TAG, "onResponse: "+appUserInfo.getYktPass());
                    Log.e(TAG, "onResponse: "+appUserInfo.getStudentID());

                    //写入本地sp
                    if (!appUserInfo.getOpenID().isEmpty()){
                        sp = getSharedPreferences("AppUserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("openID", appUserInfo.getOpenID());
                        editor.putInt("studentID", appUserInfo.getStudentID());
                        editor.putString("jwwPass", appUserInfo.getJwwPass());
                        editor.putString("tsgPass", appUserInfo.getTsgPass());
                        editor.putString("yktPass", appUserInfo.getYktPass());
                        editor.apply();

                        //判断从获取的用户信息中，有那些信息并写入保存对应账户信息的sp中
                        //一卡通
                        if (!appUserInfo.getYktPass().isEmpty()){
                            //初始化首页fragment
                            getYktUserInfo(String.valueOf(appUserInfo.getStudentID()),appUserInfo.getYktPass());
                            //存入信息到sp
                            sp = getSharedPreferences("YktUserLoginInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor yktEditor = sp.edit();
                            yktEditor.putString("USERNAME", String.valueOf(appUserInfo.getStudentID()));
                            yktEditor.putString("PASSWORD", appUserInfo.getYktPass());
                            yktEditor.putBoolean("rem_isCheck", true);
                            yktEditor.apply();
                        }

                        //教务网
                        if (!appUserInfo.getJwwPass().isEmpty()){
                            sp = getSharedPreferences("JwUserLoginInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor jwwEditor = sp.edit();
                            jwwEditor.putString("USERNAME", String.valueOf(appUserInfo.getStudentID()));
                            jwwEditor.putString("PASSWORD",appUserInfo.getJwwPass());
                            jwwEditor.putBoolean("rem_isCheck", true);
                            jwwEditor.apply();

                        }

                        //图书馆
                        if (!appUserInfo.getTsgPass().isEmpty()){
                            //更新主页数据
                            getLibUserInfo(String.valueOf(appUserInfo.getStudentID()),appUserInfo.getTsgPass());
                            //将云端获取数据存入本地sp
                            sp = getSharedPreferences("LibraryUserLoginInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor tsgEditor = sp.edit();
                            tsgEditor.putString("USERNAME", String.valueOf(appUserInfo.getStudentID()));
                            tsgEditor.putString("PASSWORD", appUserInfo.getTsgPass());
                            tsgEditor.putBoolean("rem_isCheck", true);
                            tsgEditor.apply();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NotNull Call<SecurityContent> call, @NotNull Throwable t) {

                CustomUtils.runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TipDialog.show("网络错误", WaitDialog.TYPE.ERROR);
                    }
                }, 500);
            }

        });
    }

    /**
     * 登录成功后，需要更新，校园页面数据
     * */
    public void getYktUserInfo(String username, String password){
        Log.e(TAG, "getYktUserInfo: 登录后发起网络请求的参数"+username+password);
        //实例化一个请求对象 api
        YktApi api = NetworkFactory.YktApi();
        Call<ResponseBody> Y = api.getYktUserInfo(username,password);
        Y.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String content = null;
                try {

                    assert response.body() != null;
                    content = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                YktUserInfo yktUserInfo = gson.fromJson(content, YktUserInfo.class);//实例化一个userinfo对象，将网络请求响应body内容给对象

                Date date = new Date();
                TimeInfo timeInfo = CustomUtils.LongToString(date);
                String yktCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                sp = getSharedPreferences("YktUserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                //将查询到到的金额,查询时间 存入sp
                editor.putString("newMoney", yktUserInfo.getMoney());
                editor.putString("lastTime",yktCardTime);
                editor.apply();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                CustomUtils.runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TipDialog.show("网络出错", WaitDialog.TYPE.ERROR);
                    }
                }, 500);

            }
        });



    }

    /***
     * 登录后，图书馆网络请求
     */
    public void getLibUserInfo(String username, String password){
        LibraryApi api = NetworkFactory.LibraryApi();
        Call<LibraryUserInfo> Y = api.getLibraryUserInfo(username, password);
        Log.e(TAG, "getLibUserInfo:>>>>>>>>>>>>>>>>>> "+username+password);
        Y.enqueue(new Callback<LibraryUserInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<LibraryUserInfo> call, @NotNull Response<LibraryUserInfo> response) {
                LibraryUserInfo libraryUserInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象
                if (libraryUserInfo.getLoginStatus().equals("登录正常")) {

                    //将本次查询数据，存入sp中
                    Date date = new Date();
                    TimeInfo timeInfo = CustomUtils.LongToString(date);
                    String tsgCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                    sp = getSharedPreferences("LibUserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("newBooks", libraryUserInfo.getBorrowed()+"本");
                    editor.putString("lastTime",tsgCardTime);
                    editor.apply();
                }else if (libraryUserInfo.getLoginStatus().equals("学校服务器出错")){
                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TipDialog.show("学校服务器出错", WaitDialog.TYPE.ERROR);
                        }

                    }, 150);
                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }

                    }, 1000);

                }else {
                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TipDialog.show("服务器错误", WaitDialog.TYPE.ERROR);
                        }
                    }, 500);
                }

            }

            @Override
            public void onFailure(@NotNull Call<LibraryUserInfo> call, @NotNull Throwable t) {
                CustomUtils.runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TipDialog.show("网络错误", WaitDialog.TYPE.ERROR);
                    }
                }, 500);

            }
        });

    }

    /**
     * 状态栏管理
     * */
    private void clear(){
        ImmersionBar.with(LoginActivity.this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

    }


}