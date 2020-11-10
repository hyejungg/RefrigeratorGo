package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class UserSetAccountActivity extends AppCompatActivity {

    private UserRecyclerAdapter adapter;
    private ImageView home_btn, recipe_btn, camera_btn, alarm_btn, user_btn;
    private TextView btn1, btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetaccount);

        init();

        //FragmentTransaction의 API를 사용하여 Fragment의 추가, 제거, 변경 등의 작업을 수행
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                UserAccoutFragment1 fragment1 = new UserAccoutFragment1();
                //버튼을 눌렀을 때 RE-Fr자바를 탈 수 있도록 함
                transaction.replace(R.id.frameaccout, fragment1); //프레임 레이아웃에서 프레그먼트 1로 변경(replace)해라
                transaction.commit(); //저장해라 commit
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                UserAccoutFragment2 fragment2 = new UserAccoutFragment2();
                transaction.replace(R.id.frameaccout, fragment2);
                transaction.commit();
            }
        });


        home_btn =  (ImageView) findViewById(R.id.home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetAccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        recipe_btn =  (ImageView) findViewById(R.id.recipe);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetAccountActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn =  (ImageView) findViewById(R.id.add);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetAccountActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.group);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetAccountActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn =  (ImageView) findViewById(R.id.user);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetAccountActivity.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) .
                startActivity(intent);
            }
        });

    }

    private void init() {
        btn1 = (TextView) findViewById(R.id.btn_1); //연결된
        btn2 = (TextView) findViewById(R.id.btn_2); //연결할
    }
}
