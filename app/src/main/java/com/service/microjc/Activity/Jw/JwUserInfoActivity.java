package com.service.microjc.Activity.Jw;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.service.microjc.Activity.App.DownloadUtils;
import com.service.microjc.Activity.App.MainActivity;
import com.service.microjc.CustomUtils;
import com.service.microjc.InterFace.JwApi;
import com.service.microjc.InterFace.Pure;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.LoginInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.service.microjc.stType.PureInfo;

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

public class JwUserInfoActivity extends AppCompatActivity {
    public static final String TAG = "MAIN";
    public String username;//定义全局变量用户名和密码
    public String password;
    private String url;
    private LoginInfo loginInfo = new LoginInfo();
    //listview
    public ListView mListView = null;

    /* 图片ID数组 */
    private final int[] mImageId = new int[] {R.drawable.ic_about, R.drawable.ic_about, R.drawable.ic_about, R.drawable.ic_about };
    /* 文字列表数组 */
    private final String[] mTitle = new String[] {"成绩查询", "考试查询", "补考查询", "导入课表到Pure课程表"};


    //dialog弹窗
    private final String[] data = new String[]{"2020-2021学年-第1学期", "2020-2021学年-第2学期", "2021-2022学年-第1学期"};
    private int Index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jw_user_info);

        DialogX.globalStyle = MIUIStyle.style();//设置为MIUI主题

        SetMargin();
        goBack();
        jwListView();//listview

        ImmersionBar.with(JwUserInfoActivity.this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        //初始化
        DialogX.init(this);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);//清除

        //接收JwActivity传过来的参数参数loginInfo

        loginInfo = (LoginInfo) getIntent().getSerializableExtra("loginInfo");

        //设置UI视图
        TextView studentName = findViewById(R.id.studentname);
        studentName.setText(loginInfo.getStudentName());



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


        RelativeLayout titleLayout1 = findViewById(R.id.titleRelative_jwuser);
        titleLayout1.setLayoutParams(lp);
    }

    /**
     * 图标返回
     * */
    public void goBack(){
        ImageView backIcon = findViewById(R.id.fanhui_jw);

        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(JwUserInfoActivity.this, MainActivity.class);
            startActivity(intent);
        });
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
}