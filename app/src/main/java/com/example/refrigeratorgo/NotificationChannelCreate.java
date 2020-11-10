package com.example.refrigeratorgo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationChannelCreate extends AppCompatActivity {

    Button button;
    Context context = this;
    ImageView home_btn, recipe_btn, camera_btn, user_btn, alarm_btn;

    ArrayList<AlarmData> list;
    private AlarmAdapter adapter;

    static String name, date;
    static byte[] image;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private SQLiteHelper sqLiteHelper;

    private ArrayList<AlarmData> arrayDataArrayList;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //유통기한과 이름을 담을 배열
        list = new ArrayList<AlarmData>();

        recyclerView = (RecyclerView)findViewById(R.id.reView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AlarmAdapter(list);
        recyclerView.setAdapter(adapter);

        //db초기화
        sqLiteHelper = new SQLiteHelper(context, "FoodDB.sqlite", null, 1);

        String sql = "CREATE TABLE IF NOT EXISTS ALARM (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, Date VARCHAR)";
        sqLiteHelper.queryData(sql);

        //알람 db 보여줘
        try {
            Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM ALARM");
            list.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                name = cursor.getString(1);
                date = cursor.getString(2);
//                image = cursor.getBlob(3);

                list.add(new AlarmData(name, date)); //, image));
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            //인텐트 받아
            Intent get = getIntent();

            arrayDataArrayList = new ArrayList<>(); //알람배열

            ArrayList<String> foodNameArr = get.getStringArrayListExtra("foodName"); //이름배열 받아
            ArrayList<String> foodNameDate = get.getStringArrayListExtra("foodDate"); //유통배열 받아
//            ArrayList<byte[]> foodNameImage = get.getStringArrayListExtra("foodImage"); //사진배열 받아

            for(int i = 0; i < foodNameArr.size(); i++) {
                name = foodNameArr.get(i);
                date = foodNameDate.get(i);

                Log.i("name: ", name);
                Log.i("date: ", date);
                arrayDataArrayList.add(new AlarmData(name, date));
            }

            if(arrayDataArrayList!=null){
                Log.i("alarmDataList.size(): ", String.valueOf(arrayDataArrayList.size()));
                nofi_day();
            }else {
                Log.i("intent 값이", "null");
            }
        }catch (Exception e){
            e.printStackTrace();
        }




        //밑아이콘 클릭시 이동
        home_btn = (ImageView) findViewById(R.id.page_home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationChannelCreate.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        recipe_btn =  (ImageView) findViewById(R.id.recipes_book);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationChannelCreate.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn =  (ImageView) findViewById(R.id.plus_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationChannelCreate.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationChannelCreate.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn = (ImageView) findViewById(R.id.users);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationChannelCreate.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });
    }

    private void nofi_day() {
        int todayNum = Integer.valueOf(doCurrentDate()); //현재날짜 ( format : yyyyMMdd )

        for (int i = 0; i < arrayDataArrayList.size(); i++) {
            int dDateNum = Integer.valueOf(arrayDataArrayList.get(i).getFoodDate()); //식품의 유통기한

           if ((dDateNum - 2) == todayNum) {
                Log.i("유통기한: ", String.valueOf(dDateNum));

                setNotifi(context, 123, R.drawable.main_logo, NotificationChannelCreate.class);
                sqLiteHelper.insertDataAlarm(
                        arrayDataArrayList.get(i).getFoodName(),
                        arrayDataArrayList.get(i).getFoodDate()
                );
            }
        }
    }

    private String doCurrentDate() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Log.i("오늘날짜", today);
        return today;
    }

    public static void setNotifi(Context context, int channelID, int drawableID, Class<?> cls) {
        //알림 화면의 옆 쪽에 보이게 될 큰 아이콘
        Bitmap mLargeIconForNoti =
                BitmapFactory.decodeResource(context.getResources(), drawableID);

        //create NotiChannel
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        //알림창을 누르면 MainActivity로 이동
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, cls), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(drawableID)
                        .setContentTitle("냉장GO") // required
                        .setContentText(name+"의 유통기한이 2일 남았습니다.") // required
                        .setDefaults(Notification.DEFAULT_VIBRATE) //알림 진동을 기본으로 설정
                        .setLargeIcon(mLargeIconForNoti)
                        .setContentIntent(mPendingIntent)
                        .setAutoCancel(true); //알림 터치시 반응 후 삭제

        Notification notification = mBuilder.build();
        notificationManager.notify(channelID , notification);
    }
}