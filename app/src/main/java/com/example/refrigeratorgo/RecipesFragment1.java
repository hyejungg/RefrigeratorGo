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

//종류별
public class RecipesFragment1 extends Fragment {

    ImageView r_btn1_1, r_btn1_2, r_btn1_3,r_btn1_4,r_btn1_5,r_btn1_6,r_btn1_7,r_btn1_8, r_btn1_9;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_recipes, null);

        //반찬 버튼
        r_btn1_1  = (ImageView)view.findViewById(R.id.r_btn_sidedish);
        r_btn1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_sidedish");
            }
        });

        //국,찌개,스프 버튼
        r_btn1_2  = (ImageView)view.findViewById(R.id.r_btn_soup);
        r_btn1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               move("r_btn_soup");
            }
        });
        //밥,죽 버튼
        r_btn1_3  = (ImageView)view.findViewById(R.id.r_btn_rice);
        r_btn1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               move("r_btn_rice");
            }
        });
        //면 버튼
        r_btn1_4  = (ImageView)view.findViewById(R.id.r_btn_noodles);
        r_btn1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_noodles");
            }
        });
        //양식 버튼
        r_btn1_5  = (ImageView)view.findViewById(R.id.r_btn_pizza);
        r_btn1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_pizza");
            }
        });
        //베이킹 버튼
        r_btn1_6  = (ImageView)view.findViewById(R.id.r_btn_baking);
        r_btn1_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_baking");
            }
        });
        //디저트 버튼
        r_btn1_7  = (ImageView)view.findViewById(R.id.r_btn_dessert);
        r_btn1_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_dessert");
            }
        });
        //잼, 드레싱 버튼
        r_btn1_8  = (ImageView)view.findViewById(R.id.r_btn_jam);
        r_btn1_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               move("r_btn_jam");
            }
        });
        //차, 음료 버튼
        r_btn1_9  = (ImageView)view.findViewById(R.id.r_btn_tea);
        r_btn1_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               move("r_btn_tea");
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