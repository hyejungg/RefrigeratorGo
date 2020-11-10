package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class ConnectActivity extends AppCompatActivity {

    Button button4;
    EditText search_num;
    ImageView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect);

        search_num = (EditText)findViewById(R.id.search_num);

        browser = (ImageView)findViewById(R.id.browser);

        //수정 필요
        browser.setOnClickListener(new View.OnClickListener() {
            //검색버튼을 클릭하면 데이터베이스에서 전화번호에 일치한 사용자 정보를 불러오도록 수정
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });

        button4 = (Button)findViewById(R.id.add);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4.setText("취소");
            }
        });

    }
}
