package com.example.refrigeratorgo;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class UserSettingsActivity extends AppCompatActivity{

    private String user_name1;
    private EditText user_name2;
    private String refrigerator_name1;
    private EditText refrigerator_name2;
    private String email1;
    private EditText email2;
    private String number1;
    private EditText number2;
    private Bitmap profile_image1;
    private ImageButton gallery;
    private ImageView imageView;
    private ImageView home_btn, recipe_btn, camera_btn, alarm_btn, user_btn;

    private SharedPreferences appData;
    private static int PICK_IMAGE_REQUEST = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    static final String TAG = "UserSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersettings);

        Intent intent6 = getIntent();
        String user_name = intent6.getStringExtra("EXTRA_MESSAGE");
        String user_email = intent6.getStringExtra("EXTRA_MESSAGE1");
        String user_phone = intent6.getStringExtra("EXTRA_MESSAGE2");
        String user_rename = intent6.getStringExtra("EXTRA_MESSAGE3");

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        imageView = (ImageView) findViewById(R.id.profile_image);
        gallery = (ImageButton) findViewById(R.id.gallery);
        user_name2 = (EditText)findViewById(R.id.user_name);
        refrigerator_name2 = (EditText)findViewById(R.id.refrigerator_name);
        email2 = (EditText)findViewById(R.id.email);
        number2 = (EditText)findViewById(R.id.number);
        Button confirm_button = (Button) findViewById(R.id.confirm_button);

        // 이전에 정보를 저장시킨 기록이 있다면
        if (true) {
            user_name2.setText(user_name1);
            refrigerator_name2.setText(refrigerator_name1);
            email2.setText(email1);
            number2.setText(number1);
        }

        if(user_name != null && user_email!= null){
            user_name2.setText(user_name);
            email2.setText(user_email);
            number2.setText(user_phone);
            refrigerator_name2.setText(user_rename);
        }
        else {
            user_name2.setText("이름");
            email2.setText("jango@sungshin.ac.kr");
            number2.setText("naegjango");
            refrigerator_name2.setText("Refrigerator Name");
        }

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 성공시 저장 처리, 예제는 무조건 저장
                save();
                Intent intent = new Intent(UserSettingsActivity.this, UserOutActivity.class);

                // Name 입력 값을 String 값으로 변환하여 전달.
                EditText user_name2 = (EditText) findViewById(R.id.user_name);
                intent.putExtra("name", user_name2.getText().toString());

                // Refrigerator name 입력 값을 String 값으로 변환하여 전달.
                EditText refrigerator_name = (EditText) findViewById(R.id.refrigerator_name);
                intent.putExtra("refrigerator", refrigerator_name.getText().toString());

                // Email 입력 값을 String 값으로 변환하여 전달.
                EditText email = (EditText) findViewById(R.id.email);
                intent.putExtra("email", email.getText().toString());

                // Phone 입력 값을 String 값으로 그대로 전달.
                EditText number = (EditText) findViewById(R.id.number);
                intent.putExtra("number", number.getText().toString());
            }
        });

        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //check runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions ={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for rntime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        loadImagefromGallery(v);
                    }
                }
                else{
                    //system os is less then marshmallow
                    loadImagefromGallery(v);
                }
            }
        });

        home_btn =  (ImageView) findViewById(R.id.home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                String value = refrigerator_name2.getText().toString();

                //냉장고 이름을 내가 설정한 걸로 변경
                if(refrigerator_name2 != null) {
                    Log.i("보냄", value);
                    intent.putExtra("send", value);
                }
                else {
                    Log.i("안보냄", value);
                }
                startActivity(intent);
            }
        });

        recipe_btn =  (ImageView) findViewById(R.id.recipe);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        camera_btn =  (ImageView) findViewById(R.id.add);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.group);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn =  (ImageView) findViewById(R.id.user);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putString("user name", user_name2.getText().toString().trim());
        editor.putString("refrigerator name", refrigerator_name2.getText().toString().trim());
        editor.putString("email", email2.getText().toString().trim());
        editor.putString("number", number2.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        user_name1 = appData.getString("user name", "name");
        refrigerator_name1 = appData.getString("refrigerator name", "name");
        email1 = appData.getString("email", "naejango@sungshin.ac.kr");
        number1 = appData.getString("number", "01000100010");
    }

    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    //이미지 선택작업을 후의 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //set imag to image view
            imageView.setImageURI(data.getData());
        }
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                imageView.setImageBitmap(scaled);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
}