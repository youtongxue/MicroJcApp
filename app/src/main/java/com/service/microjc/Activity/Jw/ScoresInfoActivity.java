package com.service.microjc.Activity.Jw;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.service.microjc.InterFace.JwApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.LoginInfo;
import com.service.microjc.stType.ScoresInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.kongzue.dialogx.style.MIUIStyle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoresInfoActivity extends AppCompatActivity {
//    public String username;//定义全局变量用户名和密码
//    public String password;
    private final List<ScoresInfo.Info> arrayList = new ArrayList<>();
    private ScoresInfoAdapter recyclerViewAdapter;
    private LoginInfo loginInfo = new LoginInfo();
    private TextView xn;
    private TextView xq;
    //dialog弹窗
    private final String[] singleSelectMenuText = new String[]{"2016-2017学年","2017-2018学年","2018-2019学年","2019-2020学年", "2020-2021学年"};
    private int selectMenuIndex;

    private final String[] data = new String[]{"第1学期", "第2学期", "第3学期"};
    private int Index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_info);

        //初始化
        DialogX.init(this);
        //设置为IOS主题
        DialogX.globalStyle = new MIUIStyle();

        SetMargin();
        goBack();
        selectXnXq();//选择学年，学期监听事件

        initView();
        ImmersionBar.with(ScoresInfoActivity.this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);//清除





    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.ScoresRecyclerView);//查找recyclerview控件

        LinearLayoutManager manager = new LinearLayoutManager(this);//创建线性布局管理器
        manager.setOrientation(LinearLayoutManager.VERTICAL);//添加垂直布局
        recyclerView.setLayoutManager(manager);//将线性布局管理器添加到recyclerview中
        recyclerViewAdapter = new ScoresInfoAdapter(getApplicationContext(),arrayList);//实例化适配器
        recyclerView.setAdapter(recyclerViewAdapter);//添加适配器
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


        RelativeLayout titleLayout1 = findViewById(R.id.titleRelative_scores);
        titleLayout1.setLayoutParams(lp);
    }

    /**
     * 图标返回
     * */
    public void goBack(){
        ImageView fanhui_img = findViewById(R.id.fanhui_jwUserInfo);

        fanhui_img.setOnClickListener(v -> finish());
    }

    /**
     * 选择学年监听
     * */
    private void selectXnXq(){
        //接收JwUserInfoActivity传过来的参数
        loginInfo = (LoginInfo) getIntent().getSerializableExtra("loginInfo");

        //确定，按钮回调事件拿value值
        Map<String, String> xnInfo = new HashMap<>();
        xnInfo.put("2016-2017学年", "2016-2017");
        xnInfo.put("2017-2018学年", "2017-2018");
        xnInfo.put("2018-2019学年", "2018-2019");
        xnInfo.put("2019-2020学年", "2019-2020");
        xnInfo.put("2020-2021学年", "2020-2021");

        Map<String, String> xqInfo = new HashMap<>();
        xqInfo.put("第1学期", "1");
        xqInfo.put("第2学期", "2");
        xqInfo.put("第3学期", "3");

        xn = findViewById(R.id.scord_xn);
        xq = findViewById(R.id.scord_xq);
        xn.setOnClickListener(v -> BottomMenu.show(singleSelectMenuText)
                .setStyle(MIUIStyle.style())
                .setTheme(DialogX.THEME.AUTO)
//                        .setMessage("这里是权限确认的文本说明，这是一个演示菜单")
                .setTitle("选择学年")
                .setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                    @Override
                    public void onOneItemSelect(BottomMenu dialog, CharSequence text, int index, boolean select) {
                        selectMenuIndex = index;
                    }
                })
                .setCancelButton("确定", (OnDialogButtonClickListener<BottomDialog>) (baseDialog, v1) -> {

                    Log.e("test", "selectXnXq: "+xnInfo.get(singleSelectMenuText[selectMenuIndex]) );

                    loginInfo.setXn(xnInfo.get(singleSelectMenuText[selectMenuIndex]));//注入实体类
                    xn.setText(singleSelectMenuText[selectMenuIndex]);
                    return false;
                })
                .setSelection(selectMenuIndex));

        //设置学期
        xq.setOnClickListener(v -> BottomMenu.show(data)
                .setStyle(MIUIStyle.style())
                .setTheme(DialogX.THEME.AUTO)
//                        .setMessage("这里是权限确认的文本说明，这是一个演示菜单")
                .setTitle("选择学期")
                .setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                    @Override
                    public void onOneItemSelect(BottomMenu dialog, CharSequence text, int index, boolean select) {
                        Index = index;
                    }
                })
                .setCancelButton("查询", (OnDialogButtonClickListener<BottomDialog>) (baseDialog, v12) -> {

                    loginInfo.setXq(xqInfo.get(data[Index]));//注入实体类
                    xq.setText(data[Index]);//更新UI界面

                    //调用查询http请求
                    getScores();

                    return false;
                })
                .setSelection(Index));


    }


    /**
     * 查询成绩网络请求
     * */
    private void getScores(){
        //调用前先清空集合内容
        arrayList.clear();
        //显示等待进度框
        WaitDialog.show("正在查询中...");

        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getSessionId());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getUsername());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getPassword());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getCheckCode());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getStudentName());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.get__VIEWSTATE());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getXn());
        Log.e("test","info信息>>>>>>>>>>>>>>>>>>"+loginInfo.getXq());
        //实例化一个请求对象 api
        JwApi api = NetworkFactory.jwApi();
        Call<ScoresInfo> Y = api.getScoresInfo(loginInfo);
        Y.enqueue(new Callback<ScoresInfo>() {
            private static final String TAG = "MAIN";

            @Override
            public void onResponse(@NotNull Call<ScoresInfo> call, @NotNull Response<ScoresInfo> response) {
                ScoresInfo bean = response.body();
                Log.d(TAG, "00000初始集合的长度为>>>>>>>>：" + arrayList.size());
                arrayList.addAll(bean.getInfos());
                Log.d(TAG, "11111初始集合的长度为>>>>>>>>：" + arrayList.size());
//                arrayList.clear();
                recyclerViewAdapter.refresh(arrayList);
                Log.d(TAG, "22222初始集合的长度为>>>>>>>>：" + arrayList.size());

                WaitDialog.dismiss();//关闭等待动画
            }
            @Override
            public void onFailure(@NotNull Call<ScoresInfo> call, @NotNull Throwable t) {
                TipDialog.show("网络出错", WaitDialog.TYPE.ERROR);
            }
        });

    }
}