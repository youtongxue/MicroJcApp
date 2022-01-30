package com.service.microjc.Activity.App;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.service.microjc.Activity.App.Utils.DownloadUtils;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.Fragment.My_Fragment;
import com.service.microjc.Fragment.RiCheng_Fragment;
import com.service.microjc.Fragment.School_Fragment;
import com.service.microjc.InterFace.CheckUpData;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.AppInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.style.IOSStyle;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//对于单击显示当前页面由MainActivity.java来实现
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test";
    /*声明组件变量*/
    private ImageView riCheng = null;
    private ImageView school = null;
    private ImageView wd = null;
    private TextView rc = null;
    private TextView xy = null;
    private TextView w = null;
    private SharedPreferences sp;

    FragmentManager fm;
    FragmentTransaction transaction;
    Fragment frag1;
    Fragment frag2;
    Fragment frag3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        //实例化按钮文本
        rc = findViewById(R.id.richeng_text);
        xy = findViewById(R.id.xiaoyuan_text);
        w = findViewById(R.id.my_text);
        //首先获取4张ImageView图片
        riCheng =findViewById(R.id.rc_img);
        school =findViewById(R.id.xy_img);
        wd=findViewById(R.id.jww_img);


        setStatus();
        SetDefault();
        //CheckUpData();
        checkPermission();
        fromLogin();
        initSwitchButton();//初始化设置页面按钮状态
        try {
            testRsa();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        Log.d("TAG>>>>>>>>", "-->onStart");
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

    //重写返回键回掉方法，让按下返回键时 让当前activity隐藏到后台，而不是 调用 finish(); ，第二次打开时就不会再次加载Splash界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            moveTaskToBack(true);
        }
        return true;
    }



    @SuppressLint("ResourceAsColor")
    public void onClick(View v) {
        clearSelection();//清除，图标选中状态颜色，字体颜色
        transaction = fm.beginTransaction();
        hideAllFragment();//先隐藏所有fragment
        switch (v.getId()) {
            case R.id.richeng_layout:
                riCheng.setImageResource(R.drawable.ic_tab_richeng_pressed);//修改布局中的图片
                rc.setTextColor(Color.parseColor("#FDC102"));//修改字体颜色
                if (frag1 == null) {
                    frag1 = new RiCheng_Fragment();
                    transaction.add(R.id.fragment, frag1, "Frag1");
                } else {
                    transaction.show(frag1);
                }

                break;
            case R.id.xiaoyuan_layout:
                school.setImageResource(R.drawable.ic_home_pressed);
                xy.setTextColor(Color.parseColor("#FDC102"));
                if (frag2 == null) {
                    frag2 = new School_Fragment();
                    transaction.add(R.id.fragment, frag2, "Frag2");
                }else {
                    transaction.show(frag2);
                }

                break;
            case R.id.my_layout:
                wd.setImageResource(R.drawable.ic_tab_my_pressed);
                w.setTextColor(Color.parseColor("#FDC102"));
                if (frag3 == null) {
                    frag3 = new My_Fragment();
                    transaction.add(R.id.fragment, frag3, "Frag3");
                }else {
                    transaction.show(frag3);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFragment() {
        if (frag1 != null) {
            transaction.hide(frag1);
        }
        if (frag2 != null) {
            transaction.hide(frag2);
        }
        if (frag3 != null) {
            transaction.hide(frag3);
        }
    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        riCheng.setImageResource(R.drawable.ic_tab_richeng_normal);
        rc.setTextColor(Color.parseColor("#82858b"));

        school.setImageResource(R.drawable.ic_home);
        xy.setTextColor(Color.parseColor("#82858b"));

        wd.setImageResource(R.drawable.ic_tab_my_normal);
        w.setTextColor(Color.parseColor("#82858b"));

    }

    /***
     * 默认选择日程界面
     */
    @SuppressLint("ResourceAsColor")
    public void SetDefault() {
        transaction = fm.beginTransaction();

//        riCheng.setImageResource(R.drawable.ic_tab_richeng_pressed);//修改布局中的图片
//        rc.setTextColor(Color.parseColor("#FDC102") );//修改字体颜色
//        frag1 = new RiCheng_Fragment();
//        transaction.add(R.id.fragment, frag1, "Frag1");
//        transaction.show(frag1);
//        transaction.commit();

        transaction = fm.beginTransaction();

        school.setImageResource(R.drawable.ic_home_pressed);//修改布局中的图片
        xy.setTextColor(Color.parseColor("#FDC102") );//修改字体颜色
        frag2 = new School_Fragment();
        transaction.add(R.id.fragment, frag2, "Frag1");
        transaction.show(frag2);
        transaction.commit();
    }

    /**
     * 每次打开就检查App是否需要更新
     * */
//    public  void  CheckUpData(){
//        //发起网络访问
//        //实例化一个请求对象 api
//        CheckUpData checkUpData = NetworkFactory.checkUpData();
//        Call<AppInfo> Y = checkUpData.getVersion();
//        Y.enqueue(new Callback<AppInfo>() {
//            @Override
//            public void onResponse(@NotNull Call<AppInfo> call, @NotNull Response<AppInfo> response) {
//                AppInfo appInfo = response.body();
//                assert appInfo != null;
//                float getCode = appInfo.getVersion();
//                float appCode = Float.parseFloat(CustomUtils.getVersion(MainActivity.this));
//
//                if (getCode > appCode) {
//
//                    MessageDialog.build()
//                            .setStyle(IOSStyle.style())
//                            .setTheme(DialogX.THEME.AUTO)
//                            .setTitle("更新提示")
//                            .setMessage(appInfo.getUpDataContent())
//                            .setOkButton("下载")
//                            .setCancelable(true)
//                            .setBackgroundColor(Color.parseColor("#FFFFFF"))
//                            .setOkButton((baseDialog, v) -> {
//
//                                DownloadUtils downloadUtils = new DownloadUtils(MainActivity.this);
//                                String url = "http://1.14.68.248/appupdata/app/We锦大"+getCode+".apk";
//                                String name = "We锦大"+getCode+".apk";
//                                downloadUtils.downloadAPK(url,name,name);
//
//                                return false;
//                            })
//                            .show();
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<AppInfo> call, @NotNull Throwable t) {
//                MessageDialog.build()
//                        .setStyle(IOSStyle.style())
//                        .setTheme(DialogX.THEME.AUTO)
//                        .setTitle("🙅‍♂️")
//                        .setMessage("网络错误")
//                        .setOkButton("确定")
//                        .setCancelable(true)
//                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
//                        .show();
//
//            }
//
//        });
//    }

    /**
     * 设置状态栏
     * */
    private void setStatus(){
        //设置状态栏
        ImmersionBar.with(MainActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    //检查读写权限
    private void checkPermission() {
        try {
            String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {

                int REQUEST_CODE = 0;
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_CODE);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 登录后，选中状态
     */
    public void fromLogin(){
        Intent intent = getIntent();
        int from = intent.getIntExtra("from",0);
        if (from == 1 ){
            clearSelection();//清除状态
            transaction = fm.beginTransaction();
            wd.setImageResource(R.drawable.ic_tab_my_pressed);//修改布局中的图片
            w.setTextColor(Color.parseColor("#FDC102") );//修改字体颜色
            frag3 = new My_Fragment();
            transaction.add(R.id.fragment, frag3, "Frag3");
            transaction.show(frag3);
            transaction.commit();

            //
        }
    }

    //test
    public void testRsa() throws Exception {


    }

    /**
     * 设置switchButton默认状态
     * */
    private void initSwitchButton(){
        sp = getSharedPreferences("SwitchButton", Context.MODE_PRIVATE);
        if (sp.getInt("FirstOpen",0) == 0){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("SaveUserInfo",true);
            editor.putInt("FirstOpen",1);
            editor.apply();
        }


    }

}

