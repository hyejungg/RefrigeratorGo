package com.example.refrigeratorgo;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.CustomViewHolder> {


    private ArrayList<AlarmData> arrayList;
    //여러 아이콘들
    /*public static final  int[] randomIcon= {
            R.drawable.lettuce, R.drawable.vegetarian, R.drawable.icecream, R.drawable.corn,
            R.drawable.thanksgiving, R.drawable.avocado, R.drawable.pumpkin, R.drawable.pineapple,
            R.drawable.tomato, R.drawable.zucchini
    };

    Random random = new Random();
    int i = random.nextInt(randomIcon.length);*/

    public AlarmAdapter(ArrayList<AlarmData> arrayList) {
        this.arrayList = arrayList;
    }


    //액티비티에 onCreate랑 비슷하게 리사이클뷰가 생성될 떄 ~ 임
    @androidx.annotation.NonNull
    @Override
    public AlarmAdapter.CustomViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final CustomViewHolder holder, final int position) {
        holder.foodName.setText(arrayList.get(position).getFoodName() + "의 유통기한이 2일 남았습니다.");
        holder.foodDate.setText("유통기한: " + arrayList.get(position).getFoodDate());
        holder.foodImage.setImageResource(R.drawable.alarmclock);


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM ALARM");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()){
                    arrID.add(c.getInt(0));
                }
                showDelete(arrID.get(position));
                remove(holder.getAdapterPosition());
            }
        });
    }

    private void showDelete(Integer integer) {
        try {
            MainActivity.sqLiteHelper.deleteDataAlarm(integer);
        } catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position); //notifiny 는 새로고침 같은 ㅇㅇ 거임
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView foodName;
        protected TextView foodDate;
        protected ImageView foodImage;

        public CustomViewHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);

            this.foodName = (TextView) itemView.findViewById(R.id.foodName);
            this.foodDate = (TextView) itemView.findViewById(R.id.foodDate);
            this.foodImage = (ImageView) itemView.findViewById(R.id.foodImage);
        }
    }
}
