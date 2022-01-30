package com.service.microjc.Activity.Ykt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.service.microjc.Activity.App.uicustomviews.BaseActivity;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class pay_web_Activity extends BaseActivity {
    private TextView daoHangTitle;
    private WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_web);

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
        //webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255");
        webView.setVerticalScrollBarEnabled(false);//去除垂直滚动
        webView.getSettings().setDomStorageEnabled(true);

        //混合式加载https,http
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//https,http混合模式访问
            //保存cookie
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        }

        //访问网页
        webView.loadUrl("http://pay.scujcc.edu.cn/");
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if ( url.startsWith("weixin://")  ||  url.startsWith("alipays://")  ||  url.startsWith("mqqapi://")  )
                {
                    //打开本地App进行支付
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }else if (url.equals("https://wx.weiweixiao.net/index.php/Pct/Jfdt/index/id/VhbMf1Ok6RGAAAAWPwAVGQ/token/eCSEa39N5hGAAAAWPwAVGQ.html") || url.equals("https://wx.weiweixiao.net/index.php/Pct/Jfdt/index.html?id=VhbMf1Ok6RGAAAAWPwAVGQ&token=eCSEa39N5hGAAAAWPwAVGQ")){
                    view.loadUrl("http://wx.weiweixiao.net/index.php/Wap/Index/columns.html?token=eCSEa39N5hGAAAAWPwAVGQ&id=HDnzPsgD5xGAAAAWPwAVGQ");
                }else {
                    view.loadUrl(url);
                }

                return true;
            }
            //https://wx.weiweixiao.net/index.php/Pct/Jfdt/index.html?id=VhbMf1Ok6RGAAAAWPwAVGQ&token=eCSEa39N5hGAAAAWPwAVGQ
            //https://wx.weiweixiao.net/index.php/Pct/Jfdt/login.html?id=VhbMf1Ok6RGAAAAWPwAVGQ&token=eCSEa39N5hGAAAAWPwAVGQ

            //监听页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("test", "onPageFinished: 调用方法"+url);
                super.onPageFinished(view, url);
                if (url.equals("https://wx.weiweixiao.net/index.php/Pct/Jfdt/login/id/VhbMf1Ok6RGAAAAWPwAVGQ/token/eCSEa39N5hGAAAAWPwAVGQ.html") ){
                     //&& webView.getUrl().equals("http://wx.weiweixiao.net/index.php/Wap/Index/columns.html?token=eCSEa39N5hGAAAAWPwAVGQ&id=HDnzPsgD5xGAAAAWPwAVGQ")

                    //截屏保存到相册
                    Log.e("test", "onPageFinished: 保存调用方法");

                    CustomUtils.runDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (webView.getUrl().equals("https://wx.weiweixiao.net/index.php/Pct/Jfdt/login/id/VhbMf1Ok6RGAAAAWPwAVGQ/token/eCSEa39N5hGAAAAWPwAVGQ.html")){
                                ScreenshotUtil.saveScreenshotFromView(webView,pay_web_Activity.this);
                                Toast.makeText(pay_web_Activity.this,"保存图片成功，将打开微信",Toast.LENGTH_LONG).show();
                                //public static void gowxScan(Context context){
                                Context context = pay_web_Activity.this;
                                try {
                                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                                    intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                                    context.startActivity(intent);

                                } catch (Exception e) {
                                    Toast.makeText(context, "没有安装微信",Toast.LENGTH_LONG);
                                }
                                // }
                            }

                        }
                    }, 1000);


                }
            }

        });

        //progress监听
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                Log.e("test", "progress: 调用方法");
//                super.onProgressChanged(view, newProgress);
//                if (newProgress == 100) {
//                    //加载100%
//                    Log.d("progress", "onProgressChanged: " + "webView---100%");
//                    Toast.makeText(pay_web_Activity.this,"prohress监听加载完毕",Toast.LENGTH_SHORT).show();
//                    //截屏保存到相册
//                    ScreenshotUtil.saveScreenshotFromView(webView,pay_web_Activity.this);
//                }
//            }
//        });


        //获取网站标题,显示在导航栏
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                daoHangTitle = findViewById(R.id.navigation_title);
                if (title.equals("https://wx.weiweixiao.net/index.php/Pct/Jfdt/login/id/VhbMf1Ok6RGAAAAWPwAVGQ/token/eCSEa39N5hGAAAAWPwAVGQ.html")){
                    daoHangTitle.setText("扫码登录");

                }else if (title.equals("pay.scujcc.edu.cn")){
                    daoHangTitle.setText("锦大支付");
                } else {
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
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && !webView.getUrl().equals("http://wx.weiweixiao.net/index.php/Wap/Index/columns.html?token=eCSEa39N5hGAAAAWPwAVGQ&id=HDnzPsgD5xGAAAAWPwAVGQ")) {
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

    /**
     * Author : Ziwen Lan
     * Date : 2019/10/23
     * Time : 15:11
     * Introduction : 截屏工具类
     */
    public static class ScreenshotUtil {

        /**
         * 截取指定activity显示内容
         * 需要读写权限
         */
        public static void saveScreenshotFromActivity(Activity activity) {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            saveImageToGallery(bitmap, activity);
            //回收资源
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }

        /**
         * 截取指定View显示内容
         * 需要读写权限
         */
        public static void saveScreenshotFromView(View view, Activity context) {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            saveImageToGallery(bitmap, context);
            //回收资源
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }

        /**
         * 保存图片至相册
         * 需要读写权限
         */
        private static void saveImageToGallery(Bitmap bmp, Activity context) {
            File appDir = new File(getDCIM());
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + getDCIM())));
        }

        /**
         * 获取相册路径
         */
        private static String getDCIM() {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return "";
            }
            String path = Environment.getExternalStorageDirectory().getPath() + "/dcim/";
            if (new File(path).exists()) {
                return path;
            }
            path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
            File file = new File(path);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return "";
                }
            }
            Log.e("DCIM", "getDCIM: "+path);
            return path;

        }
    }




}
