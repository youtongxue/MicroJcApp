package com.service.microjc.Activity.Library;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.service.microjc.Activity.App.MainActivity;
import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.R;
import com.gyf.immersionbar.ImmersionBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class tsg_web_Activity extends BaseActivity {
    private TextView daoHangTitle;
    private WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsg_web);

        //获得控件
        webView = findViewById(R.id.wv_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许JS交互
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setAllowFileAccess(true);//访问文件
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255");
        webView.setVerticalScrollBarEnabled(false);//去除垂直滚动
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        // 修改ua使得web端正确判断
        String ua = webView.getSettings().getUserAgentString();
        Log.e("ua>>>>>>>", "原始UA》》》》》"+ua);
        webView.getSettings().setUserAgentString(ua+"; 自定义标记");
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255");

        //混合式加载https,http
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//https,http混合模式访问
            //保存cookie
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        }

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
                daoHangTitle = findViewById(R.id.navigation_title);
                if (title.equals("weixintsg.scujcc.cn/mobile/mobile/user")){
                    daoHangTitle.setText("个人中心");
                }else {
                    daoHangTitle.setText(title);
                }

            }

        });



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
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
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
