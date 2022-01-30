package com.service.microjc.Activity.Jw;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.service.microjc.R;
import com.service.microjc.stType.ScoresInfo;

import java.util.List;


public class ScoresInfoAdapter extends RecyclerView.Adapter<ScoresInfoAdapter.MyHolder> {
    private static final String TAG = "MAIN";
    private final Context context;
    private final List<ScoresInfo.Info> data;
    //构造中传入上下文和带有数据的集合
    public ScoresInfoAdapter(Context context, List<ScoresInfo.Info> data) {
        this.context = context;
        this.data = data;
        Log.d(TAG, "333333初始集合的长度为>>>>>>>>：" + data.size());
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //这个方法主要是找的我们刚刚所写的item布局
        View inflate = LayoutInflater.from(context).inflate(R.layout.scores_item, viewGroup, false);
        return new MyHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //将集合中的具体数据拿到相应的item项中展示
        myHolder.name.setText(data.get(i).getName());
        myHolder.score.setText(data.get(i).getScore());
        myHolder.type.setText(data.get(i).getType());
        myHolder.credit.setText(data.get(i).getCredit());
        myHolder.gpa.setText(data.get(i).getGPA());
        Log.d(TAG, "data长度为>>>>>>>>>>>>>>>>>>>>：" + data.size());
    }
    @Override
    public int getItemCount() {
        //集合的长度
        Log.d(TAG, "集合的长度为：" + data.size());
        return data.size()/2;
    }
    public void refresh(List<ScoresInfo.Info> arrayList){
        //这个方法是我们自己手写的，主要是对适配器的一个刷新
        Log.d(TAG, "传入List集合的长度为>>>>>>>>>>>>>>>>>>>>>>：" + arrayList.size());

        this.data.addAll(arrayList);
        notifyDataSetChanged();
    }
    static class MyHolder extends RecyclerView.ViewHolder {
        //ViewHolder的作用主要是 性能的优化，在每个子item项中的子控件都是一样的情况下，达到控件的复用从而达到节约系统资源的目的
        TextView name;
        TextView score;
        TextView type;
        TextView credit;
        TextView gpa;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            score=itemView.findViewById(R.id.score);
            type=itemView.findViewById(R.id.type);
            credit=itemView.findViewById(R.id.credit);
            gpa=itemView.findViewById(R.id.gpa);
        }
    }

}


