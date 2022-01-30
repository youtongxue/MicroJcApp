package com.service.microjc.Activity.Jw;

import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.service.microjc.Activity.App.Utils.DownloadUtils;
import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.InterFace.JwApi;
import com.service.microjc.InterFace.Pure;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.JwUserInfo;
import com.service.microjc.stType.LoginInfo;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.service.microjc.stType.PureInfo;
import com.service.microjc.stType.RequestRoomInfo;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JwUserInfoActivity extends BaseActivity {
    public static final String TAG = "MAIN";
    public String username;//定义全局变量用户名和密码
    public String password;
    private String url;
    private LoginInfo loginInfo = new LoginInfo();
    private SharedPreferences sp;
    //listview
    public ListView mListView = null;
    /* 图片ID数组 */
    private final int[] mImageId = new int[] {R.drawable.ic_jw_cjcx, R.drawable.ic_jw_kscx, R.drawable.ic_jw_bkcx, R.drawable.ic_jw_kjscx, R.drawable.ic_jw_kbcx };
    /* 文字列表数组 */
    private final String[] mTitle = new String[] {"成绩查询", "考试查询", "补考查询", "空教室查询", "导入课表到Pure课程表"};
    //dialog弹窗
    private final String[] data = new String[]{"2020-2021学年-第1学期", "2020-2021学年-第2学期", "2021-2022学年-第1学期"};
    private int Index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jw_user_info);

        getTheIntent();
        jwListView();//listview
        //初始化
        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();//设置为MIUI主题

    }

    /**
     * 接收intent，获取参数
     * */
    private void getTheIntent(){
        Intent intent = getIntent();

        //需要判断intent来自那个activity
        //loginActivity那么就已经发起过网络请求，并传了信息过来
        switch (intent.getStringExtra("from")) {
            case "SchoolFragment":
                DialogX.globalStyle = IOSStyle.style();//设置为IOS主题
                Log.e(TAG, " 主页进入" );
                WaitDialog.show("正在查询");
                Login();//发起网络请求
                break;

            case "jwLogin":
                Log.e(TAG, " 登录进入" );
                //接收JwActivity传过来的参数参数loginInfo
                loginInfo = (LoginInfo) getIntent().getSerializableExtra("loginInfo");
                //设置UI视图
                TextView studentName = findViewById(R.id.studentname);
                studentName.setText(loginInfo.getStudentName());

        }
    }

    /**
     * 设置list view
     * */

    public void jwListView() {

        mListView = findViewById(R.id.jw_list);

        List<Map<String, Object>> mListItems = new ArrayList<>();
        for (int i = 0; i < mImageId.length; i++) {
            Map<String, Object> mMap = new HashMap<>();
            mMap.put("image", mImageId[i]);
            mMap.put("title", mTitle[i]);
            mListItems.add(mMap);
        }

        SimpleAdapter mAdapter = new SimpleAdapter(JwUserInfoActivity.this, mListItems, R.layout.jw_listview_item, new String[]{"title", "image"}, new int[]{R.id.jw_listitem_text, R.id.jw_listitem_img});
        mListView.setAdapter(mAdapter);

        //设置点击监听事件
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {

            switch (i){
                case 0:

                    Intent intent = new Intent();
                    intent.setClass(JwUserInfoActivity.this, ScoresInfoActivity.class);
                    //利用intent传参
                    intent.putExtra("loginInfo",loginInfo);
                    startActivity(intent);
                    break;
                case 1:

                    Intent intentSecond = new Intent();
                    intentSecond.setClass(JwUserInfoActivity.this, ExamInfoActivity.class);
                    //利用intent传参
                    intentSecond.putExtra("loginInfo",loginInfo);
                    intentSecond.putExtra("title","考试查询");
                    startActivity(intentSecond);
                    break;
                case 2:

                    Intent intentThree = new Intent();
                    intentThree.setClass(JwUserInfoActivity.this, ExamInfoActivity.class);
                    //利用intent传参
                    intentThree.putExtra("loginInfo",loginInfo);
                    intentThree.putExtra("title","补考查询");
                    startActivity(intentThree);

                    break;
                case 3:
                    //查询空教室
                    RequestRoomInfo requestRoomInfo = new RequestRoomInfo();
                    requestRoomInfo.setStudentName(loginInfo.getStudentName());
                    requestRoomInfo.setUsername(loginInfo.getUsername());
                    requestRoomInfo.setSessionId(loginInfo.getSessionId());

                    Intent intent1 = new Intent();
                    intent1.setClass(this,RoomActivity.class);
                    intent1.putExtra("RequestRoomInfo",requestRoomInfo);
                    startActivity(intent1);

                    break;
                case 4:

                    //获取课表源码保存到本地
                    setXn();
//                    getKbHtml();

                    break;
            }

//                Toast.makeText(getContext(), "Click item：" + i, Toast.LENGTH_SHORT).show();


        });
    }

    /**
     * 获取 课表 网络请求
     * */
    public void getKbHtml(String xn,String xq){
        //显示等待进度框
        WaitDialog.show("正在处理...");

        loginInfo.setXn(xn);
        loginInfo.setXq(xq);

        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getSessionId());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getUsername());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getPassword());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getCheckCode());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getStudentName());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.get__VIEWSTATE());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+xn);
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+xq);

        JwApi api = NetworkFactory.jwApi();
        Call<ResponseBody> r = api.getKbHtml(loginInfo);
        r.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String html = null;
                try {
                    html = Objects.requireNonNull(response.body()).string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                response.body().byteStream();
//                Log.e(TAG, "onResponse: "+html);

//                isGrantExternalRW(JwUserInfoActivity.this);

                if (html != null){
                    CustomUtils.writeTxtToFile(html,"data/data/com.service.microjc/files/html/","Kb.txt");

                    //获取uri
                    File kbPath = new File(JwUserInfoActivity.this.getFilesDir(),"html");
                    File newFile = new File(kbPath,"Kb.txt");
                    Uri KbUri = FileProvider.getUriForFile(JwUserInfoActivity.this,"com.service.microjc.fileprovider",newFile);

                    //尝试隐式跳转pure
                    Intent intent = new Intent("tool.xfy9326.schedule.action.EXTERNAL_COURSE_IMPORT");
                    intent.putExtra( "PROCESSOR_NAME","SCUJCC");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setData(KbUri);

                    startActivity(intent);
                    //跳转前判断
                    PackageManager packageManager = getPackageManager();
                    if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)!= null){
                        WaitDialog.dismiss();//关闭加载，动画
                        startActivity(intent);
                    }else {
                        WaitDialog.dismiss();//关闭加载，动画

                        MessageDialog.build()
                                .setStyle(IOSStyle.style())
                                .setTheme(DialogX.THEME.AUTO)
                                .setTitle("提示")
                                .setMessage("未安装Pure课程表")
                                .setCancelButton("取消")
                                .setOkButton("下载Pure")
                                .setCancelable(false)
                                .setBackgroundColor(Color.parseColor("#FFFFFF"))
                                .setOkButton((baseDialog, v) -> {
                                    getPureInfoHttp();

                                    return false;
                                })
                                .show();
                    }
                }


                //读取
                File file = new File("data/data/com.service.microjc/files/html/Kb.txt");
                String Html = CustomUtils.getFileContent(file);
                Log.e(TAG, "onResponse: "+Html );
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                MessageDialog.build()
                        .setStyle(IOSStyle.style())
                        .setTheme(DialogX.THEME.AUTO)
                        .setTitle("🙅‍♂")
                        .setMessage("似乎网络出问题啦\uD83E\uDD33")
                        .setOkButton("确定")
                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
                        .show();

            }
        });

    }

//    //动态申请权限
//    public static boolean isGrantExternalRW(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            activity.requestPermissions(new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, 1);
//            return false;
//        }
//        return true;
//    }

    /**
     * 获取pure
     * */
    private void getPureInfoHttp(){

        Pure api = NetworkFactory.getPureInfo();
        Call<PureInfo> Y = api.getPureInfo();
        Y.enqueue(new Callback<PureInfo>() {
            @Override
            public void onResponse(Call<PureInfo> call, Response<PureInfo> response) {
                PureInfo pureInfo = response.body();

                DownloadUtils downloadUtils = new DownloadUtils(JwUserInfoActivity.this);
                downloadUtils.downloadAPK(pureInfo.getUrl(),"Pure课程表.apk","Pure课程表"+pureInfo.getVersionName()+".apk");

            }

            @Override
            public void onFailure(Call<PureInfo> call, Throwable t) {

            }
        });
    }

    private void setXn(){
        Map<String, String> xnInfo = new HashMap<>();
        xnInfo.put("0", "2020-2021");
        xnInfo.put("1","2020-2021");
        xnInfo.put("2","2021-2022");

        Map<String, String> xqInfo = new HashMap<>();
        xqInfo.put("0", "1");
        xqInfo.put("1", "2");
        xqInfo.put("2", "1");

         BottomMenu.show(data)
                .setStyle(MIUIStyle.style())
                .setTheme(DialogX.THEME.AUTO)
//                        .setMessage("这里是权限确认的文本说明，这是一个演示菜单")
                .setTitle("选择学年")
                .setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                    @Override
                    public void onOneItemSelect(BottomMenu dialog, CharSequence text, int index, boolean select) {
                        Index = index;
                    }
                })
                .setCancelButton("确定", (OnDialogButtonClickListener<BottomDialog>) (baseDialog, v1) -> {
                    String xnd = xnInfo.get(String.valueOf(Index));
                    String xqd = xqInfo.get(String.valueOf(Index));

                    getKbHtml(xnd,xqd);
                    return false;
                })
                .setSelection(Index);
    }

    /**
     * 设置滚动布局
     * */
    private void scroll(){
        RefreshLayout refreshLayout = findViewById(R.id.jwUserInfo_refreshLayout);

        refreshLayout.setEnablePureScrollMode(true);
//        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
    }


    /**
     * 登录的网络请求
     * */
    public void Login(){
        //第一步
        JwApi api = NetworkFactory.jwApi();
        Call<LoginInfo> Y = api.getLoginInfo();
        Y.enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(@NotNull Call<LoginInfo> call, @NotNull Response<LoginInfo> response) {
                loginInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象

                assert loginInfo != null;
                Log.e(">>>>>>>", "当session值为》》》》》》》》：" + loginInfo.getSessionId());

                //getCheckCode();//调用显示验证码方法，防止空指针
                //开始登录
                //从sp中拿到用户信息
                sp = getSharedPreferences("JwUserLoginInfo", Context.MODE_PRIVATE);
                username = sp.getString("USERNAME","");
                password = sp.getString("PASSWORD","");
                //登录网络请求
                loginInfo.setUsername(username);
                loginInfo.setPassword(password);

                Log.e(TAG, "Login: 自动登录参数"+username+password );

                JwApi api1 = NetworkFactory.jwApi();
                Call<JwUserInfo> Y1 = api1.getLoginStatus(loginInfo);
                Y1.enqueue(new Callback<JwUserInfo>() {

                    @Override
                    public void onResponse(@NotNull Call<JwUserInfo> call, @NotNull Response<JwUserInfo> response) {

                        JwUserInfo jwUserInfo = response.body();//实例化一个userinfo对象，将网络请求响应body内容给对象
                        assert jwUserInfo != null;
                        loginInfo.setStudentName(jwUserInfo.getStudentName());//将服务器返回的学生名字，存入实体类

                        Log.e(">>>>>>>", "判断jwUserInfo值是否为空》》》》》》》》：" + jwUserInfo.getLoginStatus());

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
                            case "验证码错误,正在重试":
                                TipDialog.show("自动识别验证码错误", WaitDialog.TYPE.ERROR,2000);
                                finish();
                                break;
                            case "登录正常":
                                WaitDialog.dismiss();
                                TextView studentName = findViewById(R.id.studentname);
                                studentName.setText(loginInfo.getStudentName());
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

            @Override
            public void onFailure(@NotNull Call<LoginInfo> call, @NotNull Throwable t) {

            }
        });


    }
}