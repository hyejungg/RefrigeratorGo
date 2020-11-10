package com.example.refrigeratorgo;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class UserNoticeRecyclerAdapter extends RecyclerView.Adapter<UserNoticeRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list
    private ArrayList<Notice> listData = new ArrayList<>();
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 food_strings.xml을 inflate 시킴
        // return 인자는 ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_notice, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        return listData.size();
    }

    void addItem(Notice data) {
        // 외부에서 item을 추가시킬 함수
        listData.add(data);
    }

    // RecyclerView의 핵심 : ViewHolder
    // 여기서 subView를 setting
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.date1);
            textView2 = itemView.findViewById(R.id.information);
        }

        void onBind(Notice data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
        }
    }
}