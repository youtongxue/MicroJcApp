package com.service.microjc.Activity.App.uicustomviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.service.microjc.R;

public class TitleLayout extends LinearLayout {


    public TitleLayout(Context context) {
        super(context);
        //init(context,null);
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.title, this);
        ImageView titleBack = (ImageView) findViewById(R.id.navigation_back);
        TextView titleText = (TextView) findViewById(R.id.navigation_title);
        LinearLayout nav = (LinearLayout) findViewById(R.id.nav);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout);

        //设置图标返回
        titleBack.setOnClickListener(v -> {
            ((Activity) getContext()).finish();
        });

        //设置导航栏标题
        titleText.setText(typedArray.getString(R.styleable.TitleLayout_titleName));
        //设置导航栏背景色
        String navBackground = typedArray.getString(R.styleable.TitleLayout_navBackground);
        if (!navBackground.isEmpty()){
            Log.e("TAG", "TitleLayout: 拿到颜色属性为：》》》》》》》"+navBackground );
            nav.setBackgroundColor(Color.parseColor(navBackground));
        }
        SetMargin();
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init(context,attrs);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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


        RelativeLayout titleLayout1 = findViewById(R.id.TitleRel);
        titleLayout1.setLayoutParams(lp);
    }

}
