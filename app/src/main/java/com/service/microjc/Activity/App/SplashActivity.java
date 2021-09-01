package com.service.microjc.Activity.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //隐藏状态栏和底部导航栏
            ImmersionBar.with(this)
                    .hideBar(BarHide.FLAG_HIDE_BAR)
                    .init();
            Intent lIntent = new Intent(this,MainActivity.class);
            startActivity(lIntent);
            finish();
        }


}