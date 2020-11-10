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

//조리별
public class RecipesFragment3 extends Fragment {

    ImageView r_btn3_1, r_btn3_2, r_btn3_3,r_btn3_4;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment3_recipes,container,false);

        //오븐요리 버튼
        r_btn3_1  = (ImageView)view.findViewById(R.id.r_btn_oven);
        r_btn3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_oven");
            }
        });
        //전자레인지요리 버튼
        r_btn3_2  = (ImageView)view.findViewById(R.id.r_btn_microwave);
        r_btn3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_microwave");
            }
        });
        //에어프라이기요리 버튼
        r_btn3_3  = (ImageView)view.findViewById(R.id.r_btn_air);
        r_btn3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_air");
            }
        });
        //불없이 하는 요리 버튼
        r_btn3_4  = (ImageView)view.findViewById(R.id.r_btn_Nofire);
        r_btn3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move("r_btn_Nofire");
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
