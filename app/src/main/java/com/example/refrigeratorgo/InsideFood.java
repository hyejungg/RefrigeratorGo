package com.example.refrigeratorgo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InsideFood extends AppCompatActivity {
    GridView grid_view;
    FoodListAdapter adapter = null;
    TextView top_title;
    String title_name, rfgName;
    ArrayList<Food> group_food; //푸드 정보 객체배열
    ImageView food_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_food);
        init();
        new BackgroundTask().execute();

        grid_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"수정", "삭제"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(InsideFood.this);

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { //수정
                            showDialogUpdate(InsideFood.this, position);
                            // show dialog update at here
                        } else { //삭제
                            adapter.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void init(){
        // 사용자 정보 불러오기
        SharedPreferences pref = getSharedPreferences("user_info_file", MODE_PRIVATE);
        rfgName = pref.getString("rfg_name", "");

        top_title = (TextView) findViewById(R.id.title);
        grid_view = (GridView) findViewById(R.id.grid);
        group_food = new ArrayList<>();
        adapter = new FoodListAdapter(this, R.layout.food_items, group_food);
        grid_view.setAdapter(adapter);

        Intent intent1 = getIntent();
        title_name = intent1.getStringExtra("category_name");
        top_title.setText(title_name);
        System.out.println("InsideFood, rfg_name : " + rfgName);
    }



    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_activity);
        dialog.setTitle("수정");

        food_img = (ImageView) dialog.findViewById(R.id.imageView);
        final EditText et_name = (EditText) dialog.findViewById(R.id.name);
        final EditText et_date = (EditText) dialog.findViewById(R.id.dDate);
        final EditText et_memo = (EditText) dialog.findViewById(R.id.dMemo);
        //final TextView et_cate = (TextView) dialog.findViewById(R.id.category);
        Button update_btn = (Button) dialog.findViewById(R.id.btnUpdate);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        food_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        InsideFood.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        //업데이트 버튼 클릭
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String food_name = et_name.getText().toString().trim();
                String food_date = et_date.getText().toString().trim();
                String memo = et_memo.getText().toString().trim();
                try {
                    adapter.update(position, food_name, memo, food_date);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "수정 완료했습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results) {

        if (request_code == 888) {
            if (grant_results.length > 0 && grant_results[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                food_img.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://zoooz0616.dothome.co.kr/getFoodInfo.php?rfgName=" +
                    rfgName + "&category=" + title_name;
        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuffer sb = new StringBuffer();

            try {
                URL php_url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) php_url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    is = httpURLConnection.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));

                    //한줄씩 읽어서 stringBuilder에 저장함
                    while (true) {
                        String stringLine = br.readLine();
                        if (stringLine == null) break;
                        sb.append(stringLine + "\n");
                    }
                }
                parsing(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
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

    public void parsing(String data) {
        try {
            JSONObject jsonobject = new JSONObject(data);
            JSONArray jsonArray = new JSONArray(jsonobject.getString("response"));
            group_food.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                Food food_obj = new Food(); //푸드 객체배열 생성
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                food_obj.setName(jsonObject1.getString("foodName")); // 식품명 저장
                System.out.print(jsonObject1.getString("foodName"));
                food_obj.setDate(jsonObject1.getString("foodDate")); // 유통기한 저장
                System.out.print(jsonObject1.getString("foodDate"));
                food_obj.setMemo(jsonObject1.getString("memo")); // 메모 저장
                System.out.print(jsonObject1.getString("memo"));
                food_obj.setImage(jsonObject1.getString("imgUrl")); // 이미지 저장
                System.out.print(jsonObject1.getString("imgUrl")); //이미지 저장
                food_obj.setRfgName(jsonObject1.getString("rfgName"));
                System.out.print(jsonObject1.getString("rfgName"));
                food_obj.setCategory(jsonObject1.getString("category"));
                System.out.print(jsonObject1.getString("category"));
                food_obj.setIndex(jsonObject1.getInt("foodIndex"));
                System.out.println(jsonObject1.getInt("foodIndex"));
                group_food.add(food_obj); //동적배열에 객체 1 저장
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
