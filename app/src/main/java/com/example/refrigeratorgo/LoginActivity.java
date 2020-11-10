package com.example.refrigeratorgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

;import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pw;
    Button join_btn, log_btn;
    String rfg_name, user_id, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        //회원가입 버튼을 클릭했을 때 회원가입 액티비티로 전환
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼을 클릭했을 때 로그인화면 액티비티로 전환
        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext에 입력되어있는 값을 get해옴
                final String text_id = et_id.getText().toString();
                final String text_pwd = et_pw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//로그인에 성공한 경우
                                String json_id = jsonObject.getString("userId");
                                user_id = json_id;

                                new BackgroundTask().execute();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userId", user_id);

                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            } else {//로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(text_id, text_pwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    public void init() {
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pass);
        join_btn = (Button) findViewById(R.id.joinbutton);
        log_btn = (Button) findViewById(R.id.log_btn);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //파싱으로 가져올 웹페이지
            target = "http://zoooz0616.dothome.co.kr/SelectBasicInfo.php?userId=" + user_id;
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL php_url = new URL(target);
                HttpURLConnection conn = (HttpURLConnection) php_url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            sb.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                    getRfgName(sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }


    public void getRfgName(String result) {

        try {
            JSONObject jsonobject = new JSONObject(result);
            rfg_name = jsonobject.getString("rfgName");
            System.out.println("LoginActivity, rfgName " + rfg_name);
//          user_email =jsonObject.getString("userEmail"); 필요시 사용
//            System.out.println("LoginActivity" + user_email);

            SharedPreferences pref = getSharedPreferences("user_info_file", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("rfg_name", rfg_name);
            editor.putString("user_id", user_id);
            editor.commit(); //변경된 내용 저장

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("tagconvertstr", "[" + result + "]");
        }
    }
}
