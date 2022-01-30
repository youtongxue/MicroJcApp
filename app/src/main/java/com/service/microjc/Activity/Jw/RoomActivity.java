package com.service.microjc.Activity.Jw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnCustomWheelPickerSelected;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnWheelChangeListener;
import com.kongzue.dialogx.datepicker.CalendarDialog;
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.InterFace.JwApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.ExamInfo;
import com.service.microjc.stType.LoginInfo;
import com.service.microjc.stType.RequestRoomInfo;
import com.service.microjc.stType.RoomResultInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends BaseActivity {
    private List<RoomResultInfo.roomsInfo> roomsInfoArrayList = new ArrayList<>();
    private TextView roomTime;
    private TextView roomClassTime;
    private TextView roomBuild;
    private RoomAdapter recyclerViewAdapter;
    private int defaultYear = 2021, defaultMonth = 11, defaultDay = 10;
    private int[] defaultCustomWheelSelect = new int[]{0};//wheelpicker选中默认行
    private RequestRoomInfo requestRoomInfo = new RequestRoomInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //初始化
        DialogX.init(this);

        roomTime = findViewById(R.id.roomTime);
        roomClassTime = findViewById(R.id.roomClassTime);
        roomBuild = findViewById(R.id.roomBuild);

        //存入待请求的实体类
        requestRoomInfo = (RequestRoomInfo) getIntent().getSerializableExtra("RequestRoomInfo");

        setRoomTime();
        setRoomClassTime();
        setRoomBuild();
        initView();

    }

    //recyclerView绑定
    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.RoomRecyclerView);//查找recyclerview控件

        LinearLayoutManager manager = new LinearLayoutManager(this);//创建线性布局管理器
        manager.setOrientation(LinearLayoutManager.VERTICAL);//添加垂直布局
        recyclerView.setLayoutManager(manager);//将线性布局管理器添加到recyclerview中
        recyclerViewAdapter = new RoomAdapter(getApplicationContext(),roomsInfoArrayList);//实例化适配器
        recyclerView.setAdapter(recyclerViewAdapter);//添加适配器
    }

    //点击监听事件
    public void setRoomTime(){
        DialogX.globalStyle = new MIUIStyle();
        roomTime.setOnClickListener(v -> {
            CalendarDialog.build()
                    .setMinTime(2021, 11, 10)            //指定最小可选日期 1990年5月20日
                    .setMaxTime(2022, 1, 18)            //指定最大可选日期 2030年2月10日
                    .setDefaultSelect(defaultYear, defaultMonth, defaultDay)    //设置默认选中日期
                    .show(new OnDateSelected() {
                        @Override
                        public void onSelect(String text, int year, int month, int day) {
                            roomTime.setText(text);

                            requestRoomInfo.setStartTime(text);
                            requestRoomInfo.setEndTime(text);

                            defaultYear = year;
                            defaultMonth = month;
                            defaultDay = day;
                        }
                    });
        });

    }

    //
    public void setRoomClassTime(){
        Map<String,String> ClassTime = new HashMap<>();

        ClassTime.put("上午第一节","0");
        ClassTime.put("上午第二节","1");
        ClassTime.put("下午第一节","2");
        ClassTime.put("下午第二节","3");
        ClassTime.put("晚上一二节","4");
        ClassTime.put("上午","5");
        ClassTime.put("下午","6");
        ClassTime.put("白天","8");
        ClassTime.put("晚上","7");
        ClassTime.put("整天","9");

        roomClassTime.setOnClickListener(v -> {
            DialogX.globalStyle = new MIUIStyle();
            CustomWheelPickerDialog.build()
                    .addWheel(new String[]{"上午第一节", "上午第二节", "下午第一节", "下午第二节", "晚上一二节", "上午", "下午", "白天", "晚上", "整天"})
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
                            //roomClassTime.setText(ClassTime.get(text));
                            roomClassTime.setText(text);
                            //存入实体类
                            requestRoomInfo.setClassTime(ClassTime.get(text));

                        }
                    });


        });

    }

    //
    private void setRoomBuild(){
        Map<String,String> ClassBuild = new HashMap<>();

        ClassBuild.put("A教","0");
        ClassBuild.put("B教","1");
        ClassBuild.put("C教","2");
        ClassBuild.put("全部","3");
        ClassBuild.put("所有类型空教室","4");

        roomBuild.setOnClickListener(v -> {
            DialogX.globalStyle = new MIUIStyle();
            CustomWheelPickerDialog.build()
                    .addWheel(new String[]{"A教", "B教", "C教", "全部", "所有类型空教室"})
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
                            //roomClassTime.setText(ClassTime.get(text));
                            roomBuild.setText(text);

                            requestRoomInfo.setSearchType(ClassBuild.get(text));

                            //查询网络请求
                            getRoom();

                        }
                    });


        });
    }

    /**
     *网络请求
     * */
    private void getRoom(){
        Log.e("TAG", "getRoom: 》》》》》》执行一次网络查询空教室" );
        DialogX.globalStyle = new IOSStyle();
        CustomUtils.runTime(false);

        //调用前先清空集合内容
        roomsInfoArrayList.clear();


        JwApi api = NetworkFactory.jwApi();
        Call<RoomResultInfo> r = api.getRoom(requestRoomInfo);
        r.enqueue(new Callback<RoomResultInfo>() {
            @Override
            public void onResponse(Call<RoomResultInfo> call, Response<RoomResultInfo> response) {

                RoomResultInfo roomResultInfo = response.body();
                assert roomResultInfo != null;
                roomsInfoArrayList.addAll(roomResultInfo.getRoomsInfoArrayList());
                recyclerViewAdapter.refresh(roomsInfoArrayList);

                CustomUtils.runTime(true);

            }

            @Override
            public void onFailure(Call<RoomResultInfo> call, Throwable t) {
                CustomUtils.runTime(true);
                TipDialog.show("查询失败", WaitDialog.TYPE.ERROR,2000);

            }
        });
    }
}