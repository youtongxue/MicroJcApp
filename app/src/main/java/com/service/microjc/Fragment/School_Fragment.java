package com.service.microjc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.service.microjc.Activity.Jw.JwLoginActivity;
import com.service.microjc.Activity.Jw.JwUserInfoActivity;
import com.service.microjc.Activity.Library.LibraryLoginActivity;
import com.service.microjc.Activity.Library.LibraryUserInfoActivity;
import com.service.microjc.Activity.Ykt.YktLoginActivity;
import com.service.microjc.Activity.Library.tsg_web_Activity;
import com.service.microjc.Activity.Ykt.YktUserInfoActivity;
import com.service.microjc.Activity.Ykt.pay_web_Activity;
import com.service.microjc.InterFace.YktApi;
import com.service.microjc.NetworkFactory;
import com.service.microjc.R;
import com.service.microjc.stType.YktUserInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.style.IOSStyle;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class School_Fragment extends ImmersionFragment {
    private View view;

    private SharedPreferences sp;
    private Boolean savePass;

    private YktUserInfo info;//??????????????????????????????????????????
    private ImageView touxiang;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.school_fragment,null);

        touxiang  = view.findViewById(R.id.touxiang);

        //?????????
        DialogX.init(getContext());
        //?????????IOS??????
        DialogX.globalStyle = new IOSStyle();
        DialogX.onlyOnePopTip = true;

        setLinearHeight();
        scroll();
        SetMargin();
        to_LibraryLoginActivity();
        setUserImg();

        return view;
    }

    //?????? oNStart??????
    @Override
    public void onStart() {
        Log.e("test", "onStart");
        super.onStart();
        setMoney();
        setBooks();
        setUserImg();
    }

    /**
     * ??? Layout???????????????????????????
     * */
    public void to_LibraryLoginActivity(){
        //?????? ???LinearLayout
        LinearLayout jwwLayout = view.findViewById(R.id.jww_Layout);
        LinearLayout tsgLayout = view.findViewById(R.id.tsg_Layout);
        LinearLayout yktLayout = view.findViewById(R.id.ykt_Layout);
        LinearLayout sdfLayout = view.findViewById(R.id.sdf_Layout);
        LinearLayout wmLayout = view.findViewById(R.id.wm_Layout);
        LinearLayout swzlLayout = view.findViewById(R.id.swzl_Layout);

        //Card
        View yktCard = view.findViewById(R.id.school_ykt_card_info);//card??????
        View libCard = view.findViewById(R.id.school_lib_card_info);//card??????

        swzlLayout.setOnClickListener(v -> {
            //public static void gowxScan(Context context){
            Context context = getContext();
            try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                    context.startActivity(intent);

                } catch (Exception e) {
//                    Toast.makeText(context, "??????????????????",);
                }
           // }


        });
        //??????
        sdfLayout.setOnClickListener(v ->{


        });

        tsgLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("LibraryUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), LibraryLoginActivity.class);
            }else {
                intent.setClass(getActivity(), LibraryUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        yktLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("YktUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), YktLoginActivity.class);
            }else {
                intent.setClass(getActivity(), YktUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        wmLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), tsg_web_Activity.class);
            startActivity(intent);
        });

        jwwLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("JwUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), JwLoginActivity.class);
            }else {
                intent.setClass(getActivity(), JwUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        yktCard.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("YktUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), YktLoginActivity.class);
            }else {
                intent.setClass(getActivity(), YktUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

        libCard.setOnClickListener(v -> {
            Intent intent = new Intent();
            sp = getActivity().getSharedPreferences("LibraryUserLoginInfo",Context.MODE_PRIVATE);
            if (sp.getString("PASSWORD","").isEmpty()){
                intent.setClass(getActivity(), LibraryLoginActivity.class);
            }else {
                intent.setClass(getActivity(), LibraryUserInfoActivity.class);
                intent.putExtra("from","SchoolFragment");
            }
            startActivity(intent);
        });

    }

    /**
     * ??????????????????????????????layout???margin??????top???
     *
     * */
    public void SetMargin(){
        //?????????????????????
        int statusBarHeight1 = 0;
        //??????status_bar_height?????????ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //????????????ID????????????????????????
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("TAG", "??????1???????????????:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + statusBarHeight1);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(0, statusBarHeight1, 0, 0);

        RelativeLayout titleLayout = view.findViewById(R.id.titleRelative_school);
        titleLayout.setLayoutParams(lp);

    }

    /***
     * ????????????????????????
     */
//    public void auto_login(String username, String password){
//        //??????????????????
//        //??????????????????????????? api
//        YktApi api = NetworkFactory.YktApi();
//        Call<Object> Y = api.getYktUserInfo(username, password);
//        Y.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
//                info = (YktUserInfo) response.body();//???????????????userinfo??????????????????????????????body???????????????
////                Log.d("????????????????????????", "???????????????????????????????????????????????????"+info.getUserName());
//                //????????????
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), YktUserInfoActivity.class);
//                //??????intent??????
//                //?????????  username???password?????????SharedPreferences??????????????????????????????????????????????????????????????????
//                intent.putExtra("username",sp.getString("USERNAME", ""));
//                intent.putExtra("password",sp.getString("PASSWORD", ""));
//                intent.putExtra("userinfo", info);
//
//                startActivity(intent);
//                //??????finish?????????
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
//
//
//            }
//        });
//
//    }

    /**
     * ??????????????????
     * */
    private void scroll(){
        RefreshLayout refreshLayout = view.findViewById(R.id.school_refreshLayout);

        refreshLayout.setEnablePureScrollMode(true);
//        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
    }

    /**
     * ??????linearlayout???????????????????????????
     * */
    private void setLinearHeight(){
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // ??????????????????????????????
            display.getRealSize(outPoint);
        } else {
            // ????????????????????????
            display.getSize(outPoint);
        }
        int mRealSizeWidth;//????????????????????????
        mRealSizeWidth = outPoint.x;

        Log.e("test", "setLinearWidth: "+mRealSizeWidth );
        /*
         * ??????????????????????????? px(??????) ????????? ????????? dp
         */

        float scale = getContext().getResources().getDisplayMetrics().density;
        int width = (int) (mRealSizeWidth / scale + 0.5f);

        //??????linear???
        LinearLayout schoolCardLayout = view.findViewById(R.id.school_card_linear);
        ViewGroup.LayoutParams lp;
        lp = schoolCardLayout.getLayoutParams();
        lp.height = width+width/5;
        schoolCardLayout.setLayoutParams(lp);

    }

    /**
     * ????????????card??????
     * */

    private void setMoney(){
        sp = getActivity().getSharedPreferences("YktUserInfo", Context.MODE_PRIVATE);
        String newMoney = sp.getString("newMoney","");
        String lastTime = sp.getString("lastTime","");//????????????
        Log.e("test", "sp?????????????????????????????????????????????"+newMoney );
        TextView moneyText = view.findViewById(R.id.moneyText);
        TextView yktCardTime = view.findViewById(R.id.yktCardTime);
        moneyText.setText(newMoney);
        yktCardTime.setText(lastTime);

        Log.e("test", "setMoney: >>>>>>>>>>?????????" );
    }

    private void setBooks(){
        sp = getActivity().getSharedPreferences("LibUserInfo",Context.MODE_PRIVATE);
        String newBooks = sp.getString("newBooks","");
        String lastTime = sp.getString("lastTime","");
        TextView booksText = view.findViewById(R.id.booksText);
        TextView tsgCardTime = view.findViewById(R.id.lib_card_time);
        booksText.setText(newBooks);
        tsgCardTime.setText(lastTime);

    }


    /**
     * ?????????,????????? extends ImmersionFragment????????? initImmersionBar???????????????fragment?????????????????????????????????
     * */
    @Override
    public void initImmersionBar() {
        //?????????????????????
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    public void setUserImg(){
        sp = getActivity().getSharedPreferences("UserLoginInfo",Context.MODE_PRIVATE);
        String Url = sp.getString("UserImageUrl","null");
        if (Url.equals("null") & !sp.getBoolean("loginState",false)){
            Toast.makeText(getContext(),"????????????????????????",Toast.LENGTH_LONG);
            Drawable drawable = getActivity().getDrawable(R.drawable.touxiang);
            touxiang.setImageDrawable(drawable);
        }else {
            //????????????
            RoundedCorners roundedCorners = new RoundedCorners(200);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(getContext()).load(Url).apply(options).into(touxiang);
        }
    }
}
