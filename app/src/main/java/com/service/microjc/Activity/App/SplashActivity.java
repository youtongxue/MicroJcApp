package com.service.microjc.Activity.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.service.microjc.R;

public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //隐藏状态栏和底部导航栏
            ImmersionBar.with(this)
                    .hideBar(BarHide.FLAG_HIDE_BAR)
                    .init();

            View view = View.inflate(this, R.layout.activity_splash,null);
            setContentView(view);
            AlphaAnimation a = new AlphaAnimation(0.1f,1.0f);//渐变动画
            a.setDuration(150);//持续时间
            view.startAnimation(a);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent lIntent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(lIntent);
                    overridePendingTransition(0, 0);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }


}