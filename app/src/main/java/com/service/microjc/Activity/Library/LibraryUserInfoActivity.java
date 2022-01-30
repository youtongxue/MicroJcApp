package com.service.microjc.Activity.Library;

import androidx.annotation.RequiresApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.InterFace.LibraryApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.LibraryUserInfo;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.service.microjc.stType.TimeInfo;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryUserInfoActivity extends BaseActivity {
    public static final String TAG = "MAIN";
    public String username;//定义全局变量用户名和密码
    public String password;
    //UI TEXT
    private TextView studentNameTextView;
    private TextView studentIdTextView;
    private TextView facultyTextView;
    private TextView borrowedTextView;
    private TextView availableTextView;
    private TextView overtimeTextView;
    private TextView booksDataTextView;

    //主页card时间信息
    private String tsgCardTime;

    private SharedPreferences sp;
    private LibraryUserInfo libraryUserInfo;
    private ImageView backIcon;


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
        backIcon = findViewById(R.id.fanhui_jyxq);

        //初始化
        DialogX.init(this);
        DialogX.globalStyle = new IOSStyle();//设置为IOS主题

        getThisIntent();

    }


    /**
     * 获取intent和参数
     * */
    private void getThisIntent(){
        //接收LibraryLogin传过来的参数参数
        Intent intent = getIntent();
        //判断来自那个activity
        switch (intent.getStringExtra("from")) {
            case "libLogin":
                Log.e(TAG, " 登录进入" );
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");
                libraryUserInfo = (LibraryUserInfo) intent.getSerializableExtra("userinfo");

                //改变textview文本
                studentNameTextView.setText(libraryUserInfo.getStudentName());
                studentIdTextView.setText(libraryUserInfo.getStudentId());
                facultyTextView.setText(libraryUserInfo.getFaculty());
                borrowedTextView.setText(libraryUserInfo.getBorrowed());
                availableTextView.setText(libraryUserInfo.getAvailable());
                overtimeTextView.setText(libraryUserInfo.getOvertime());

                //将本次查询到的数据存入sp本地
                Date date = new Date();
                TimeInfo timeInfo = CustomUtils.LongToString(date);
                String libCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

                sp = getSharedPreferences("LibUserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                //将查询到到的书 本数 存入sp
                String book = libraryUserInfo.getBorrowed()+"本";
                editor.putString("newBooks", book);
                editor.putString("lastTime", libCardTime);
                editor.apply();

                break;
            case "SchoolFragment":
                Log.e(TAG, " 主页进入" );
                DialogX.globalStyle = IOSStyle.style();//设置为IOS主题
                WaitDialog.show("正在查询");
                getLibUserInfo();//发起网络请求

                break;

        }
    }

    /***
     * 登录网络请求
     */
    public void getLibUserInfo(){
        sp = getSharedPreferences("LibraryUserLoginInfo", Context.MODE_PRIVATE);
        username = sp.getString("USERNAME","");
        password = sp.getString("PASSWORD","");
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

                    //将本次查询数据，存入sp中
                    Date date = new Date();
                    TimeInfo timeInfo = CustomUtils.LongToString(date);
                    tsgCardTime = timeInfo.getM()+"月"+timeInfo.getD()+"日"+"  "+timeInfo.getHmString();

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


}