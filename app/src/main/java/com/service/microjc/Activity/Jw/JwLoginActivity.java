package com.service.microjc.Activity.Jw;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.service.microjc.Activity.App.Utils.SaveUserLoginInfo;
import com.service.microjc.Activity.App.Utils.security.AesRsa;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.InterFace.JwApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.AppUserInfo;
import com.service.microjc.stType.JwUserInfo;
import com.service.microjc.stType.LoginInfo;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.service.microjc.stType.SecurityContent;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JwLoginActivity extends AppCompatActivity {
    private EditText password;//密码输入框
    private EditText username;//账户输入框

    private CheckBox remember_key;//记住密码勾选框
    private SharedPreferences sp;

    private String userNameValue;
    private String passwordValue;

    public LoginInfo info = new LoginInfo();
    public JwUserInfo jwUserInfo = new JwUserInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jw);

        sp = getSharedPreferences("JwUserLoginInfo", Context.MODE_PRIVATE);
        //对but进行实例化
        remember_key = findViewById(R.id.jw_remember_key);
        //对文本框实例化
        username = findViewById(R.id.JwUsername);
        password = findViewById(R.id.JwPassword);
        remember_key.setChecked(true);//设置记住密码初始化为true

        //初始化
        DialogX.init(this);
        Clear();
        SetEditTextStatus();
        loginFirst();
        loginButtonListener();

    }
    /**
     *第一步获取登录信息
     */
    public void loginFirst() {

                JwApi api = NetworkFactory.jwApi();
                Call<LoginInfo> Y = api.getLoginInfo();
                Y.enqueue(new Callback<LoginInfo>() {
                    @Override
                    public void onResponse(@NotNull Call<LoginInfo> call, @NotNull Response<LoginInfo> response) {
                        info = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象

                        assert info != null;
                        Log.d(">>>>>>>", "当session值为》》》》》》》》：" + info.getSessionId());

                        //getCheckCode();//调用显示验证码方法，防止空指针

                    }

                    @Override
                    public void onFailure(@NotNull Call<LoginInfo> call, @NotNull Throwable t) {

                    }
                });

    }
    /**
     * 获取验证码
     * */
//    public void getCheckCode(){
//        ImageView checkCode_img = findViewById(R.id.checkcode);
//        GlideUrl glideUrl = new GlideUrl("http://1.14.68.248:8090/microjc/getcheckcode.jpg", new LazyHeaders.Builder()
//                .addHeader("Session", info.getSessionId())
//                .build());
//        Glide.with(JwLoginActivity.this)
//                .load(glideUrl)
//                .skipMemoryCache(true) // 不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
//                .into(checkCode_img);
//
//        checkCode_img.setOnClickListener(v -> {
//            GlideUrl glideUrl1 = new GlideUrl("http://1.14.68.248:8090/microjc/getcheckcode.jpg", new LazyHeaders.Builder()
//                    .addHeader("Session", info.getSessionId())
//                    .build());
//            Glide.with(JwLoginActivity.this)
//                    .load(glideUrl1)
//                    .skipMemoryCache(true) // 不使用内存缓存
//                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
//                    .into(checkCode_img);
//        });
//
//
//    }

    /**
     * 状态栏管理
     * */
    public void Clear(){
        //隐藏状态栏和底部导航栏
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    /**
     * 设置账户和密码输入框，交互体验
     * */
    public void SetEditTextStatus(){
        //当textview获取焦点时，隐藏hint当内容，具体是重写OnFocusChange方法
        //username输入框

        //根据password textview的输入状态，调整是否显示 有颜色的登录button>>>    具体是继承TextWatcher类，重写 onTextChanged方法
        password = findViewById(R.id.JwPassword);
        Button loginColor = findViewById(R.id.JwLogin_color);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = password.getText().toString();
                if (TextUtils.isEmpty(pass)){
                    loginColor.setVisibility(View.INVISIBLE);
                }else {
                    loginColor.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //获取textview焦点状态，当获取到输入焦点时隐藏 hint 文本内容
        username = findViewById(R.id.JwUsername);
        username.setOnFocusChangeListener((v, hasFocus) -> {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus){
                hint = textView.getHint().toString();
                textView.setTag(hint);//在获取到焦点时先把原始hint文本保存到Tag，再将文本设置为空 ""  以Tag的形式捆绑在View（即获得焦点的EditText）上
                textView.setHint("");
            }else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }

        });
        //password输入框
        password = findViewById(R.id.JwPassword);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus){
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            }else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }

        });
    }

    /**
     * 登陆跳转,按钮监听
     * */
    public void loginButtonListener(){

        Button JwLogin = findViewById(R.id.JwLogin_color);
        JwLogin.setOnClickListener(v -> {
            WaitDialog.show("正在登录");
            //获取到输入账号、密码等
            userNameValue = username.getText().toString();
            passwordValue = password.getText().toString();

            Login();
        });


    }

    /**
     * 登录的网络请求
     * */
    public void Login(){

        //登录网络请求
        info.setUsername(userNameValue);
        info.setPassword(passwordValue);

        JwApi api = NetworkFactory.jwApi();
        Call<JwUserInfo> Y = api.getLoginStatus(info);
        Y.enqueue(new Callback<JwUserInfo>() {

            @Override
            public void onResponse(@NotNull Call<JwUserInfo> call, @NotNull Response<JwUserInfo> response) {

                jwUserInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象
                assert jwUserInfo != null;
                info.setStudentName(jwUserInfo.getStudentName());//将服务器返回的学生名字，存入实体类

                Log.e(">>>>>>>", "判断jwUserInfo值是否为空》》》》》》》》：" + jwUserInfo.getLoginStatus());

                //getCheckCode();//调用显示验证码方法，防止空指针

                //判断登录状态
                switch (jwUserInfo.getLoginStatus()) {
                    case "账户错误":
                        CustomUtils.runDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TipDialog.show("账号错误", WaitDialog.TYPE.ERROR);
                            }
                        }, 150);

                        break;
                    case "密码错误还有4次尝试机会":
                    case "密码错误还有3次尝试机会":
                    case "密码错误还有2次尝试机会":
                    case "密码错误还有1次尝试机会":
                        CustomUtils.runDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TipDialog.show(jwUserInfo.getLoginStatus(), WaitDialog.TYPE.ERROR,2000);
                            }
                        }, 150);
                        break;
                    case "密码输入错误已达5次":
                        TipDialog.show("密码输入错误已达5次请明天再试", WaitDialog.TYPE.ERROR);
                        CustomUtils.runDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TipDialog.show("密码输入错误已达5次请明天再试", WaitDialog.TYPE.ERROR,2000);
                            }
                        }, 150);
                        break;
//                    case "验证码错误":
//                        CustomUtils.runDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                TipDialog.show("验证码错误", WaitDialog.TYPE.ERROR);
//                            }
//                        }, 150);
//                        break;
                    case "验证码错误,正在重试":
                        //如果自动识别验证码错误，则重试
                        //Login();
                        TipDialog.show("自动识别验证码错误", WaitDialog.TYPE.ERROR,2000);
                        finish();
                        break;
                    case "登录正常":
                        WaitDialog.dismiss();
                        to_JwUserInfoActivity();
                        //判断是否需要保存密码到本地
                        if (remember_key.isChecked()) {
                            sp = getSharedPreferences("JwUserLoginInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();//实例化一个sp对象
                            //如果记住密码选中则，将用户名、密码存入SP中
                            editor.putString("USERNAME", userNameValue);
                            editor.putString("PASSWORD", passwordValue);
                            editor.putBoolean("rem_isCheck", remember_key.isChecked());
                            editor.apply();

                            Log.d("选中保存密码", "账号：" + userNameValue +
                                    "\n" + "密码：" + passwordValue +
                                    "\n" + "是否记住密码：" + remember_key.isChecked());
                        }
                        //判断是否备份到云端
                        try {
                            SaveUserInfo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }

            @Override
            public void onFailure(@NotNull Call<JwUserInfo> call, @NotNull Throwable t) {

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
     * 登录跳转
     * */
    public void to_JwUserInfoActivity(){

        //利用intent意图跳转到第二个页面
        Intent intent = new Intent();
        intent.setClass(JwLoginActivity.this, JwUserInfoActivity.class);
        //利用intent传参
        intent.putExtra("from","jwLogin");
        intent.putExtra("loginInfo",info);
        startActivity(intent);
        finish();

    }

    /**
     * 判断是否要将用户信息上传到云端备份
     * */
    private void SaveUserInfo() throws Exception {
        Log.e("TAG", "SaveUserInfo: 进入加密方法" );


        sp = getSharedPreferences("SwitchButton", Context.MODE_PRIVATE);//设置页面创建
        if (remember_key.isChecked() & sp.getBoolean("SaveUserInfo",false)) {
            Log.e("TAG", "SaveUserInfo: 进入加密方法1111111" );

            //将要上传的信息加密
            Gson gson = new Gson();
            AppUserInfo appUserInfo = new AppUserInfo();

            sp = getSharedPreferences("UserLoginInfo", Context.MODE_PRIVATE);
            appUserInfo.setOpenID(sp.getString("openID", ""));
            appUserInfo.setTsgPass("");
            appUserInfo.setYktPass("");
            appUserInfo.setStudentID(Integer.parseInt(userNameValue));
            appUserInfo.setJwwPass(passwordValue);
            SecurityContent serContent = AesRsa.clientToServer(gson.toJson(appUserInfo));


            SaveUserLoginInfo.setUserInfo(serContent);
        }

    }

}