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
import com.service.microjc.stType.ExamInfo;

import java.util.List;


public class ExamInfoAdapter extends RecyclerView.Adapter<ExamInfoAdapter.MyHolder> {
    private static final String TAG = "MAIN";
    private final Context context;
    private final List<ExamInfo.Info> data;
    //构造中传入上下文和带有数据的集合
    public ExamInfoAdapter(Context context, List<ExamInfo.Info> data) {
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //这个方法主要是找的我们刚刚所写的item布局
        View inflate = LayoutInflater.from(context).inflate(R.layout.exam_item, viewGroup, false);
        return new MyHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //将集合中的具体数据拿到相应的item项中展示
        myHolder.exam_name.setText(data.get(i).getName());
        myHolder.classroom.setText(data.get(i).getPlace());
        myHolder.Exam_StudentName.setText(data.get(i).getStudentName());
        myHolder.localNumber.setText(data.get(i).getNumber());
        myHolder.location.setText(data.get(i).getWhere());
        myHolder.how.setText(data.get(i).getHow());
        myHolder.exam_time.setText(data.get(i).getTime());
        Log.d(TAG, "data长度为>>>>>>>>>>>>>>>>>>>>：" + data.size());
    }
    @Override
    public int getItemCount() {
        //集合的长度
        Log.d(TAG, "集合的长度为：" + data.size());
        return data.size()/2;
    }
    public void refresh(List<ExamInfo.Info> arrayList){
        //这个方法是我们自己手写的，主要是对适配器的一个刷新
        Log.d(TAG, "传入List集合的长度为>>>>>>>>>>>>>>>>>>>>>>：" + arrayList.size());

        this.data.addAll(arrayList);
        notifyDataSetChanged();
    }
    static class MyHolder extends RecyclerView.ViewHolder {
        //ViewHolder的作用主要是 性能的优化，在每个子item项中的子控件都是一样的情况下，达到控件的复用从而达到节约系统资源的目的
        TextView exam_name;
        TextView classroom;
        TextView Exam_StudentName;
        TextView localNumber;
        TextView location;
        TextView how;
        TextView exam_time;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            exam_name=itemView.findViewById(R.id.exam_name);
            classroom=itemView.findViewById(R.id.classroom);
            Exam_StudentName=itemView.findViewById(R.id.Exam_StudentName);
            localNumber=itemView.findViewById(R.id.localNumber);
            location=itemView.findViewById(R.id.location);
            how=itemView.findViewById(R.id.how);
            exam_time=itemView.findViewById(R.id.exam_time);
        }
    }

}


