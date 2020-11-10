package com.example.refrigeratorgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//스크랩화면
public class RecipesFragment4 extends Fragment {

    ArrayList<ScrapData> scrap_list;
    RecyclerView recycler_view;
    ScrapAdapter main_adapter = null;
    LinearLayoutManager linear_layout_manager; //recycleView에서 사용하는 친구

    Bundle bundle;
    String rfg_name;
    View view;
    Context context;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment4_recipes,null);
        recycler_view = (RecyclerView) view.findViewById(R.id.reView);

        init();
        //추가된 레시피DB를 보여줘
        new ScrapSelectTask().execute();

        return view;
    }
    private void init(){
        scrap_list = new ArrayList<>();
        main_adapter = new ScrapAdapter(scrap_list);
        recycler_view.setAdapter(main_adapter);
        context = getContext();

        // 냉장고 이름 초기화, 번들 -> 쉐어드프리퍼런스(엑테비티 아닌 프레그먼트 등 전용프리퍼런스)로 수정
        SharedPreferences rfgFile = getActivity().getSharedPreferences(getString(R.string.user_info_file), Context.MODE_PRIVATE); //프레그먼트에서는 쉐어드프리퍼런스 사용시 string.xml사용
        String defaultValue = getResources().getString(R.string.default_value);
        rfg_name = rfgFile.getString(getString(R.string.rfgname), defaultValue);
        System.out.println("RecipesFragment4, rfg_name : " + rfg_name);

    }
    //db에 scrap 내용을 읽어오는 내부클래스.
    class ScrapSelectTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //파싱으로 가져올 웹페이지
            target = "http://zoooz0616.dothome.co.kr/ScrapSelect.php?rfgName=" + rfg_name;
        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuffer sb = new StringBuffer();

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));

                    //한줄씩 읽어서 stringBuilder에 저장함
                    while (true) {
                        String stringLine = br.readLine();
                        if (stringLine == null) break;
                        sb.append(stringLine + "\n");
                    }
                }
                System.out.println(sb.toString());
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
        //파싱해서 arrayList에 넣은 값을 adpter랑 연결
        @SuppressLint("WrongConstant") //노란줄 때문에
        @Override
        protected void onPostExecute(String result) {
            main_adapter.notifyDataSetChanged(); //새로고침 완료. add 나 remove 이후엔 꼭 해줘야 새로고침 정상 됨.
            main_adapter = new ScrapAdapter(scrap_list);
            linear_layout_manager = new LinearLayoutManager(getContext());
            linear_layout_manager.setOrientation(linear_layout_manager.VERTICAL);
            recycler_view.setLayoutManager(linear_layout_manager);
            recycler_view.setAdapter(main_adapter);
        }
    }
    //db에 있는 값 가져오는 내부클래스.
    private void parsing(String data) {
        try {
            JSONObject jsonobject1 = new JSONObject(data);
            JSONArray jsonArray = new JSONArray(jsonobject1.getString("response"));

            //arrayList 클리어
            if(scrap_list != null){
                scrap_list.clear();
            }

            //웹서버에서 가져온 값을 전달 받아서 arrayList에 담기
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject2 = (JSONObject) jsonArray.get(i);
                String recipe_img = jsonobject2.getString("recipeImage"); //레시피 이미지
                System.out.println(jsonobject2.getString("recipeImage"));
                String title = jsonobject2.getString("title"); //레시피 명
                System.out.println(jsonobject2.getString("title"));
                String recipe_addr = jsonobject2.getString("recipeAddr"); //레시피 상세링크
                System.out.println(jsonobject2.getString("recipeAddr"));
                String rfg_name = jsonobject2.getString("rfgName"); //냉장고이름
                System.out.println(jsonobject2.getString("rfgName"));
                scrap_list.add(new ScrapData(title, recipe_addr, recipe_img, rfg_name));
            }
            System.out.println("scraps : " + scrap_list); //****잘 출력되지만, 이상하게 null값.....
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}