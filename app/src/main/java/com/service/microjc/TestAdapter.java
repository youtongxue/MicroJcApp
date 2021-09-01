package com.service.microjc;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.service.microjc.stType.LibraryUserInfo;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter {
    private List<LibraryUserInfo.BooksInfo> data = new ArrayList<>();

    public void setData(List<LibraryUserInfo.BooksInfo> d) {
        this.data = d;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public BookViewHolder(View itemView) {
            super(itemView);
        }
    }
}
