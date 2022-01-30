package com.service.microjc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.service.microjc.Activity.Jw.JwLoginActivity;
import com.service.microjc.Activity.Jw.JwUserInfoActivity;
import com.service.microjc.Activity.Library.LibraryLoginActivity;
import com.service.microjc.Activity.Library.LibraryUserInfoActivity;
import com.service.microjc.Activity.Ykt.YktLoginActivity;
import com.service.microjc.Activity.Library.tsg_web_Activity;
import com.service.microjc.Activity.Ykt.YktUserInfoActivity;
import com.service.microjc.Activity.Ykt.pay_web_Activity;
import com.service.microjc.InterFace.YktApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.YktUserInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.style.IOSStyle;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class School_Fragment extends ImmersionFragment {
    private View view;

    private SharedPreferences sp;
    private Boolean savePass;

    private YktUserInfo info;//用于接收一卡通自动登录的信息
    private ImageView touxiang;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.school_fragment,null);

        touxiang  = view.findViewById(R.id.touxiang);

        //初始化
        DialogX.init(getContext());
        //设置为IOS主题
        DialogX.globalStyle = new IOSStyle();
        DialogX.onlyOnePopTip = true;

        setLinearHeight();
        scroll();
        SetMargin();
        to_LibraryLoginActivity();
        setUserImg();

        return view;
    }

    //重写 oNStart方法
    @Override
    public void onStart() {
        Log.e("test", "onStart");
        super.onStart();
        setMoney();
        setBooks();
        setUserImg();
    }

    /**
     * 对 Layout，设置监听跳转事件
     * */
    public void to_LibraryLoginActivity(){
        //图标 父LinearLayout
        LinearLayout jwwLayout = view.findViewById(R.id.jww_Layout);
        LinearLayout tsgLayout = view.findViewById(R.id.tsg_Layout);
        LinearLayout yktLayout = view.findViewById(R.id.ykt_Layout);
        LinearLayout sdfLayout = view.findViewById(R.id.sdf_Layout);
        LinearLayout wmLayout = view.findViewById(R.id.wm_Layout);
        LinearLayout swzlLayout = view.findViewById(R.id.swzl_Layout);

        //Card
        View yktCard = view.findViewById(R.id.school_ykt_card_info);//card信息
        View libCard = view.findViewById(R.id.school_lib_card_info);//card信息

        swzlLayout.setOnClickListener(v -> {
            //public static void gowxScan(Context context){
            Context context = getContext();
            try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                    context.startActivity(intent);

                } catch (Exception e) {
//                    Toast.makeText(context, "没有安装微信",);
                }
           // }


        });
        //测试
        sdfLayout.setOnClickListener(v ->{


        });

        tsgLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("LibraryUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), LibraryLoginActivity.class);
            }else {
                intent.setClass(getActivity(), LibraryUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        yktLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("YktUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), YktLoginActivity.class);
            }else {
                intent.setClass(getActivity(), YktUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        wmLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), tsg_web_Activity.class);
            startActivity(intent);
        });

        jwwLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("JwUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), JwLoginActivity.class);
            }else {
                intent.setClass(getActivity(), JwUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        yktCard.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("YktUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), YktLoginActivity.class);
            }else {
                intent.setClass(getActivity(), YktUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        libCard.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("LibraryUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), LibraryLoginActivity.class);
            }else {
                intent.setClass(getActivity(), LibraryUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

    }

    /**
     * 获取状态栏高度，设置layout的margin——top值
     *
     * */
    public void SetMargin(){
        //获取状态栏高度
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("TAG", "方法1状态栏高度:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + statusBarHeight1);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(0, statusBarHeight1, 0, 0);

        RelativeLayout titleLayout = view.findViewById(R.id.titleRelative_school);
        titleLayout.setLayoutParams(lp);

    }

    /***
     * 自动登录网络请求
     */
//    public void auto_login(String username, String password){
//        //发起网络访问
//        //实例化一个请求对象 api
//        YktApi api = NetworkFactory.YktApi();
//        Call<Object> Y = api.getYktUserInfo(username, password);
//        Y.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
//                info = (YktUserInfo) response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象
////                Log.d("自动登陆网络请求", "自动登陆结果》》》》》》》》》》》"+info.getUserName());
//                //跳转界面
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), YktUserInfoActivity.class);
//                //利用intent传参
//                //这里的  username和password只能从SharedPreferences去拿，全局变量定义的值会再程序结束后变为空值
//                intent.putExtra("username",sp.getString("USERNAME", ""));
//                intent.putExtra("password",sp.getString("PASSWORD", ""));
//                intent.putExtra("userinfo", info);
//
//                startActivity(intent);
//                //不能finish掉首页
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
//
//
//            }
//        });
//
//    }

    /**
     * 设置滚动布局
     * */
    private void scroll(){
        RefreshLayout refreshLayout = view.findViewById(R.id.school_refreshLayout);

        refreshLayout.setEnablePureScrollMode(true);
//        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
    }

    /**
     * 设置linearlayout的高度为屏幕的宽度
     * */
    private void setLinearHeight(){
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }
        int mRealSizeWidth;//手机屏幕真实宽度
        mRealSizeWidth = outPoint.x;

        Log.e("test", "setLinearWidth: "+mRealSizeWidth );
        /*
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */

        float scale = getContext().getResources().getDisplayMetrics().density;
        int width = (int) (mRealSizeWidth / scale + 0.5f);

        //设置linear高
        LinearLayout schoolCardLayout = view.findViewById(R.id.school_card_linear);
        ViewGroup.LayoutParams lp;
        lp = schoolCardLayout.getLayoutParams();
        lp.height = width+width/5;
        schoolCardLayout.setLayoutParams(lp);

    }

    /**
     * 设置信息card的值
     * */

    private void setMoney(){
        sp = getActivity().getSharedPreferences("YktUserInfo", Context.MODE_PRIVATE);
        String newMoney = sp.getString("newMoney","");
        String lastTime = sp.getString("lastTime","");//查询时间
        Log.e("test", "sp中金额为》》》》》》》》》》》"+newMoney );
        TextView moneyText = view.findViewById(R.id.moneyText);
        TextView yktCardTime = view.findViewById(R.id.yktCardTime);
        moneyText.setText(newMoney);
        yktCardTime.setText(lastTime);

        Log.e("test", "setMoney: >>>>>>>>>>被调用" );
    }

    private void setBooks(){
        sp = getActivity().getSharedPreferences("LibUserInfo",Context.MODE_PRIVATE);
        String newBooks = sp.getString("newBooks","");
        String lastTime = sp.getString("lastTime","");
        TextView booksText = view.findViewById(R.id.booksText);
        TextView tsgCardTime = view.findViewById(R.id.lib_card_time);
        booksText.setText(newBooks);
        tsgCardTime.setText(lastTime);

    }


    /**
     * 状态栏,继承自 extends ImmersionFragment，重写 initImmersionBar，即可不走fragment生命周期改变状态栏颜色
     * */
    @Override
    public void initImmersionBar() {
        //设置状态栏颜色
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    public void setUserImg(){
        sp = getActivity().getSharedPreferences("UserLoginInfo",Context.MODE_PRIVATE);
        String Url = sp.getString("UserImageUrl","null");
        if (Url.equals("null") & !sp.getBoolean("loginState",false)){
            Toast.makeText(getContext(),"获取用户相信失败",Toast.LENGTH_LONG);
            Drawable drawable = getActivity().getDrawable(R.drawable.touxiang);
            touxiang.setImageDrawable(drawable);
        }else {
            //设置头像
            RoundedCorners roundedCorners = new RoundedCorners(200);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(getContext()).load(Url).apply(options).into(touxiang);
        }
    }
}
