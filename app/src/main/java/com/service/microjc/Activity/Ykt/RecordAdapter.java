package com.service.microjc.Activity.Ykt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.service.microjc.R;
import com.service.microjc.stType.RecordsBean;

import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyHolder> {
    private static final String TAG = "MAIN";
    private final Context context;
    private final List<RecordsBean.recordsInfosBean> data;
    //构造中传入上下文和带有数据的集合
    public RecordAdapter(Context context, List<RecordsBean.recordsInfosBean> data) {
        this.context = context;
        this.data = data;
        Log.d(TAG, "333333初始集合的长度为>>>>>>>>：" + data.size());
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //这个方法主要是找的我们刚刚所写的item布局
        View inflate = LayoutInflater.from(context).inflate(R.layout.record_item, viewGroup, false);
        return new MyHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String spentMoney;
        //将集合中的具体数据拿到相应的item项中展示
        String url;
//        Picasso.with(context).load(data.get(i).getShopImage()).into(myHolder.im);
        //Glide加载图片

        Glide.with(context).load(data.get(i).getShopImage()).into(myHolder.im);
        Log.e(TAG, "onBindViewHolder:  店铺图片 》》》》 "+(data.get(i).getShopImage())) ;


        //判断商户名是否为空，如果为空则返回机器编号
        Log.e(TAG, "onBindViewHolder: >>>>>>>>>>>>>>>>>>"+data.get(i).getShop() );
        if (data.get(i).getShop() == null){
            myHolder.shop.setText(data.get(i).getMachine());
        }else {
            myHolder.shop.setText(data.get(i).getShop());
        }

        //如果是 微信充值则为➕，其他则为➖
        if (data.get(i).getType().equals("微信充值")){
            spentMoney = "＋"+data.get(i).getSpentMoney();
            myHolder.spent.setTextColor(Color.parseColor("#45BB35"));
            myHolder.spent.setText(spentMoney);
            myHolder.time.setText(data.get(i).getTime());
        }else if (data.get(i).getType().equals("支付宝充值")){
            spentMoney = "＋"+data.get(i).getSpentMoney();
            myHolder.spent.setTextColor(Color.parseColor("#119DEE"));
            myHolder.spent.setText(spentMoney);
            myHolder.time.setText(data.get(i).getTime());
        } else {
            spentMoney = "－"+data.get(i).getSpentMoney();
            myHolder.spent.setText(spentMoney);
            myHolder.time.setText(data.get(i).getTime());
            Log.d(TAG, "data长度为>>>>>>>>>>>>>>>>>>>>：" + data.size());
        }

    }
    @Override
    public int getItemCount() {
        //集合的长度
        Log.d(TAG, "集合的长度为：" + data.size());
        return data.size()/2;
    }
    public void refresh(List<RecordsBean.recordsInfosBean> arrayList){
        //这个方法是我们自己手写的，主要是对适配器的一个刷新
        Log.d(TAG, "传入List集合的长度为>>>>>>>>>>>>>>>>>>>>>>：" + arrayList.size());

        this.data.addAll(arrayList);
        notifyDataSetChanged();
    }
    static class MyHolder extends RecyclerView.ViewHolder {
        //ViewHolder的作用主要是 性能的优化，在每个子item项中的子控件都是一样的情况下，达到控件的复用从而达到节约系统资源的目的
        ImageView im;
        TextView shop;
        TextView spent;
        TextView time;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.image_shop);
            shop=itemView.findViewById(R.id.text_shop);
            spent=itemView.findViewById(R.id.ykt_status);
            time=itemView.findViewById(R.id.text_time);

        }
    }

}


