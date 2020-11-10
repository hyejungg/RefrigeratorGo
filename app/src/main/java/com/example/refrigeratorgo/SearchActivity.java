package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {


    EditText search_num;
    ImageView browser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 화면 레이아웃 파일 정의
        // R : Resource
        setContentView(R.layout.activity_search);

        search_num = (EditText)findViewById(R.id.search_num);

        browser = (ImageView) findViewById(R.id.browser);
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });


    }
}
