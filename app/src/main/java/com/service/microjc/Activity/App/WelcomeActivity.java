//package com.service.microjc.Activity.App;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//
//import com.service.microjc.R;
//import com.gyf.immersionbar.BarHide;
//import com.gyf.immersionbar.ImmersionBar;
//
//public class WelcomeActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //隐藏action bar
////        ActionBar actionBar = getSupportActionBar();
//////        assert actionBar != null;
////        actionBar.hide();
//        //隐藏状态栏和底部导航栏
//        ImmersionBar.with(this)
//                .hideBar(BarHide.FLAG_HIDE_BAR)
//                .init();
//
//
////        setContentView(R.layout.activity_welcome);
//        View view = View.inflate(this, R.layout.activity_splash,null);
//        setContentView(view);
//        AlphaAnimation a = new AlphaAnimation(0.6f,1.0f);//渐变动画
//        a.setDuration(5000);//持续时间
//        view.startAnimation(a);
//        a.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Intent intent =new Intent();
//                intent.setClass(WelcomeActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//}