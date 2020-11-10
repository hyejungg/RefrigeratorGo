package com.example.refrigeratorgo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddfoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //식품 정보
    ImageView food_img;
    EditText food_name, food_date, memo;
    TextView category;
    Spinner spinner;
    String[] food_strings;

    //addfood 관련 버튼
    Button add_btn;
    ImageButton gallery_btn, bcode_btn;

    //하단 버튼 이미지
    ImageView home_btn, recipe_btn, camera_btn, alarm_btn, user_btn;

    String bcode_url = "https://www.cvslove.com/product/product_view.asp?pcode=";
    String bcode, img_url, rfg_name, user_id;

    private TextView bcode_num;

    final int PICK_FROM_ALBUM = 999;
    private IntentIntegrator integrator;

    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfood);
        NetworkUtil.setNetworkPolicy(); // 네트워크 관련 스레드 정책 허용
        init();
        getPreferences(); //사용자 정보

        //바코드 스캔버튼
        bcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); (바코드를 qr코드로 한정)
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class); // 바코드 세로모드 지원
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false); //true로 설정하면 인식시 '삐'소리남
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        //갤러리 이미지 버튼 (차후 갤러리 이미지 대신 카테고리별 기본 이미지로 수정할 것임)
        gallery_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //check runtime permission
                ActivityCompat.requestPermissions(
                        AddfoodActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PICK_FROM_ALBUM
                );
            }
        });

        //등록 버튼
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String food_name = AddfoodActivity.this.food_name.getText().toString();
                    String rfg_name = getRfgName();
                    String memo = AddfoodActivity.this.memo.getText().toString();
                    String food_date = AddfoodActivity.this.food_date.getText().toString();
                    String category = AddfoodActivity.this.category.getText().toString();

                try {
                    PHPRequest request = new PHPRequest(); //**서버연결요청 & 데이터삽입 클래스 객체생성

                    String result = request.PHPtest(food_name, rfg_name, memo, food_date, category, img_url); //**db에 데이터 삽입 결과값 반환
                    if("1".equals(result)){ //**데이터 삽입 성공
                        Toast.makeText(getApplication(),food_name + " 추가!",Toast.LENGTH_SHORT).show();
                    }
                    else{ //**데이터 삽입 실패
                        Toast.makeText(getApplication(),"추가 실패",Toast.LENGTH_SHORT).show();
                    }
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    AddfoodActivity.this.food_name.setText("");
                    AddfoodActivity.this.food_date.setText("");
                    food_img.setImageResource(R.drawable.background5);
                    AddfoodActivity.this.category.setText("");
                    AddfoodActivity.this.memo.setText("");
                    bcode_num.setText("");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        home_btn = (ImageView) findViewById(R.id.page_home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        recipe_btn = (ImageView) findViewById(R.id.recipes_book);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn = (ImageView) findViewById(R.id.plus_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn = (ImageView) findViewById(R.id.users);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results) {
        if(request_code == PICK_FROM_ALBUM){
            if(grant_results.length > 0 && grant_results[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
            else{
                Toast.makeText(getApplicationContext(), "You dont have permission to access file locationl", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
    }

    //handle result of picked image
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        category.setText(food_strings[i]);
        if(category.getText().toString().equals("선택하세요")){
            category.setText("");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
        category.setText("");
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, @Nullable Intent data) {
        //갤러리에서 이미지 데려오기
        if (request_code == PICK_FROM_ALBUM) {
            if (result_code == RESULT_OK && null != data){
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 150, 150, true); //150로 이미지 크기 고정
//                    img_url = scaled;
                    food_img.setImageBitmap(scaled);

                } catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (Exception e) { e.printStackTrace(); }

                super.onActivityResult(request_code, result_code, data);
            }
        }
        //바코드스캔 후 상품 정보 파싱
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(request_code, result_code, data);
            bcode = result.getContents(); //바코드 번호
            if (bcode == null) {
                Toast.makeText(this, "You cancelled this scanning", Toast.LENGTH_LONG).show();
            } else {
                bcode_num.setText(bcode);
                new Description().execute(); // 상품명 & 이미지 파싱
            }
        }
    }

    // 바코드로 인식된 식품의 이름, 이미지 파싱
    private class Description extends AsyncTask<String, String, String[]> {
        String[] arr = new String[2];
        String img_url;
        String pdt_name; //식품명

        @Override
        protected String[] doInBackground(String... params) {
            try {
                String php_url = bcode_url + bcode;
                Document document1 = Jsoup.connect(php_url).get();
                Elements mElementDataSize1 = ((Document) document1).select("table[ID=Table4]");
                for (Element elem : mElementDataSize1) {
                    pdt_name = elem.select("td[width=60%]").first().text();
                    arr[0] = pdt_name;
                }

                Document document2 = Jsoup.connect(php_url).get();
                Elements mElementDataSize2 = ((Document) document2).select("table[ID=Table3]");
                for (Element elem : mElementDataSize2) {
                    img_url = elem.select("img").attr("src");
                    AddfoodActivity.this.img_url = img_url;
                    arr[1] = img_url;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.w("예외발생", "catch문 읽음");
            }
            return arr;
        }
        //제품명 적용
        @Override
        protected void onPostExecute(String[] arr) {
            food_name.setText(arr[0]);
            Glide.with(AddfoodActivity.this).load(arr[1]).override(150,150).skipMemoryCache(true).into(food_img);
        }
    }

    private void init(){
        //바코드
        integrator = new IntentIntegrator(this);

        //식품 카테코리 스피너
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        food_strings = new String[]{"선택하세요", "우유 / 유제품", "정육 / 계란", "수산물",
                "반찬 / 냉장 / 냉동식품","채소 / 과일", "간식","조미료 / 드레싱","음료 / 커피 / 차","기타"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, food_strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //버튼
        bcode_btn =(ImageButton)findViewById(R.id.scan_btn);
        gallery_btn = (ImageButton)findViewById(R.id.choose_image_btn);
        add_btn = (Button)findViewById(R.id.textbutton);

        //식품 정보
        food_img = (ImageView) findViewById(R.id.image_view);
        bcode_num = (TextView)findViewById(R.id.scan_content);
        food_name = (EditText)findViewById(R.id.product_name);
        food_date = (EditText)findViewById(R.id.mYear);
        category =(TextView)findViewById(R.id.selectedText);
        memo = (EditText)findViewById(R.id.edtMemo);

        //하단버튼 이미지
        home_btn = (ImageView) findViewById(R.id.page_home);
        recipe_btn = (ImageView) findViewById(R.id.recipes_book);
        camera_btn = (ImageView) findViewById(R.id.plus_camera);
        alarm_btn = (ImageView) findViewById(R.id.alarm);
        user_btn = (ImageView) findViewById(R.id.users);
    }

    String getRfgName(){
        return rfg_name;
    }

    // 앱 내 기본 데이터 가져오기
    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("user_info_file", MODE_PRIVATE);
        rfg_name = pref.getString("rfg_name", "jango"); // 냉장고 이름, s1:default value
        user_id = pref.getString("user_id", "ID"); // 사용자 아이디
    }
}

// 인터넷 연결시(네트워크 관련 처리를 메인 쓰레드에서 처리할 경우) runtime 에러 방지
class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetworkPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}

