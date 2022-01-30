package com.service.microjc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.service.microjc.Activity.ToDo.MySQLiteOpenHelper;
import com.service.microjc.Activity.ToDo.NewToDoActivity;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.R;
import com.service.microjc.stType.TimeInfo;
import com.service.microjc.stType.ToDoInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.style.IOSStyle;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.painter.CalendarBackground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class RiCheng_Fragment extends ImmersionFragment {
    private static final String TAG = "生命周期被调用>>>>>>>>>>>>>>>";
    private Miui10Calendar miui10Calendar;
    private String selectTime;//选择时间
    private View view = null;
    private int itemSum;//一共有多少条事件
    private ListView listView;
    private long MaxTimeStamp = System.currentTimeMillis();//当前long格式时间戳

    private ArrayList<ToDoInfo> todoList = new ArrayList<>();
    private ToDoInfoListAdapter listAdapter;

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.richeng_fragment,null);

        //初始化
        DialogX.init(getContext());
        //设置为IOS主题
        DialogX.globalStyle = new IOSStyle();

        setCalendarBackGround();
        SetMargin();
        toNewToDoActivity();
        setTodoListView();
        pressedListItem();

        return view;
    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
        refreshToday();

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

        RelativeLayout titleLayout = view.findViewById(R.id.titleRelative_richeng);
        titleLayout.setLayoutParams(lp);
    }

    /**
     * 设置日历背景
     * */
    private void setCalendarBackGround(){
        Miui10Calendar calendar;
        calendar = view.findViewById(R.id.miui10Calendar);

        //折叠后背景颜色
        Drawable drawable = getResources().getDrawable(R.drawable.ic_canldenrbackground);
        calendar.setWeekCalendarBackground(new CalendarBackground() {
            @Override
            public Drawable getBackgroundDrawable(LocalDate localDate, int currentDistance, int totalDistance) {
                return drawable;
            }
        });
        calendar.setMonthCalendarBackground(new CalendarBackground() {
            @Override
            public Drawable getBackgroundDrawable(LocalDate localDate, int currentDistance, int totalDistance) {
                return drawable;
            }
        });
    }

//    /**
//     * 设置list view
//     * */
//    private void todoListView(){
//
//        todoListView = view.findViewById(R.id.todolist);
//
//        List<Map<String, Object>> mListItems = new ArrayList<>();
//        for (int i = 0; i < todoTitle.length; i++) {
//            Map<String, Object> mMap = new HashMap<>();
//            mMap.put("title", todoTitle[i]);
//            mListItems.add(mMap);
//        }
//
//        SimpleAdapter mAdapter = new SimpleAdapter(getContext(), mListItems, R.layout.todo_item, new String[]{"title"}, new int[]{R.id.todotitle});
//        todoListView.setAdapter(mAdapter);
//
//        //设置点击监听事件
//        todoListView.setOnItemClickListener((adapterView, view, i, l) -> {
//
//            switch (i){
//                case 0:
//                    PopTip.show("点击了事件1");
//
//                    break;
//                case 1:
//
//                    PopTip.show("点击了事件2");
//
//                    break;
//                case 2:
//
//                    PopTip.show("点击了事件3");
//
//
//                    break;
//                case 3:
//                    PopTip.show("点击了事件4");
//                    break;
//            }
//
//        });
//    }

    /**
     * 状态栏,继承自 extends ImmersionFragment，重写 initImmersionBar，即可不走fragment生命周期改变状态栏颜色
     * */
    @Override
    public void initImmersionBar() {
        //设置状态栏颜色
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    /**
     * 跳转到新建日程
     * */
    private void toNewToDoActivity(){
        ImageView addIcon = view.findViewById(R.id.addToDo);
        addIcon.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), NewToDoActivity.class);
            startActivity(intent);

        });
    }

    /**
     * 查询数据库设置，list view
     * */
    private void setTodoListView(){


        miui10Calendar = view.findViewById(R.id.miui10Calendar);

        //日历标点
//        InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
//        List<String> defaultCustomWheelSelect = new ArrayList<>();
//        defaultCustomWheelSelect.add("2021-11-11");
//        innerPainter.setPointList(defaultCustomWheelSelect);

        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                //每次查询清空todoList集合
                if (todoList.size() != 0){
                    todoList.clear();
                }

                TextView time = view.findViewById(R.id.time_now);
                time.setText(localDate.toString("M月d日"));//设置导航栏时间

                selectTime = localDate.toString("yyyyMMdd");//20210824格式，用于数据库查询当日事件
                Log.e("test", "onCalendarChange: "+selectTime );

                //数据库查询操作
                // 创建SQLiteOpenHelper子类对象
                MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(getContext(),"UserToDoInfo",1);
                // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

                String sql = "SELECT _id, username, password FROM ToDoInfo";
                // 通过游标遍历名为ToDoInfo的表
                Cursor result = sqliteDatabase.query("ToDoInfo",new String[] { "id","title","info","local","ymdStart","start","ymdEnd","end","hm","tip","repeat","selectTime" },"selectTime=?",new String[]{selectTime},null,null,"hm desc");
                itemSum = result.getCount();//获取查询到的项数
                setCardHeight(itemSum);//设置list view高度

                Log.e("test", "itemSum: "+itemSum);

                result.moveToFirst();//游标移动到第一项
                while (!result.isAfterLast()) {

                    ToDoInfo toDoInfo = new ToDoInfo();
                    int id = result.getInt(0);
                    String title = result.getString(1);
                    String info = result.getString(2);
                    String local = result.getString(3);
                    String ymdStart = result.getString(4);
                    String start = result.getString(5);
                    String ymdEnd = result.getString(6);
                    String end = result.getString(7);
                    int hm = result.getInt(8);
                    int tip = result.getInt(9);
                    String repeat = result.getString(10);
                    String select = result.getString(11);
                    // do something useful with these

                    toDoInfo.setId(id);
                    toDoInfo.setTitle(title);
                    toDoInfo.setInfo(info);
                    toDoInfo.setLocal(local);
                    toDoInfo.setYmdStart(ymdStart);
                    toDoInfo.setStart(start);
                    toDoInfo.setYmdEnd(ymdEnd);
                    toDoInfo.setEnd(end);
                    toDoInfo.setHm(hm);
                    toDoInfo.setTip(tip);
                    toDoInfo.setRepeat(repeat);
                    toDoInfo.setSelectTime(select);

                    todoList.add(toDoInfo);

                    result.moveToNext();
                }
                result.close();

                //设置adapter
                listAdapter = new ToDoInfoListAdapter();
                listView = view.findViewById(R.id.todolist);
                listView.setAdapter(listAdapter);


                Log.e("test", "集合长度为"+todoList.size() );

            }


        });



    }

    /**
     * ToDoListView适配器
     * */
    public class ToDoInfoListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView= LayoutInflater.from(getActivity()).inflate(R.layout.todo_item,null,false);
                viewHolder = new ViewHolder();
                viewHolder.start=convertView.findViewById(R.id.todo_start);
                viewHolder.end=convertView.findViewById(R.id.todo_end);
                viewHolder.title=convertView.findViewById(R.id.todo_title);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.start.setText(todoList.get(getCount()-position-1).getStart());
            viewHolder.end.setText(todoList.get(getCount()-position-1).getEnd());
            viewHolder.title.setText(todoList.get(getCount()-position-1).getTitle());


            return convertView;
        }
        public class ViewHolder{
            TextView start;
            TextView end;
            TextView title;

        }
    }

    /**
     * 设置card的长度
     * */
    private void setCardHeight(int itemSum){
        int px = dip2px(getContext(),80);

        listView = view.findViewById(R.id.todolist);
        ViewGroup.LayoutParams params = listView.getLayoutParams();

//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
//                + listView.getListPaddingBottom() + listView.getListPaddingTop();
        params.height = itemSum*px;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // listView.getListPaddingBottom()获取ListView的内边距
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);

    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 添加日历切换回来的刷新当日行程
     * */
    private void refreshToday(){
        TimeInfo timeInfo = CustomUtils.LongToString(MaxTimeStamp);
        selectTime = timeInfo.getYmd();

        //每次查询清空todoList集合
        if (todoList.size() != 0){
            todoList.clear();
        }

        //数据库查询操作
        // 创建SQLiteOpenHelper子类对象
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(getContext(),"UserToDoInfo",1);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

        // 通过游标遍历名为ToDoInfo的表
        Cursor result = sqliteDatabase.query("ToDoInfo",new String[] { "id","title","info","local","ymdStart","start","ymdEnd","end","hm","tip","repeat","selectTime" },"selectTime=?",new String[]{selectTime},null,null,"hm desc");
        itemSum = result.getCount();//获取查询到的项数
        setCardHeight(itemSum);//设置list view高度

        Log.e("test", "itemSum: "+itemSum);

        result.moveToFirst();//游标移动到第一项
        while (!result.isAfterLast()) {

            ToDoInfo toDoInfo = new ToDoInfo();
            int id = result.getInt(0);
            String title = result.getString(1);
            String info = result.getString(2);
            String local = result.getString(3);
            String ymdStart = result.getString(4);
            String start = result.getString(5);
            String ymdEnd = result.getString(6);
            String end = result.getString(7);
            int hm = result.getInt(8);
            int tip = result.getInt(9);
            String repeat = result.getString(10);
            String select = result.getString(11);
            // do something useful with these

            toDoInfo.setId(id);
            toDoInfo.setTitle(title);
            toDoInfo.setInfo(info);
            toDoInfo.setLocal(local);
            toDoInfo.setYmdStart(ymdStart);
            toDoInfo.setStart(start);
            toDoInfo.setYmdEnd(ymdEnd);
            toDoInfo.setEnd(end);
            toDoInfo.setHm(hm);
            toDoInfo.setTip(tip);
            toDoInfo.setRepeat(repeat);
            toDoInfo.setSelectTime(select);

            todoList.add(toDoInfo);

            result.moveToNext();
        }
        result.close();

        //设置adapter
        listAdapter = new ToDoInfoListAdapter();
        listView = view.findViewById(R.id.todolist);
        listView.setAdapter(listAdapter);

    }

    /**
     * listview点击事件
     * */
    private void pressedListItem(){
        ListView todoListView = view.findViewById(R.id.todolist);
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //尝试获取点击item内容
                //这儿的position是item在list view中的位置，在todoList集合信息中的位置是倒叙的
                int todoId = todoList.get(itemSum-position-1).getId();
                String title = todoList.get(itemSum-position-1).getTitle();

                PopTip.show("事件标题为："+title+"数据库中ID："+todoId);

                Log.e(TAG, "数据库中ID："+todoId);
                Log.e(TAG, "事件标题为："+title);


            }
        });


    }

}
