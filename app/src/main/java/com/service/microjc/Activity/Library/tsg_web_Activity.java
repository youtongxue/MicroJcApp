package com.service.microjc.Activity.Library;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.service.microjc.Activity.App.MainActivity;
import com.service.microjc.R;
import com.gyf.immersionbar.ImmersionBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class tsg_web_Activity extends AppCompatActivity {
    private TextView daoHangTitle;
    private WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsg_web);
        SetMargin();
        GoBack();


        ImmersionBar.with(tsg_web_Activity.this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();

        //隐藏action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        //获得控件
        webView = findViewById(R.id.wv_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许JS交互
        //        webSettings.setDomStorageEnabled(true);
        //访问网页
        webView.loadUrl("http://weixintsg.scujcc.cn/mobile/mobile");
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }


        });

        //获取网站标题,显示在导航栏
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                daoHangTitle = findViewById(R.id.tsg_title_text);
                daoHangTitle.setText(title);
            }

        });

//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                // 页面加载完成
//                hideBottom(); // 执行隐藏底部栏方法
//            }
//        });




    }

    /**
     * 获取状态栏高度，设置layout的margin——top值
     */
    public void SetMargin() {
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


        RelativeLayout titleLayout1 = findViewById(R.id.titleRelative_tsg);
        titleLayout1.setLayoutParams(lp);
    }

    /**
     * web方法
     */
    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * WebView销毁，不然存在内存泄漏可能
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroyWebView();
    }

    public void destroyWebView() {

        webView.removeAllViews();

        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView.pauseTimers();
            webView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

    /**
     * 设置返回监听
     */

    public void GoBack() {
        ImageView backIcon = findViewById(R.id.fanhui_tsg);
        backIcon.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                Intent intent = new Intent();
                intent.setClass(tsg_web_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });


    }

    /**
     * 隐藏底部栏方法
     */
    private void hideBottom() {
        try {
            //定义javaScript方法
            String javascript = "javascript:function hideBottom() { "
                    + "document.getElementsByClassName('jp-logo')[0].style.display='none'"
                    +"}";
//            document.getElementsByClassName('jp-nav-bottom')[0].style.display='none';

            //加载方法
            webView.loadUrl(javascript);
            //执行方法
            webView.loadUrl("javascript:hideBottom();");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
