package com.service.microjc.Activity.Jw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnCustomWheelPickerSelected;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnWheelChangeListener;
import com.kongzue.dialogx.style.IOSStyle;
import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
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

public class ScoresInfoActivity extends BaseActivity {
//    public String username;//定义全局变量用户名和密码
//    public String password;
    private final List<ScoresInfo.Info> arrayList = new ArrayList<>();
    private ScoresInfoAdapter recyclerViewAdapter;
    private LoginInfo loginInfo = new LoginInfo();
    private TextView xn;
    //dialog弹窗
    private int[] defaultCustomWheelSelect = new int[]{3,0};//wheelpicker选中默认行


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_info);

        //初始化
        DialogX.init(this);

        selectXnXq();//选择学年，学期监听事件
        initView();
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
     * 选择学年监听
     * */
    private void selectXnXq(){
        //设置为IOS主题
        DialogX.globalStyle = new MIUIStyle();

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
        xn.setOnClickListener(v -> {
            DialogX.globalStyle = new MIUIStyle();
            CustomWheelPickerDialog.build()
                    .addWheel(new String[]{"2018-2019学年", "2019-2020学年", "2020-2021学年", "2021-2022学年"})
                    .addWheel(new String[]{"第1学期", "第2学期", "第3学期"})    //添加列表项
                    .setOnWheelChangeListener(new OnWheelChangeListener() {
                        /**
                         * 当滚轮滑动时触发
                         * @param picker            滑动对话框
                         * @param wheelIndex        当前是第几个列表项触发滑动
                         * @param originWheelData   原始列表项数据
                         * @param itemIndex         已选中数据的索引
                         * @param itemText          已选中数据的内容
                         */
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onWheel(CustomWheelPickerDialog picker, int wheelIndex, String[] originWheelData, int itemIndex, String itemText) {


                        }
                    })
                    .setDefaultSelect(0, defaultCustomWheelSelect[0])
                    .setDefaultSelect(1, defaultCustomWheelSelect[1])
                    .show(new OnCustomWheelPickerSelected() {
                        /**
                         * 当确认后，
                         * @param picker        滑动对话框
                         * @param text          返回默认文本，例如“初中 初4班 声乐组”
                         * @param selectedTexts 选中的每个列表项文本集合
                         * @param selectedIndex 选中的每个列表项索引集合
                         */
                        @Override
                        public void onSelected(CustomWheelPickerDialog picker, String text, String[] selectedTexts, int[] selectedIndex) {
                            defaultCustomWheelSelect = selectedIndex;

                            //更改UI
                            xn.setText(selectedTexts[0]+"的"+selectedTexts[1]);
                            //发起网络请求
                            loginInfo.setXn(xnInfo.get(selectedTexts[0]));
                            loginInfo.setXq(xqInfo.get(selectedTexts[1]));

                            //查询http
                            getScores();

                        }
                    });
        });


    }


    /**
     * 查询成绩网络请求
     * */
    private void getScores(){
        DialogX.globalStyle = new IOSStyle();
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