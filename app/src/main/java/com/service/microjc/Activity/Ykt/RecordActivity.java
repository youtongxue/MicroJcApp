package com.service.microjc.Activity.Ykt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.InterFace.YktApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.RecordsBean;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordActivity extends BaseActivity {
    public String username;//定义全局变量用户名和密码
    public String password;
    public String start;//存储用于消费记录网络请求的 开始 时间 2021-8-8 这种格式
    public String end;//结束时间
    private final List<RecordsBean.recordsInfosBean> arrayList = new ArrayList<>();
    private RecordAdapter recyclerViewAdapter;
    private TextView start_text;//UI显示开始时间
    private TextView end_text;//UI显示结束时间
    private Long now_time_start = null;//存储选择的开始时间戳
    private Long now_time_end = null;//存储选择的结束时间戳
    private Long l = null;//存储选择的结束时间戳
    private long MaxTimeStamp = System.currentTimeMillis();//当前long格式时间戳
    private long MinTimeStamp;//用于设置最小时间戳，是当前时间往前推5年

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_info);

        //设置为IOS主题
        DialogX.globalStyle = new IOSStyle();


        getMinTimeStamp();

        Log.e("方法是否执行", "onCreate:>>>>>>>>>>>>>>>>>>> "+MinTimeStamp );

        initView();
        //初始化
        DialogX.init(this);

        //获取当前时间，Util包的Date
        Date date = new Date();//这里的date是时间戳
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //默认查询开始时间，为现在时间
        //默认查询结束时间，为现在时间
        String defaultEnd;
        String defaultStart = defaultEnd = start = dateFormat.format(date);//设置默认时间，点击文本选择的时间>>>>>>为现在时间

        now_time_start = now_time_end = l = date.getTime();

        //更新UI层，利用Calendar获取 年、月、日
        String StringDate = LongToString(date);
        start_text = findViewById(R.id.start);
        end_text = findViewById(R.id.end);
        start_text.setText(StringDate);
        end_text.setText(StringDate);

        getRecordsInfo(defaultStart, defaultEnd);//获取现在时间的消费记录，显示在UI层





        /*
         * 选择时间
         * */
        //对选择时间布局，设置监听事件
        start_text = findViewById(R.id.start);
        end_text = findViewById(R.id.end);

        start_text.setOnClickListener(v -> setStartTime());
        //结束时间
        end_text.setOnClickListener(v -> {
            if (now_time_start.equals(l)){

                PopTip.show("请先选择起始时间");

            }else{
                setEndTime();
            }
        });



    }


    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.RecordsRecyclerView);//查找recyclerview控件

        LinearLayoutManager manager = new LinearLayoutManager(this);//创建线性布局管理器
        manager.setOrientation(LinearLayoutManager.VERTICAL);//添加垂直布局
        recyclerView.setLayoutManager(manager);//将线性布局管理器添加到recyclerview中
        recyclerViewAdapter = new RecordAdapter(getApplicationContext(),arrayList);//实例化适配器
        recyclerView.setAdapter(recyclerViewAdapter);//添加适配器
    }


    /**
     * 消费记录网络请求
     * */
    public void getRecordsInfo(String start,String end){
        //显示等待进度框
        WaitDialog.show("正在查询中...");

        //接收YktLogin传过来的参数参数
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        //实例化一个请求对象 api
        YktApi api = NetworkFactory.YktApi();
        Call<RecordsBean> l = api.getLibraryUserInfo(username,password,start,end);//传参调用api网络请求
        Log.e("网络请求", "网络请求发起 "+username+password+start+end);
        l.enqueue(new Callback<RecordsBean>() {
            private static final String TAG = "MAIN";

            @Override
            public void onResponse(@NotNull Call<RecordsBean> call, @NotNull Response<RecordsBean> response) {
                arrayList.clear();

                RecordsBean bean = response.body();
                Log.d(TAG, "00000初始集合的长度为>>>>>>>>：" + arrayList.size());
                assert bean != null;
                arrayList.addAll(bean.getRecordsInfos());
                Log.d(TAG, "11111初始集合的长度为>>>>>>>>：" + arrayList.size());
//                arrayList.clear();
                recyclerViewAdapter.refresh(arrayList);
                Log.d(TAG, "22222初始集合的长度为>>>>>>>>：" + arrayList.size());

                WaitDialog.dismiss();//关闭正在加载动画
            }
            @Override
            public void onFailure(@NotNull Call<RecordsBean> call, @NotNull Throwable t) {
            }
        });
    }

    /**
     * 选择时间
     * */
    public void setStartTime(){

        new CardDatePickerDialog.Builder(RecordActivity.this)
                .setTitle("选择起始日期")
                .showBackNow(false)//设置是否显示回到当前日期
                .setDisplayType(0,1,2)//设置显示的时间，这个方法传入一个int list 顺序为 年 - 月 - 日 - 时 - 分 - 秒
                .setBackGroundModel(CardDatePickerDialog.STACK)//设置底部弹出样式
                .setThemeColor(Color.parseColor("#4DADFF"))//设置颜色
                .setWrapSelectorWheel(false)//设置是否可以循环滚动
                .setMinTime(MinTimeStamp)
                .setMaxTime(MaxTimeStamp)
                .setDefaultTime(0)
                .showFocusDateInfo(true)
                .setOnChoose("确定", aLong -> {
                    //将aLong转换成日期2020-01-01格式
                    Date date = new Date(aLong);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    start = sd.format(date);
                    now_time_start = aLong;//起始时间戳

                    //更新UI层
                    String NewStartText = LongToString(aLong);
                    start_text.setText(NewStartText);

                    //设置截止日期的最大值
                    setMaxStamp(aLong);

                    return null;
                })
                .build()
                .show();
    }

    //选择结束时间
    public void setEndTime(){


        new CardDatePickerDialog.Builder(RecordActivity.this)
                .setTitle("选择截止日期")
                .showBackNow(false)//设置是否显示回到当前日期
                .setDisplayType(0,1,2)//设置显示的时间，这个方法传入一个int list 顺序为 年 - 月 - 日 - 时 - 分 - 秒
                .setBackGroundModel(CardDatePickerDialog.STACK)//设置底部弹出样式
                .setThemeColor(Color.parseColor("#0FAF5B"))//设置颜色
                .setWrapSelectorWheel(false)//设置是否可以循环滚动
                .setMaxTime(MaxTimeStamp)
                .setMinTime(now_time_start)
                .setDefaultTime(0)
                .showFocusDateInfo(true)
                .setOnChoose("查询", aLong -> {
                    //将aLong转换成日期2020-01-01格式
                    Date date = new Date(aLong);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    end = sd.format(date);

                    now_time_end = aLong;

                    //更新UI层
                    String NewEndText = LongToString(aLong);
                    end_text.setText(NewEndText);

                    //发起网络访问，查询选择时间的消费记录
                    getRecordsInfo(start,end);

                    return null;
                })
                .build()
                .show();
    }

    /**
     * @Description 将当前时间戳往前推5年，转换成long
     * */
    public void getMinTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(MaxTimeStamp);//获取当前时间戳
        //保存当前年
        int nowYear = calendar.get(Calendar.YEAR);//获取当前时间戳里面的  年

        //将时间格式转化成long
        calendar.set((nowYear -5), 0, 1);
        Date dateEnd = calendar.getTime();

        MinTimeStamp = dateEnd.getTime();
    }

    /**
     * 将Long或Date类型时间戳转化成 2021-08-10 String格式
     * */
    public String LongToString(Object o){
        String M,D;
        //将long转换成Calendar类型，获取int 型的 年 月 日，显示在UI
        //Date也可以获取，但是工具提示已经过时
        Calendar calendar = Calendar.getInstance();

        if (o instanceof Long){
        calendar.setTimeInMillis((Long) o);
        }else if (o instanceof Date){
            calendar.setTime((Date) o);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (month < 9){
            M =  "0"+(month+1);
        }else {
            M = String.valueOf(month+1);
        }

        if (day < 10){
            D = "0"+day;
        }else {
            D = String.valueOf(day);
        }

        return (year+"年"+M+"月"+D+"日");

    }

    /**
     * 将选择时间范围限定在50天内
     * */
    public void setMaxStamp(Long aLong){
        Date date = new Date(aLong);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,+50);//当前时间减50天

        date = calendar.getTime();
        long MaxDay = date.getTime();//转换成long型时间戳


        if (MaxTimeStamp > MaxDay ){

            MaxTimeStamp = MaxDay;

        }


    }

}