package com.example.refrigeratorgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    //카테고리 버튼
    private Button checklist, milk, egg, seafood, dressing, vegetable, snack, sidedish, drink, others;
    //하단 버튼 이미지
    private ImageView recipe_btn, camera_btn, alarm_btn, user_btn;

    private TextView top_title;
    public String rfg_name, user_id;

    public static SQLiteHelper sqLiteHelper; //sqlite helper 참고시 필요해서 일단 놔둠

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //카테고리 별 이동
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideChecklist.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });
        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("우유 / 유제품");
            }
        });

        egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("정육 / 계란");
            }
        });

        seafood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("수산물");
            }
        });

        sidedish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("반찬 / 냉장 / 냉동식품");
            }
        });

        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("채소 / 과일");
            }
        });

        dressing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("조미료 / 드레싱");
            }
        });

        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("음료 / 커피 / 차");
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("간식");
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_category("기타");
            }
        });

        //하단 버튼 이동
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipesMain.class);
                intent.putExtra("rfg_name", rfg_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });
    }

    private void init() {
        checklist = (Button) findViewById(R.id.checklist);
        milk = (Button) findViewById(R.id.milk);
        egg = (Button) findViewById(R.id.egg);
        seafood = (Button) findViewById(R.id.seafood);
        dressing = (Button) findViewById(R.id.dressing);
        vegetable = (Button) findViewById(R.id.vegetable);
        snack = (Button) findViewById(R.id.snack);
        sidedish = (Button) findViewById(R.id.sidedish);
        drink = (Button) findViewById(R.id.drink);
        others = (Button) findViewById(R.id.others);

        //하단버튼 이미지
        recipe_btn = (ImageView) findViewById(R.id.recipes_book);
        camera_btn = (ImageView) findViewById(R.id.plus_camera);
        alarm_btn = (ImageView) findViewById(R.id.alarm);
        user_btn = (ImageView) findViewById(R.id.users);
//      home_btn = (ImageView) findViewById(R.id.page_home);

        //사용자 정보 가져오기
        SharedPreferences pref = getSharedPreferences("user_info_file", MODE_PRIVATE);
        rfg_name = pref.getString("rfg_name", "");
        user_id = pref.getString("user_id", "");
        System.out.println( "MainActivity, rfg_name(메인쓰레드) : " + rfg_name);

        //상단 타이틀 냉장고 이름으로 설정
        top_title = findViewById(R.id.title);
        top_title.setText(rfg_name);
    }

    public void move_category(String str){
        Intent intent1 = new Intent(MainActivity.this, InsideFood.class);
        intent1.putExtra("category_name", str); // 타이틀 이름 인텐트 보냄
        startActivity(intent1); //액티비티 이동
    }
}