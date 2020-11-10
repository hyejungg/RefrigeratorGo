package com.example.refrigeratorgo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//상황별
public class RecipesFragment2 extends Fragment {

    ImageView r_btn2_1, r_btn2_2, r_btn2_3,r_btn2_4,r_btn2_5,r_btn2_6, r_btn2_7;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2_recipes, null);

        //일상 버튼
        r_btn2_1  = (ImageView)view.findViewById(R.id.r_btn_daily);
        r_btn2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new RecipesFragment1().move("r_btn_daily"); 이거쓰고 싶은데 안됨..
                move("r_btn_daily");
            }
        });
        //안주,야식 버튼
        r_btn2_2  = (ImageView)view.findViewById(R.id.r_btn_night);
        r_btn2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_night");
            }
        });
        //다이어트 버튼
        r_btn2_3  = (ImageView)view.findViewById(R.id.r_btn_diet);
        r_btn2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_diet");
            }
        });
        //손님상 버튼
        r_btn2_4  = (ImageView)view.findViewById(R.id.r_btn_guest);
        r_btn2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_guest");
            }
        });
        //건강식 버튼
        r_btn2_5  = (ImageView)view.findViewById(R.id.r_btn_health);
        r_btn2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_health");
            }
        });
        //간편식 버튼
        r_btn2_6  = (ImageView)view.findViewById(R.id.r_btn_simple);
        r_btn2_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_simple");
            }
        });
        //간식 버튼
        r_btn2_7  = (ImageView)view.findViewById(R.id.r_btn_dessert2);
        r_btn2_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_dessert2");
            }
        });
        return view;
    }

    public void move(String btn_name){
        Bundle bundle = new Bundle();
        bundle.putString("btnId", btn_name);  //전달할 key,value(버튼 ID값)설정
        Log.i(this.getClass().getName(), "클릭성공");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        RecipesInside Recipes_inside = new RecipesInside();
        Recipes_inside.setArguments(bundle); //Recipes_inside로 버튼ID값 전달완료

        transaction.replace(R.id.frame, Recipes_inside);
        transaction.commit();//저장해라 commit
    }
}
