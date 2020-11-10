package com.example.refrigeratorgo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {
    private EditText et_id, et_pwd, et_name, et_email, et_rfg_name;
    private Button join_btn;
    static final String TAG = "JoinActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {//액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();

        //회원가입 버튼 클릭 시 수행
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edittext에 입력되어있는 값을 get해옴

                final String userId = et_id.getText().toString();
                final String userPwd = et_pwd.getText().toString();
                final String userName = et_name.getText().toString();
                final String rfgName = et_rfg_name.getText().toString();
                final String userEmail = et_email.getText().toString();

                Response.Listener<String> responseListener =  new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);

//                                SharedPreferences rfgNameFile = getSharedPreferences("rfgNameFile", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = rfgNameFile.edit();
//                                editor.putString("rfgName", rfgName);
//                                editor.putString("userId", userId);
//                                editor.commit(); //변경된 내용 저장
                                startActivity(intent);
                            }
                            else {//회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley이용해 요청.
                RegisterRequest registerRequest = new RegisterRequest(userName, userId,  userEmail, rfgName, userPwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                queue.add(registerRequest);
            }
        });
    }
    public void init(){
        //id값 찾아주기.
        et_id =findViewById(R.id.et_phone);
        et_pwd =findViewById(R.id.et_pass);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_rfg_name =findViewById(R.id.et_rfg_name);
        join_btn=findViewById(R.id.join_btn);
    }
}
