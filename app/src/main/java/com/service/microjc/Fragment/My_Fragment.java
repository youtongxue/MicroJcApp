package com.service.microjc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.service.microjc.Activity.App.LoginActivity;
import com.service.microjc.Activity.App.SettingActivity;
import com.service.microjc.Activity.App.Utils.CustomUtils;
import com.service.microjc.Activity.App.Utils.DownloadUtils;
import com.service.microjc.InterFace.CheckUpData;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.AppInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class  My_Fragment extends ImmersionFragment {
    private View view;
    //listview
    public ListView mListView = null;

    /* 图片ID数组 */
    private final int[] mImageId = new int[] {R.drawable.ic_user_login, R.drawable.ic_user_updata, R.drawable.ic_user_login, R.drawable.ic_user_setting };
    /* 文字列表数组 */
    private final String[] mTitle = new String[] {"反馈与建议", "检测升级", "登录账号", "设置"};

    public static Tencent mTencent;
    private UserInfo mInfo = null;
    private SharedPreferences sp;
    private ImageView userImg;
    private TextView nickNameText;
    private ImageView userXbImg;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.my_fragment,null);

        userImg = view.findViewById(R.id.userImage);
        nickNameText = view.findViewById(R.id.username);
        userXbImg = view.findViewById(R.id.xbicon_img);
        init();

        return view;
    }

    //重写 oNStart方法
    @Override
    public void onStart() {
        Log.e("test", "onStart");
        super.onStart();
        setUserImg();
    }

    /**
     * 设置list view
     * */

    public void init() {

        mListView = view.findViewById(R.id.listview);

        List<Map<String, Object>> mListItems = new ArrayList<>();
        for (int i = 0; i < mImageId.length; i++) {
            Map<String, Object> mMap = new HashMap<>();
            mMap.put("image", mImageId[i]);
            mMap.put("title", mTitle[i]);
            mListItems.add(mMap);
        }

        SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), mListItems, R.layout.myfragment_listview_item, new String[]{"title", "image"}, new int[]{R.id.textview, R.id.imageview});
        mListView.setAdapter(mAdapter);

        //设置点击监听事件
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {

            switch (i) {
                case 0:
                    //点击第一个拉起QQ群
                    joinQQGroup("BlnWumRzYHPzfnnw0ypeAsxGWkbJVMP4");
                    break;
                case 1:
                    CheckUpData();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    Intent intent = new Intent();
                    intent.setClass(getContext(), SettingActivity.class);
                    startActivity(intent);
            }

//                Toast.makeText(getContext(), "Click item：" + i, Toast.LENGTH_SHORT).show();


        });
    }

    /****************
     *
     * 发起添加群流程。群号：锦城微服务App反馈(95547598) 的 key 为： BlnWumRzYHPzfnnw0ypeAsxGWkbJVMP4
     * 调用 joinQQGroup(BlnWumRzYHPzfnnw0ypeAsxGWkbJVMP4) 即可发起手Q客户端申请加群 锦城微服务App反馈(95547598)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 检测软件升级
     * */
    public void CheckUpData(){
        //发起网络访问
        //实例化一个请求对象 api
        CheckUpData checkUpData = NetworkFactory.checkUpData();
        Call<AppInfo> Y = checkUpData.getVersion();
        Y.enqueue(new Callback<AppInfo>() {
            @Override
            public void onResponse(@NotNull Call<AppInfo> call, @NotNull Response<AppInfo> response) {
                AppInfo appInfo = response.body();
                assert appInfo != null;
                float getCode = appInfo.getVersion();
                float appCode = Float.parseFloat(CustomUtils.getVersion(getContext()));

                //如果服务器版本号，大于本地版本号
                if (getCode > appCode) {

                    MessageDialog.build()
                            .setStyle(IOSStyle.style())
                            .setTheme(DialogX.THEME.AUTO)
                            .setTitle("检测到新版本")
                            .setMessage(appInfo.getUpDataContent())
                            .setOkButton("下载")
                            .setCancelable(true)
                            .setBackgroundColor(Color.parseColor("#FFFFFF"))
                            .setOkButton((baseDialog, v) -> {

                                DownloadUtils downloadUtils = new DownloadUtils(getContext());
                                String url = "http://1.14.68.248/app/We锦大"+getCode+".apk";
                                String name = "We锦大"+getCode+".apk";
                                downloadUtils.downloadAPK(url,name,name);

                                return false;
                            })
                            .show();
                }else if (getCode == appCode){
                    MessageDialog.build()
                            .setStyle(IOSStyle.style())
                            .setTheme(DialogX.THEME.AUTO)
                            .setTitle("当前版本已是最新版本")
//                            .setMessage(appInfo.getUpDataContent())
                            .setOkButton("确定")
                            .setCancelable(true)
                            .setBackgroundColor(Color.parseColor("#FFFFFF"))
                            .show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<AppInfo> call, @NotNull Throwable t) {
                MessageDialog.build()
                        .setStyle(IOSStyle.style())
                        .setTheme(DialogX.THEME.AUTO)
                        .setTitle("似乎网络出了点问题")
//                            .setMessage(appInfo.getUpDataContent())
                        .setOkButton("确定")
                        .setCancelable(true)
                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
                        .show();

            }

        });

    }

    @Override
    public void initImmersionBar() {
        //设置状态栏颜色
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色
                .init();
    }


    //QQ登录测试
    public void login(){
        Intent intent = new Intent();
        intent.setClass(getContext(),LoginActivity.class);
        startActivity(intent);
    }
    public void setUserImg() {
        sp = getActivity().getSharedPreferences("UserLoginInfo",Context.MODE_PRIVATE);
        String Url = sp.getString("UserImageUrl","null");
        String nickName = sp.getString("nickName","null");
        String userXb = sp.getString("userXb","null");
        Log.e("TAG", "userxb>>>>>>>>>>>>>>>>>>>: "+userXb);
        if (Url.equals("null") & !sp.getBoolean("loginState",false)){
            Toast.makeText(getContext(),"获取用户相信失败",Toast.LENGTH_LONG).show();
            Drawable drawable = getActivity().getDrawable(R.drawable.touxiang);
            Drawable drawable1 = getActivity().getDrawable(R.drawable.ic_xbicon);
            userImg.setImageDrawable(drawable);
            userXbImg.setImageDrawable(drawable1);
            nickNameText.setText("用户昵称");
        }else {
            Log.e("用户头像链接： ",">>>>>>>>>"+Url);
            //设置头像
            RoundedCorners roundedCorners = new RoundedCorners(200);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(getContext()).load(Url).apply(options).into(userImg);
            //设置昵称
            nickNameText.setText(nickName);
            //设置性别图片
            Drawable drawable;
            if (userXb.equals("男")){
                drawable = getActivity().getDrawable(R.drawable.ic_xbicon);
                userXbImg.setImageDrawable(drawable);
            }else if (userXb.equals("女")){
                drawable = getActivity().getDrawable(R.drawable.ic_xb0);
                userXbImg.setImageDrawable(drawable);
            }else {
                drawable = getActivity().getDrawable(R.drawable.ic_about);
                userXbImg.setImageDrawable(drawable);
            }



        }
    }

}
