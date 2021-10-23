package com.service.microjc.Activity.Ykt;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.service.microjc.CustomUtils;
import com.service.microjc.InterFace.YktApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.YktUserInfo;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.service.microjc.stType.YktUserLoginInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YktLoginActivity extends AppCompatActivity {
    private String userNameValue;
    private String passwordValue;
    public YktUserLoginInfo loginInfo = new YktUserLoginInfo();
    public YktUserInfo info = new YktUserInfo();
    public Gson gson = new Gson();
    private EditText password;//密码输入框
    private EditText username;//账户输入框
    private CheckBox rememberKeyCheckBox;//记住密码勾选框
    private CheckBox autoLoginCheckBox;//自动登录选框
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ykt_login);

        //打开Preferences，名称为userInfo，如果存在则打开它，否则创建新的Preferences
        //Context.MODE_PRIVATE：指定该SharedPreferences数据只能被本应用程序读、写
        //Context.MODE_WORLD_READABLE：指定该SharedPreferences数据能被其他应用程序读，但不能写
        //Context.MODE_WORLD_WRITEABLE：指定该SharedPreferences数据能被其他应用程序读写。

        //对组件进行实例化
        //登录button
        rememberKeyCheckBox = findViewById(R.id.ykt_remember_key);
        rememberKeyCheckBox.setChecked(true);//设置默认 记住密码 初始化为true
        autoLoginCheckBox = findViewById(R.id.ykt_automatic_login);
        //对文本框实例化
        username = findViewById(R.id.YktUsername);
        password = findViewById(R.id.YktPassword);

        //初始化
        DialogX.init(this);
        DialogX.globalStyle = new IOSStyle();//设置为IOS主题

        Clear();//状态栏
        SetEditTextStatus();//输入状态监听
        setUI();//第二次进入登录界面，UI显示
        ClickLoginButton();//点击登录事件

    }

    /**
     * 状态栏管理
     * */
    public void Clear(){
        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        //隐藏状态栏和底部导航栏
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    /**
     * 进入登录界面，判断是否要勾选 记住密码，自动登录，以及填充数据
     * */
    private void setUI(){
        sp = getSharedPreferences("YktUserLoginInfo", Context.MODE_PRIVATE);
        //判断记住密码多选框的状态
        if (sp.getBoolean("rem_isCheck", false)) {
            //设置默认是记录密码状态
            rememberKeyCheckBox.setChecked(true);
            username.setText(sp.getString("USERNAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            Log.d("自动恢复保存的账号密码", "自动恢复保存的账号密码");

            //判断自动登陆多选框状态
            if (sp.getBoolean("auto_isCheck", false)) {

                //设置默认是自动登录状态
                autoLoginCheckBox.setChecked(true);
                Log.e("test", "boolean>>>>>>>>>>>>>>>>>: "+sp.getBoolean("auto_isCheck", false));

            }
        }
    }

    /**
     * 设置账户和密码输入框，交互体验
     * */
    public void SetEditTextStatus(){
        //获取textview焦点状态，当获取到输入焦点时隐藏 hint 文本内容
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

        //根据password textview的输入状态，调整是否显示 有颜色的登录button>>>    具体是继承TextWatcher类，重写 onTextChanged方法
        Button loginColor = findViewById(R.id.yktlogin_color);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userNameValue = username.getText().toString();
                passwordValue = password.getText().toString();
                if (TextUtils.isEmpty(passwordValue) || TextUtils.isEmpty(userNameValue)){
                    loginColor.setVisibility(View.INVISIBLE);
                }else {
                    loginColor.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userNameValue = username.getText().toString();
                passwordValue = password.getText().toString();
                if (TextUtils.isEmpty(passwordValue) || TextUtils.isEmpty(userNameValue)){
                    loginColor.setVisibility(View.INVISIBLE);
                }else {
                    loginColor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 点击登录事件
     * */
    private void ClickLoginButton(){
        userNameValue = username.getText().toString();
        passwordValue = password.getText().toString();

        //点击登录按钮  >>>   跳转事件
        Button loginButton = findViewById(R.id.yktlogin_color);
        loginButton.setOnClickListener(v -> {
            WaitDialog.show("正在登录");

            Log.d("选中保存密码", "当前状态：" + rememberKeyCheckBox.isChecked());

            //发起网络访问
            //实例化一个请求对象 api
            YktApi api = NetworkFactory.YktApi();
            Call<ResponseBody> Y = api.getYktUserInfo(userNameValue, passwordValue);
            Y.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    String content = null;
                    try {
                        content = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("test>>>>>>>>>>>>>>>>>", "onResponse: "+content );

                    loginInfo = gson.fromJson(content,YktUserLoginInfo.class);
                    String status = loginInfo.getLoginStatus();
                    if (status.equals("密码错误") || status.equals("账户错误")){
                        switch (status) {
                            case "账户错误":
                                CustomUtils.runDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog.show("账号错误", WaitDialog.TYPE.ERROR);
                                    }
                                }, 150);
                                break;
                            case "密码错误":
                                CustomUtils.runDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog.show("密码错误", WaitDialog.TYPE.ERROR);
                                    }
                                }, 150);
                                break;
                        }

                    }else if (status.equals("登录正常")){
                        info = gson.fromJson(content,YktUserInfo.class);
                        login();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TipDialog.show("网络错误", WaitDialog.TYPE.ERROR);
                        }
                    }, 500);


                }
            });

        });
    }

    /**
     * 登陆跳转
     * */
    public void login(){
        sp = getSharedPreferences("YktUserLoginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //判断是否要记住密码，并把勾选状态保存到sp当中
        if (rememberKeyCheckBox.isChecked()) {
            //记住用户名、密码、
            editor.putString("USERNAME", userNameValue);
            editor.putString("PASSWORD", passwordValue);
            editor.putBoolean("rem_isCheck", rememberKeyCheckBox.isChecked());
            editor.putBoolean("auto_isCheck", autoLoginCheckBox.isChecked());
            editor.apply();

            Log.d("选中保存密码", "账号：" + userNameValue +
                    "\n" + "密码：" + passwordValue +
                    "\n" + "是否记住密码：" + rememberKeyCheckBox.isChecked() +
                    "\n" + "是否自动登陆：" + autoLoginCheckBox.isChecked());
        }

            //利用intent意图跳转到第二个页面
            Intent intent = new Intent();
            intent.setClass(YktLoginActivity.this, YktUserInfoActivity.class);
            //利用intent传参
            intent.putExtra("username", userNameValue);
            intent.putExtra("password", passwordValue);
            intent.putExtra("from","yktLogin");
            intent.putExtra("userinfo", info);
            startActivity(intent);
            finish();
    }


}
