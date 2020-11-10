package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); //액티비티와 연결되는 레이아웃으로 activity_splash설정

        TextView logoText = (TextView) findViewById(R.id.logoText); //이미지뷰의 로고(id:splashimg)
        Animation animTrans = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_anim); //로고 애니메이션
        logoText.startAnimation(animTrans);//로고 애니메이션 시작


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("state", "launch");
                startActivity(intent);
                finish();
            }
        }, 2300);  // 메인 Activity로 이동 전 Delay Term 2.3초로 설정
    }
}
