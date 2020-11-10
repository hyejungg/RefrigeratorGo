package com.example.refrigeratorgo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class UserSetNoticeActivity extends AppCompatActivity implements View.OnClickListener{

    private UserNoticeRecyclerAdapter adapter;
    private ImageView home_btn, recipe_btn, camera_btn, alarm_btn, user_btn;
    public void Initialize() {
        RelativeLayout first = (RelativeLayout) findViewById(R.id.first_row);
        first.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetnotice);

        Initialize();
        init();
        getData();

        home_btn =  (ImageView) findViewById(R.id.home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetNoticeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        recipe_btn =  (ImageView) findViewById(R.id.recipe);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetNoticeActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn =  (ImageView) findViewById(R.id.add);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetNoticeActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.group);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetNoticeActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn =  (ImageView) findViewById(R.id.user);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetNoticeActivity.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) .
                startActivity(intent);
            }
        });
    }

    public void onClick(View v){
        Intent intent = null;
        switch(v.getId()) {
            case R.id.first_row :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/refrigeratorgo"));
                break;
        }
        startActivity(intent);
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new UserNoticeRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }
    private void getData() {
        // 임의의 데이터
        List<String> listDate = Arrays.asList("19.08.15", "19.08.15");
        List<String> listInformation = Arrays.asList(
                "레시피 업로딩 문의",
                "회원가입 문의"
        );

        for (int i = 0; i < listDate.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줌
            Notice data = new Notice();
            data.setTitle(listDate.get(i));
            data.setContent(listInformation.get(i));
            // 각 값이 들어간 data를 adapter에 추가
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줌
        adapter.notifyDataSetChanged();
    }
}
