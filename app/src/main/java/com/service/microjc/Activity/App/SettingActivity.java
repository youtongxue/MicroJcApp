package com.service.microjc.Activity.App;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.kyleduo.switchbutton.SwitchButton;
import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.R;

public class SettingActivity extends BaseActivity {
    private SwitchButton switchButton;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeting);

        switchButton = findViewById(R.id.button_set_saveuserinfo);
        sp = getSharedPreferences("SwitchButton", Context.MODE_PRIVATE);

        SetSwitchButton();
    }

    @Override
    protected void onDestroy() {
        SaveSwitchButton();
        super.onDestroy();
    }

    /**
     * 需要更具本地sp中信息判断那些按钮为true
     * */
    private void SetSwitchButton(){
        Boolean saveUserInfo = sp.getBoolean("SaveUserInfo",false);
        if (saveUserInfo){
            switchButton.setChecked(true);
        }

    }

    /**
     * 保存switchButton状态
     * */
    private void SaveSwitchButton(){
        SharedPreferences.Editor editor = sp.edit();

        if (switchButton.isChecked()){
            editor.putBoolean("SaveUserInfo",true);
        }else {
            editor.putBoolean("SaveUserInfo",false);
        }
        editor.apply();

    }

}

