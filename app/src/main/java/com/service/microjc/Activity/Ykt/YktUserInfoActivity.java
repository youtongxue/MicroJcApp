package com.service.microjc.Activity.Ykt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.service.microjc.CustomUtils;
import com.service.microjc.InterFace.YktApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.TimeInfo;
import com.service.microjc.stType.YktUserInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.util.InputInfo;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YktUserInfoActivity extends AppCompatActivity {
    @SuppressLint("MissingSuperCall")
    public static final String TAG = "MAIN";
    private YktUserInfo yktUserInfo = new YktUserInfo() ;
    public String username;//定义全局变量用户名和密码
    public String password;
    public String start;
    public String end;

    private Boolean YktUserInfoHttp;
    private RefreshLayout refreshLayout;

    //布局监听
    private View recordLay;//消费记录
    private View limitLay;//限额
    private View gsLay;//挂失
    private View bubLay;//补办

    //主页card时间信息
    String yktCardTime;

    //实例化UI层的textview
    //card信息
    private TextView UserNameTextView;
    private TextView YktStateTextView;
    private TextView YktMoneyTextView;
    private TextView YktLimitMoneyTextView;

    private CardView card;
    //功能服务
    private TextView gsText;

    //实例化sp
    private SharedPreferences sp;

    //在主线程创建handler用于接收，子线程message压栈的待处理消息队列
//    private Handler h = new Handler() {
//        //当收到消息时，应该执行的代码
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0){
////                Toast.makeText(YktUserInfoActivity.this,"访问出错",
////                        Toast.LENGTH_LONG).show();
//            }else if (msg.what == 1) {
//                YktUserInfo info = (YktUserInfo) msg.obj;
////                List<LibraryUserInfo> libraryUserInfo = (List<LibraryUserInfo>) msg.obj;
////                LibraryUserInfo Userinfo = new LibraryUserInfo();
////                Userinfo.setStudentName(info.getStudentName());
//                Log.d("TEST", info.toString());
//
//                //改变textview文本
//                UserNameTextView.setText(info.getUserName());
//                YktStateTextView.setText(info.getState());
//                YktMoneyTextView.setText(info.getMoney());
//                YktLimitMoneyTextView.setText(info.getLimitMoney());
//
//                WaitDialog.dismiss();
//
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ykt_userinfo);



        //初始化
        DialogX.init(this);
        //设置为IOS主题
        DialogX.globalStyle = new IOSStyle();

        //实例化card信息textview
        card = findViewById(R.id.ykt_card);

        UserNameTextView = findViewById(R.id.YktUserName);
        YktStateTextView = findViewById(R.id.ykt_status);
        YktMoneyTextView = findViewById(R.id.YktMoney);
        YktLimitMoneyTextView = findViewById(R.id.YktLimitMoney);

        //实例化 功能服务 布局组件
        recordLay = findViewById(R.id.record_Layout);
        limitLay = findViewById(R.id.limit_Layout);
        gsLay = findViewById(R.id.gs_Layout);
        bubLay = findViewById(R.id.bub_Layout);
        //textview
        gsText = findViewById(R.id.gs_text);

        clear();
        SetMargin();
        goBack();
        getTheIntent();
        cardFunction();
        refresh();//下拉刷新
    }

    /**
     * 接收intent，获取参数
     * */
    private void getTheIntent(){
        //接收LibraryLogin传过来的参数参数
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        //需要判断intent来自那个activity

        //loginActivity那么就已经发起过网络请求，并传了信息过来
        switch (intent.getStringExtra("from")) {
            case "yktLogin":
                Log.e(TAG, " 登录进入" );
                yktUserInfo = (YktUserInfo) getIntent().getSerializableExtra("userinfo");

                UserNameTextView.setText(yktUserInfo.getUserName());
                YktStateTextView.setText(yktUserInfo.getState());
                YktMoneyTextView.setText(yktUserInfo.getMoney());
                YktLimitMoneyTextView.setText(yktUserInfo.getLimitMoney());

                Date date = new Date();
                TimeInfo timeInfo = CustomUtils.LongToString(date);
                yktCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                sp = getSharedPreferences("YktUserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                //将查询到到的金额存入sp
                editor.putString("newMoney", yktUserInfo.getMoney());
                editor.putString("lastTime", yktCardTime);
                editor.apply();

                setCardColor();//判断 card状态 设置颜色


                break;
            case "school":
            case "schoolCard":
                Log.e(TAG, " 主页进入" );
                WaitDialog.show("正在查询");
                getYktUserInfo();//发起网络请求

                break;


        }
    }

    /**
     * 设置状态栏状态，颜色，底部虚拟导航栏颜色
     * */
    private void clear(){
        ImmersionBar.with(YktUserInfoActivity.this)
                .statusBarColor(R.color.navbar)
                .navigationBarColor(R.color.navbar)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    /**
     * 获取用户信息，子线程发起网络请求
     *
     * @return
     */
    private void getYktUserInfo(){

        //实例化一个请求对象 api
        YktApi api = NetworkFactory.YktApi();
        Call<YktUserInfo> Y = api.getYktUserInfo(username,password);
        Y.enqueue(new Callback<YktUserInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<YktUserInfo> call, @NotNull Response<YktUserInfo> response) {
                yktUserInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象

                YktUserInfoHttp = true;
                setCardColor();//拿到请求结果先设置card颜色

                UserNameTextView.setText(yktUserInfo.getUserName());
                YktStateTextView.setText(yktUserInfo.getState());
                YktMoneyTextView.setText(yktUserInfo.getMoney());
                YktLimitMoneyTextView.setText(yktUserInfo.getLimitMoney());

                WaitDialog.dismiss();//关闭等待动画
                //获取当前查询时间
//                Date date = new Date();
//                long nowLong = date.getTime();//转换成long型时间戳
//                TimeInfo timeInfo = CustomUtils.LongToString(nowLong);
//                yktCardM = timeInfo.getM();
//                yktCardD = timeInfo.getD();
//                yktCardTime = yktCardM+"月"+yktCardD+"日  "+timeInfo.getHmString();

                Date date = new Date();
                TimeInfo timeInfo = CustomUtils.LongToString(date);
                yktCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                sp = getSharedPreferences("YktUserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                //将查询到到的金额,查询时间 存入sp
                editor.putString("newMoney", yktUserInfo.getMoney());
                editor.putString("lastTime",yktCardTime);
                editor.apply();

//                Message msg = new Message();
//                msg.what = 1;
//                msg.obj = yktUserInfo;
//                h.sendMessage(msg);//用handler将json反序列化对象堆栈到MessageQueue

            }

            @Override
            public void onFailure(@NotNull Call<YktUserInfo> call, @NotNull Throwable t) {


                CustomUtils.runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TipDialog.show("网络出错", WaitDialog.TYPE.ERROR);
                    }
                }, 500);

            }
        });



        }

    /**
     *设置 功能服务 布局点击监听事件
     * */
    public void cardFunction(){
        //消费记录
        recordLay.setOnClickListener(v -> toRecordActivity());

        //消费限额
        limitLay.setOnClickListener(v -> setLimit());

        //挂失
        gsLay.setOnClickListener(v -> lostCard());

        //导航
        bubLay.setOnClickListener(v -> gotoBaiDuMap());
    }


    /***
     *查询消费记录跳转
     */
    public void toRecordActivity(){
                Intent intent = new Intent();
                intent.setClass(YktUserInfoActivity.this, RecordActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                startActivity(intent);
                Log.d(TAG, "意图是否执行>>>>>>>>：");
                Log.d(TAG, "查看数据>>>>>>>>："+username+password+start+end);
    }

    /**
     * 获取状态栏高度，设置layout的margin——top值
     *
     * */
    public void SetMargin(){
        //获取状态栏高度
        //屏幕高度
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


        RelativeLayout titleLayout1 = findViewById(R.id.titleRelative_yktuser);
        titleLayout1.setLayoutParams(lp);

//        setScroller();
    }

    /**
     * 图标返回
     * */
    public void goBack(){
        //图标
        ImageView backIcon = findViewById(R.id.fanhui_yktxq);

        backIcon.setOnClickListener(v -> finish());
    }

    /**
     * @Description 一卡通挂失
     * @Date 2021-8-9
     * */
    public void lostCard(){
                //是正常则挂失
                if (YktStateTextView.getText().equals("正常")){

                    InputInfo in = new InputInfo();
                    in.setInputType(0x00000081);

                    new InputDialog("挂失一卡通", "验证密码确认是您", "确定", "取消")
//                            .setInputText("test")
                            .setInputInfo(in)
                            .setCancelable(false)
                            .setOkButton((baseDialog, v, inputStr) -> {
                                //显示等待进度框
                                WaitDialog.show("处理中...");

                                baseDialog.getInputText();
                                //判断输入密码是否正确
                                if (inputStr.equals(password)){
                                    yktGs(inputStr,"0");
                                }else {
                                    CustomUtils.runDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            TipDialog.show("密码错误", WaitDialog.TYPE.ERROR);
                                        }
                                    }, 150);
                                }

                                return false;
                            })
                            .show();
                }
                //账户挂失则，解除挂失
                else{
                    InputInfo in = new InputInfo();
                    in.setInputType(0x00000081);
                    new InputDialog("解除挂失", "验证密码确认是您", "确定", "取消")
//                            .setInputInfo(TextInfo)
//                            .setInputText("test")
                            .setInputInfo(in)
                            .setCancelable(false)
                            .setOkButton((baseDialog, v, inputStr) -> {
                                WaitDialog.show("正在处理");
                                //判断输入密码是否正确
                                if (inputStr.equals(password)){
                                    yktGs(inputStr,"1");
                                }else {

                                    CustomUtils.runDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            TipDialog.show("密码错误", WaitDialog.TYPE.ERROR);
                                        }
                                    }, 150);
                                }

                                return false;
                            })
                            .show();
                }
    }

    /**
     * 挂失方法，网络请求
     * @Date 2021-8-9
     * @Author 游同学
     * */
    public void yktGs(String inputStr,String state){

        //如果输入密码和登录密码一致，则执行挂失
        if (inputStr.equals(password)){

            //实例化网络请求API
            YktApi yktApi = NetworkFactory.YktApi();
            Call<ResponseBody> result = yktApi.getState(username,password,state);
            result.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    Log.e(TAG, "re: >>>>>>>>>>>>>>>"+response.body());

                    String re = null;
                    try {
                        assert response.body() != null;
                        re = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    assert re != null;
                    if (re.equals("挂失成功")){
                        TipDialog.show("挂失成功！", WaitDialog.TYPE.SUCCESS);
                        YktStateTextView.setText("账户挂失");//更改UI层
                        gsText.setText("解除挂失");
                        //FEB900
                        card.setCardBackgroundColor(Color.parseColor("#FEB900"));


                    }else if (re.equals("解除挂失成功")){
                        TipDialog.show("解除挂失成功！", WaitDialog.TYPE.SUCCESS);
                        YktStateTextView.setText("正常");//更改UI层
                        gsText.setText("挂失账户");
                        card.setCardBackgroundColor(Color.parseColor("#2196F3"));

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

        }
    }

    /**
     * @Description 设置消费限额
     * @Date 2021-8-11
     * @Author 游同学
     * */
    private void setLimit(){
                new InputDialog("设置单日额度", "", "确定", "取消")
                        .setCancelable(false)
                        .setOkButton((baseDialog, v, inputStr) -> {
                            WaitDialog.show("正在处理");
                            setLimitHttp(inputStr);

                            return false;
                        })
                        .show();

    }

    /**
     * 设置消费限额，网络请求
     * */
    private void setLimitHttp(String money){

        //实例化网络请求API
        YktApi yktApi = NetworkFactory.YktApi();
        Call<ResponseBody> result = yktApi.setLimitMoney(username,password,money);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                String comp = null;
                try {
                    assert response.body() != null;
                    comp = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert comp != null;
                if (comp.equals("设置成功")){
                    YktLimitMoneyTextView.setText(money);

                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TipDialog.show("设置成功！", WaitDialog.TYPE.SUCCESS);
                        }
                    }, 150);


                }else {

                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TipDialog.show("服务器出错", WaitDialog.TYPE.WARNING);
                        }
                    }, 150);
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



    }

    /**
     * 地图导航
     * */
    private void gotoBaiDuMap() {
        // 驾车导航
        StringBuffer sb = new StringBuffer("baidumap://map/navi")
                .append("?coord_type=gcj02")
                .append("&query=").append("川大锦城二食堂")
                .append("&src=").append(this.getPackageName());
        Intent intent = new Intent();
        intent.setData(Uri.parse(sb.toString()));
        startActivity(intent);
    }

    /**
     * 判断 card 状态改变颜色,这个方法只是刚进入activity时判断调用
     * */
    @SuppressLint("ResourceAsColor")
    private void setCardColor(){


        if (yktUserInfo.getState().equals("账户挂失")){
            card.setCardBackgroundColor(Color.parseColor("#FEB900"));
            gsText.setText("解除挂失");
        }else if (yktUserInfo.getState().equals("正常")){
            gsText.setText("挂失账户");
            card.setCardBackgroundColor(Color.parseColor("#2196F3"));
        }
    }

    /**
     * 刷新，回弹效果
     * */
    private void refresh(){
        refreshLayout = findViewById(R.id.refreshLayout);

        //第一个refresh布局
        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);

        //第二个refresh布局
        RefreshLayout refreshLayoutTwo = findViewById(R.id.refreshLayout_two);
        refreshLayoutTwo.setEnablePureScrollMode(true);
        refreshLayoutTwo.setEnableRefresh(false);
        refreshLayoutTwo.setEnableLoadMore(true);

        //下拉刷新监听事件
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLay -> {
            //显示等待进度框
            RefreshYktUserInfo(refreshLay);

        });

    }

    /**
     * 下拉刷新单独网络请求方法
     * */
    private void RefreshYktUserInfo(RefreshLayout refreshLayout){
        //实例化一个请求对象 api
        YktApi api = NetworkFactory.YktApi();
        Call<YktUserInfo> Y = api.getYktUserInfo(username,password);
        Y.enqueue(new Callback<YktUserInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<YktUserInfo> call, @NotNull Response<YktUserInfo> response) {
                yktUserInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象

                setCardColor();//拿到请求结果先设置card颜色

                UserNameTextView.setText(yktUserInfo.getUserName());
                YktStateTextView.setText(yktUserInfo.getState());
                YktMoneyTextView.setText(yktUserInfo.getMoney());
                YktLimitMoneyTextView.setText(yktUserInfo.getLimitMoney());

                refreshLayout.finishRefresh();

                //获取当前查询时间
//                Date date = new Date();
//                long nowLong = date.getTime();//转换成long型时间戳
//                TimeInfo timeInfo = CustomUtils.LongToString(nowLong);
//                yktCardM = timeInfo.getM();
//                yktCardD = timeInfo.getD();
//                yktCardTime = yktCardM+"月"+yktCardD+"日  "+timeInfo.getHmString();

                Date date = new Date();
                TimeInfo timeInfo = CustomUtils.LongToString(date);
                yktCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                //实例化sp
                sp = getSharedPreferences("YktUserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                //将查询到到的金额存入sp
                editor.putString("lastTime",yktCardTime);
                editor.putString("newMoney", yktUserInfo.getMoney());
                editor.apply();




            }

            @Override
            public void onFailure(@NotNull Call<YktUserInfo> call, @NotNull Throwable t) {
                refreshLayout.finishRefresh(false);


            }
        });


    }




}
