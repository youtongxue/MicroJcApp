package com.service.microjc.Activity.Library;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.service.microjc.Activity.App.MainActivity;
import com.service.microjc.CustomUtils;
import com.service.microjc.InterFace.LibraryApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.LibraryUserInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.service.microjc.stType.TimeInfo;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.time.LocalTime;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryUserInfoActivity extends AppCompatActivity {
    public static final String TAG = "MAIN";
    public String username;//定义全局变量用户名和密码
    public String password;
    //UI TEXT
    TextView studentNameTextView;
    TextView studentIdTextView;
    TextView facultyTextView;
    TextView borrowedTextView;
    TextView availableTextView;
    TextView overtimeTextView;
    TextView booksDataTextView;

    //主页card时间信息
    String tsgCardTime;

    private SharedPreferences sp;
    private LibraryUserInfo libraryUserInfo;


//    //在主线程创建handler用于接收，子线程message压栈的待处理消息队列
//    private  Handler h = new Handler() {
//        //当收到消息时，应该执行的代码
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0){
//                Toast.makeText(LibraryUserInfoActivity.this,"访问出错",
//                        Toast.LENGTH_LONG).show();
//            }else if (msg.what == 1) {
//                LibraryUserInfo info = (LibraryUserInfo) msg.obj;
////                List<LibraryUserInfo> libraryUserInfo = (List<LibraryUserInfo>) msg.obj;
////                LibraryUserInfo Userinfo = new LibraryUserInfo();
////                Userinfo.setStudentName(info.getStudentName());
//                Log.d("TEST", info.toString());
//
//                //改变textview文本
//                studentNameTextView.setText(info.getStudentName());
//                studentIdTextView.setText(info.getStudentId());
//                facultyTextView.setText(info.getFaculty());
//                borrowedTextView.setText(info.getBorrowed());
//                availableTextView.setText(info.getAvailable());
//                overtimeTextView.setText(info.getOvertime());
////                booksDataTextView.setText(info.getBooksData());//借阅书籍详情打不出来
//
//            } else if (msg.what == 2) {
//               List<LibraryUserInfo.BooksInfo> data = (List<LibraryUserInfo.BooksInfo>) msg.obj;
//               bookAdapter.setData(data);
//               bookAdapter.notifyDataSetChanged();
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_userinfo);

        studentNameTextView = findViewById(R.id.studentName);
        studentIdTextView = findViewById(R.id.studentId);
        facultyTextView = findViewById(R.id.faculty);
        borrowedTextView = findViewById(R.id.borrowed);
        availableTextView = findViewById(R.id.available);
        overtimeTextView = findViewById(R.id.overtime);
        booksDataTextView = findViewById(R.id.booksData);

        //初始化
        DialogX.init(this);
        DialogX.globalStyle = new IOSStyle();//设置为IOS主题

        clear();
        SetMargin();
        comeBack();
        getThisIntent();

    }

    /**
     * 状态栏管理
     * */
    private void clear(){
        ImmersionBar.with(LibraryUserInfoActivity.this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    /**
     * icon 返回
     * */
    private void comeBack(){
        ImageView backIcon = findViewById(R.id.fanhui_jyxq);
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(LibraryUserInfoActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 获取状态栏高度，设置layout的margin——top值
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


        RelativeLayout titleLayout1 = findViewById(R.id.titleRelative_libuser);
        titleLayout1.setLayoutParams(lp);
    }

    /**
     * 获取intent和参数
     * */
    private void getThisIntent(){
        //接收LibraryLogin传过来的参数参数
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        //判断来自那个activity
        switch (intent.getStringExtra("from")) {
            case "libLogin":
                Log.e(TAG, " 登录进入" );
                libraryUserInfo = (LibraryUserInfo) getIntent().getSerializableExtra("userinfo");

                //改变textview文本
                studentNameTextView.setText(libraryUserInfo.getStudentName());
                studentIdTextView.setText(libraryUserInfo.getStudentId());
                facultyTextView.setText(libraryUserInfo.getFaculty());
                borrowedTextView.setText(libraryUserInfo.getBorrowed());
                availableTextView.setText(libraryUserInfo.getAvailable());
                overtimeTextView.setText(libraryUserInfo.getOvertime());

                sp = getSharedPreferences("LibUserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                //将查询到到的书 本数 存入sp
                String book = libraryUserInfo.getBorrowed()+"本";
                editor.putString("newBooks", book);
                editor.apply();

                break;
            case "school":
            case "schoolCard":
                Log.e(TAG, " 主页进入" );
                WaitDialog.show("正在查询");
                getLibUserInfo();//发起网络请求

                break;


        }
    }

    /***
     * 登录网络请求
     */
    public void getLibUserInfo(){
        //发起网络访问
        //实例化一个请求对象 api
        LibraryApi api = NetworkFactory.LibraryApi();
        Call<LibraryUserInfo> Y = api.getLibraryUserInfo(username, password);
        Log.e(TAG, "getLibUserInfo:>>>>>>>>>>>>>>>>>> "+username+password);
        Y.enqueue(new Callback<LibraryUserInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<LibraryUserInfo> call, @NotNull Response<LibraryUserInfo> response) {
                libraryUserInfo = new LibraryUserInfo();
                libraryUserInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象
//                assert libraryUserInfo != null;
//                Log.d("自动登陆网络请求", "自动登陆结果》》》》》》》》》》》"+libraryUserInfo.getStudentName());

                if (libraryUserInfo.getLoginStatus().equals("登录正常")) {
                    //改变textview文本
                    studentNameTextView.setText(libraryUserInfo.getStudentName());
                    studentIdTextView.setText(libraryUserInfo.getStudentId());
                    facultyTextView.setText(libraryUserInfo.getFaculty());
                    borrowedTextView.setText(libraryUserInfo.getBorrowed());
                    availableTextView.setText(libraryUserInfo.getAvailable());
                    overtimeTextView.setText(libraryUserInfo.getOvertime());

                    WaitDialog.dismiss();

                    Date date = new Date();
                    TimeInfo timeInfo = CustomUtils.LongToString(date);
                    tsgCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                    sp = getSharedPreferences("LibUserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    //将查询到到本数入sp
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


}