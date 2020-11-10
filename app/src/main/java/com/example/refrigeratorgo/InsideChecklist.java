package com.example.refrigeratorgo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InsideChecklist extends AppCompatActivity {

    Button add, delete;
    EditText editText;

    Context context;

    private String userId, list;

    ArrayList<String> items;
    ArrayAdapter adapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_checklist);

        context = getApplicationContext();

        // 빈 데이터 리스트 생성.
//        final ArrayList<String> items = new ArrayList<String>();
        items = new ArrayList<>();
        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);

        // listview 생성 및 adapter 지정.
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        //user_id 인텐트 받기.
        Intent receive = getIntent();
        userId = receive.getStringExtra("user_id");
        System.out.println(getClass() + "userId는 " + userId);

        editText = (EditText) findViewById(R.id.editTextitem);

        //서버에 있는 Cart값 가져오기.
        new CartSelectTask().execute();

        //add버튼 클릭 시 아이템 추가
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = editText.getText().toString();

                //OutputStream outputStream = conn.getOutputStream(); 오류 해결하기 위해 AsynsTask에 묶음.
                new CartInsertTask().execute(userId, list); //db 값 저장(1)
                System.out.println("****db에 저장되는 값은 : userId는 " + userId + ", 해당 list는 " + list);

                //새로 추가한 것도 밑에 보이도록 우선 add.
                items.add(list);
                adapter.notifyDataSetChanged();
                editText.setText("");//다시 공백으로 초기화
            }
        });


        //delete버튼 클릭 시 아이템 삭제
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //list에서 삭제
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                int count = adapter.getCount();

                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        String deleteList = items.get(i);
                        items.remove(i);

                        //phpmyadmin db에서 삭제
                        new CartDeleteTask().execute(deleteList, userId);
                        System.out.println("cartDeleteTask 읽힘.");
                    }
                }
                // 모든 선택 상태 초기화.
                listview.clearChoices();
                adapter.notifyDataSetChanged();
            }
        });
    }

    //내부클래스로 생성. CART에 데이터 삽입
    class CartInsertTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String userId = arg0[0];
                String list = arg0[1];
                //데이터 삽입하는 코드
                String postData = "user_id=" + userId + "&" + "list=" + list; //전송 데이터 https://twinw.tistory.com/29
                String link = "http://zoooz0616.dothome.co.kr/CartInsert.php";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream(); //******오류 발생. 해결하기위해 해당 클래스 AsyncTask 상속하도록 수정함.
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush(); //outputstream 내부의 작은 버퍼. 데이터 출력 전 쌓아뒀다가 순서대로 출력.
                outputStream.close();
                String result = readStream(conn.getInputStream());
                conn.disconnect();
                parsing(result);

                return result; // success : 1, fail : -1
            } catch (Exception e) {
                Log.i("RequestCART", "request was failed.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        public String readStream(InputStream in) throws IOException { //** 수정 필요없이 그대로 복붙하면 됨
            StringBuilder jsonHtml = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = " ";

            while ((line = reader.readLine()) != null)
                jsonHtml.append(line);

            reader.close();
            return jsonHtml.toString();
        }
    }

    class CartDeleteTask extends AsyncTask<String, Void ,Void>{
        String target;

        @Override
        protected Void doInBackground(String... arg) {
            String deleteList = arg[0];
            String deleteUserID = arg[1];
            //파싱으로 가져올 웹페이지
            target = "http://zoooz0616.dothome.co.kr/CartDelete.php?list=" + deleteList + "&" + "userID=" + deleteUserID;

            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader reader = null;
            StringBuffer stringBuffer = new StringBuffer();

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    is = httpURLConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));

                    //한줄씩 읽어서 stringBuilder에 저장함
                    while (true) {
                        String stringLine = reader.readLine();
                        if (stringLine == null) break;
                        stringBuffer.append(stringLine + "\n");
                    }
                }
                System.out.println(stringBuffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (isr != null) isr.close();
                    if (is != null) is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

    class CartSelectTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //파싱으로 가져올 웹페이지
            target = "http://zoooz0616.dothome.co.kr/CartSelect.php?user_id=" +
                    userId;
        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader reader = null;
            StringBuffer stringBuffer = new StringBuffer();

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    is = httpURLConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));

                    //한줄씩 읽어서 stringBuilder에 저장함
                    while (true) {
                        String stringLine = reader.readLine();
                        if (stringLine == null) break;
                        stringBuffer.append(stringLine + "\n");
                    }
                }
                System.out.println(stringBuffer.toString());
                parsing(stringBuffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (isr != null) isr.close();
                    if (is != null) is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    //db에 있는 값 가져오는 내부클래스.
    public void parsing(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("response"));

            //arrayList 클리어
            if(items != null){
                items.clear();
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                String listData;
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                listData = jsonObject1.getString("list");
                System.out.println(listData);
                items.add(listData);
                System.out.println("items : " + items);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}