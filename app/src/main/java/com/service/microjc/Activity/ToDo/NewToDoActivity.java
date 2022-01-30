package com.service.microjc.Activity.ToDo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.R;
import com.service.microjc.stType.TimeInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

public class NewToDoActivity extends AppCompatActivity {
    private long MinTimeStamp = System.currentTimeMillis();//当前long格式时间戳
    private long ymdPickerStart;
    private long hmPickerEnd;
    private SharedPreferences sp;

    private TextView ymdStartText;
    private TextView hmStartText;
    private TextView ymdEndText;
    private TextView hmEndText;

    private int id;
    private String title;
    private String info;
    private String local;
    private String ymdStart;
    private String start;
    private String ymdEnd;
    private String end;
    private int hm;
    private int tip;
    private String repeat = "不重复";//
    private String select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);
        sp = getSharedPreferences("ToDoId", Context.MODE_PRIVATE);

        ymdStartText = findViewById(R.id.todo_ymdStart);
        hmStartText = findViewById(R.id.todo_hmStart);
        ymdEndText = findViewById(R.id.todo_ymdEnd);
        hmEndText = findViewById(R.id.todo_hmEnd);


        clear();
        SetMargin();
        goBack();
        saveToDoInfo();
        setDefTime();

    }

    /**
     * 图标返回
     * */
    private void goBack(){
        //图标
        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(v -> finish());
    }

    /**
     * 获取状态栏高度，设置layout的margin——top值
     *
     * */
    private void SetMargin(){
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


        RelativeLayout titleLayout1 = findViewById(R.id.titleRelative_newToDo);
        titleLayout1.setLayoutParams(lp);

//        setScroller();
    }

    /**
     * 设置状态栏状态，颜色，底部虚拟导航栏颜色
     * */
    private void clear(){
        ImmersionBar.with(NewToDoActivity.this)
                .statusBarColor(R.color.defBackground)
                .navigationBarColor(R.color.defBackground)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    /**
     * 设置默认起始和结束时间,如果用户不选择时间
     * */
    private void setDefTime(){
        TimeInfo timeDef = CustomUtils.LongToString(MinTimeStamp);

        //设置UI默认视图
        ymdStartText.setText(timeDef.getY_m_d()+timeDef.getWeek());
        hmStartText.setText(timeDef.getHmString());
        ymdEndText.setText(timeDef.getY_m_d()+timeDef.getWeek());
        hmEndText.setText(timeDef.getHmString());

        //存入数据库数据
        ymdStart = timeDef.getY_m_d()+timeDef.getWeek();
        ymdEnd = timeDef.getY_m_d()+timeDef.getWeek();
        start = timeDef.getHmString();//开始时间 19：00格式
        end = timeDef.getHmString();
        hm = Integer.parseInt(timeDef.getHm());
        select = timeDef.getYmd();




    }

    /**
     * 保存按键监听事件
     * */
    private void saveToDoInfo(){

        TextView textView = findViewById(R.id.saveToDO_text);

        ymdStartText.setOnClickListener(v -> {
            new CardDatePickerDialog.Builder(NewToDoActivity.this)
                    .setTitle("选择起始日期")
                    .showBackNow(false)//设置是否显示回到当前日期
                    .setDisplayType(0,1,2)//设置显示的时间，这个方法传入一个int list 顺序为 年 - 月 - 日 - 时 - 分 - 秒
                    .setBackGroundModel(CardDatePickerDialog.STACK)//设置底部弹出样式
                    .setThemeColor(Color.parseColor("#4DADFF"))//设置颜色
                    .setWrapSelectorWheel(false)//设置是否可以循环滚动
                    .setMinTime(MinTimeStamp)
//                    .setMaxTime(MaxTimeStamp)
                    .setDefaultTime(0)
                    .showFocusDateInfo(true)
                    .setOnChoose("确定", aLong -> {
                        ymdPickerStart = aLong;//起始时间戳

                        TimeInfo timeInfo = CustomUtils.LongToString(aLong);
                        ymdStart = timeInfo.getY_m_d()+timeInfo.getWeek();
                        select = timeInfo.getYmd();//20210823格式用于数据库查找，具体到某一天的事件
                        //更新UI层
                        ymdStartText.setText(ymdStart);

                        return null;
                    })
                    .build()
                    .show();
        });
        //选择开始的时和分
        hmStartText.setOnClickListener(v -> {
            new CardDatePickerDialog.Builder(NewToDoActivity.this)
                    .setTitle("选择起始日期")
                    .showBackNow(false)//设置是否显示回到当前日期
                    .setDisplayType(3,4)//设置显示的时间，这个方法传入一个int list 顺序为 年 - 月 - 日 - 时 - 分 - 秒
                    .setBackGroundModel(CardDatePickerDialog.STACK)//设置底部弹出样式
                    .setThemeColor(Color.parseColor("#4DADFF"))//设置颜色
                    .setWrapSelectorWheel(false)//设置是否可以循环滚动
                    .setMinTime(MinTimeStamp)
//                    .setMaxTime(MaxTimeStamp)
                    .setDefaultTime(0)
                    .showFocusDateInfo(true)
                    .setOnChoose("确定", aLong -> {
                        ymdPickerStart = aLong;//起始时间戳

                        TimeInfo timeInfo = CustomUtils.LongToString(aLong);
                        start = timeInfo.getHmString();//开始时间 19：00格式
                        hm = Integer.parseInt(timeInfo.getHm());
                        //更新UI层
                        hmStartText.setText(start);

                        return null;
                    })
                    .build()
                    .show();

        });

        //选择结束年月日
        ymdEndText.setOnClickListener(v -> {
            new CardDatePickerDialog.Builder(NewToDoActivity.this)
                    .setTitle("选择起始日期")
                    .showBackNow(false)//设置是否显示回到当前日期
                    .setDisplayType(0,1,2)//设置显示的时间，这个方法传入一个int list 顺序为 年 - 月 - 日 - 时 - 分 - 秒
                    .setBackGroundModel(CardDatePickerDialog.STACK)//设置底部弹出样式
                    .setThemeColor(Color.parseColor("#4DADFF"))//设置颜色
                    .setWrapSelectorWheel(false)//设置是否可以循环滚动
                    .setMinTime(ymdPickerStart)
//                    .setMaxTime(MaxTimeStamp)
                    .setDefaultTime(0)
                    .showFocusDateInfo(true)
                    .setOnChoose("确定", aLong -> {
                        TimeInfo timeInfo = CustomUtils.LongToString(aLong);
                        ymdEnd = timeInfo.getY_m_d()+timeInfo.getWeek();
                        //更新UI层
                        ymdEndText.setText(ymdEnd);

                        return null;
                    })
                    .build()
                    .show();
        });

        //结束时：分
        hmEndText.setOnClickListener(v -> {
            new CardDatePickerDialog.Builder(NewToDoActivity.this)
                    .setTitle("选择起始日期")
                    .showBackNow(false)//设置是否显示回到当前日期
                    .setDisplayType(3,4)//设置显示的时间，这个方法传入一个int list 顺序为 年 - 月 - 日 - 时 - 分 - 秒
                    .setBackGroundModel(CardDatePickerDialog.STACK)//设置底部弹出样式
                    .setThemeColor(Color.parseColor("#4DADFF"))//设置颜色
                    .setWrapSelectorWheel(false)//设置是否可以循环滚动
                    .setMinTime(ymdPickerStart)
//                    .setMaxTime(MaxTimeStamp)
                    .setDefaultTime(0)
                    .showFocusDateInfo(true)
                    .setOnChoose("确定", aLong -> {
                        TimeInfo timeInfo = CustomUtils.LongToString(aLong);

                        end = timeInfo.getHmString();//开始时间 19：00格式
                        //更新UI层
                        hmEndText.setText(end);

                        return null;
                    })
                    .build()
                    .show();
        });

        //保存按钮监听
        textView.setOnClickListener(v -> {
            WaitDialog.show("正在处理");

            //获取控件输入信息
            EditText titleEdit = findViewById(R.id.todo_title);
            EditText infoEdit = findViewById(R.id.todo_info);
            EditText localEdit = findViewById(R.id.todo_local);

            title = titleEdit.getText().toString();
            info = infoEdit.getText().toString();
            local = localEdit.getText().toString();

            if (TextUtils.isEmpty(title)){
                CustomUtils.runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TipDialog.show("标题不可为空", WaitDialog.TYPE.ERROR);
                    }
                }, 150);
            }else {
                WaitDialog.show("保存中");

                // 创建SQLiteOpenHelper子类对象
                MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(this,"UserToDoInfo",1);
                // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase  sqliteDatabase = dbHelper.getWritableDatabase();

                // 创建ContentValues对象
                ContentValues values = new ContentValues();

                id = sp.getInt("ToDoId", Integer.parseInt("1"));
                // 向该对象中插入键值对
                values.put("id", id);
                values.put("title", title);
                values.put("start",start);
                values.put("end",end);
                values.put("hm",hm);
                values.put("ymdStart",ymdStart);
                values.put("ymdEnd",ymdEnd);
                values.put("info",info);
                values.put("local",local);
                values.put("selectTime",select);
                values.put("tip",1);
                values.put("repeat",repeat);


                // 调用insert()方法将数据插入到数据库当中
                sqliteDatabase.insert("ToDoInfo", null, values);

//                 sqliteDatabase.execSQL("insert into user (id,name) values (1,'carson')");

                SharedPreferences.Editor editor = sp.edit();
                //将查询到到的金额存入sp
                editor.putInt("ToDoId", id+1);
                editor.apply();
                //关闭数据库
                sqliteDatabase.close();

                CustomUtils.runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PopTip.show("保存成功");
                    }
                }, 150);

                finish();

            }
        });
    }

}
