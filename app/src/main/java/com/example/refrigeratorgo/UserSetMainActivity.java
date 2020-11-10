package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserSetMainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    //LinearLayout firstLinear = (LinearLayout)findViewById(R.id.first_row);
    //LinearLayout secondLinear = (LinearLayout)findViewById(R.id.second_row);
    //LinearLayout thirdLinear = (LinearLayout)findViewById(R.id.third_row);
   // LinearLayout fourthLinear = (LinearLayout)findViewById(R.id.forth_row);
    //LinearLayout fifthLinear = (LinearLayout)findViewById(R.id.fifth_row);
    //Button account_btn;

    private ImageView home_btn, recipe_btn, camera_btn, alarm_btn;
    public void Initialize(){

        Button notice_btn = (Button)findViewById(R.id.notice_button);
        Button account_btn = (Button)findViewById(R.id.account_button);
        Button settings_btn = (Button)findViewById(R.id.settings_button);
        Button exit_btn = (Button)findViewById(R.id.exit_button);

        Switch switchView = (Switch)findViewById(R.id.switchButton);

        notice_btn.setOnClickListener(this);
        account_btn.setOnClickListener(this);
        settings_btn.setOnClickListener(this);
        exit_btn.setOnClickListener(this);

        switchView.setOnCheckedChangeListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetmain);
        Initialize();

        Intent intent6 = getIntent();
        String user_name = intent6.getStringExtra("EXTRA_MESSAGE");
        String user_email = intent6.getStringExtra("EXTRA_MESSAGE1");

        TextView textView = findViewById(R.id.display_name);
        TextView textView2 = findViewById(R.id.display_email);
        if(user_name != null && user_email!= null){
            textView.setText(user_name);
            textView2.setText(user_email);

        }
        else {
            textView.setText("ID");
            textView2.setText("jango@sungshin.ac.kr");
        }

        home_btn = (ImageView) findViewById(R.id.home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetMainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        recipe_btn = (ImageView) findViewById(R.id.recipe);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetMainActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        camera_btn = (ImageView) findViewById(R.id.add);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetMainActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.notification);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetMainActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });
        }

    @Override
    public void onClick(View v){
        Intent intent = null;
        switch(v.getId()) {
            case R.id.notice_button :
                intent = new Intent(this, UserSetNoticeActivity.class);
                break;
            case R.id.account_button :
                intent = new Intent(this, UserSetAccountActivity.class);
                break;
            case R.id.settings_button :
                intent = new Intent(this, UserSettingsActivity.class);
                Intent intent6 = getIntent();
                String user_name = intent6.getStringExtra("EXTRA_MESSAGE");
                String user_email = intent6.getStringExtra("EXTRA_MESSAGE1");
                String user_phone = intent6.getStringExtra("EXTRA_MESSAGE2");
                String user_rename = intent6.getStringExtra("EXTRA_MESSAGE3");
                intent = new Intent(this, UserSettingsActivity.class);
                intent.putExtra("EXTRA_MESSAGE", user_name);
                intent.putExtra("EXTRA_MESSAGE1", user_email);
                intent.putExtra("EXTRA_MESSAGE2", user_phone);
                intent.putExtra("EXTRA_MESSAGE3", user_rename);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.exit_button :
                intent = new Intent(this, UserOutActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton swich, boolean isChecked) {
        // 스위치버튼을 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드

       if(isChecked){
        //체크된 상태 취소시 코드
           Toast.makeText(UserSetMainActivity.this, "푸시알람 ON!", Toast.LENGTH_SHORT).show();

       }else{
           //체크된 상태로 만들시 코드
           Toast.makeText(UserSetMainActivity.this, "푸시알람 OFF!", Toast.LENGTH_SHORT).show();
       }
    }
}
