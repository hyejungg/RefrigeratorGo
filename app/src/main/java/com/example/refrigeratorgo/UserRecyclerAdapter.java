package com.example.refrigeratorgo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list
    private ArrayList<User> listData = new ArrayList<>();
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 food_strings.xml을 inflate 시킴
        // return 인자는 ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_content, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position));

        //꾹 누루면 삭제
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        return listData.size();
    }

    public void remove(int position){
        try{
            listData.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    void addItem(User data) {
        // 외부에서 item을 추가시킬 함수
        listData.add(data);
    }

    // RecyclerView의 핵심 : ViewHolder
    // 여기서 subView를 setting
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.btn_disconnect);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void onBind(User data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
            textView3.setText(data.getBtn());
            imageView.setImageResource(data.getResId());
        }
    }
}