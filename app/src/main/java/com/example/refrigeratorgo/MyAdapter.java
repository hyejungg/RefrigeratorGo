package com.example.refrigeratorgo;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//길게 눌렀을 때 리스트뷰 삭제
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private ArrayList<RecipesData> recipe_list; //리스트에 담을 배열
    public MyAdapter(ArrayList<RecipesData> array_list) {
        this.recipe_list = array_list;
    }
    String cur_name, rfg_name, scrap_btn, detail_url, img_url, final_url;
    RecipesData data;

    //리사이클뷰 생성시 작동하는 함수
    @NonNull
    @Override
    public MyAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.CustomViewHolder holder, final int position) {
        holder.food_name.setText(recipe_list.get(position).getFoodName());
        Glide.with(holder.itemView).load(recipe_list.get(position).getImgUrl()).override(650,650).skipMemoryCache(true).into(holder.food_img);
        holder.scrap.setText(recipe_list.get(position).getScrap());
        holder.itemView.setTag(position);

        //스크랩 버튼 클릭
        holder.scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                cur_name = holder.food_name.getText().toString();
                scrap_btn = "스크랩 취소";
                detail_url = recipe_list.get(position).getDetailUrl();
                img_url = recipe_list.get(position).getImgUrl();
                rfg_name = recipe_list.get(position).getRfgName();

                System.out.println("Myadapter, rfgName: " + rfg_name);
                Log.i(this.getClass().getName(), "클릭성공");
                Log.i(String.valueOf(bundle), "클릭성공");

                //Scrap에 데이터 추가.
                new ScrapInsertTask().execute(img_url, cur_name, detail_url, rfg_name);

                RecipesFragment4 fragment4 = new RecipesFragment4();
                fragment4.setArguments(bundle);
                ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment4).commit();
                Log.i(this.getClass().getName(), "fragment 이동 성공");
            }
        });

        //레시피 상세 링크로 이동
        holder.food_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_url = recipe_list.get(position).getDetailUrl();
                final_url = "http://www.10000recipe.com"+detail_url;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(final_url));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return (null!= recipe_list ? recipe_list.size() : 0); }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView food_img;
        protected TextView food_name;
        protected TextView scrap;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.food_img = (ImageView)itemView.findViewById(R.id.img_profile);
            this.food_name = (TextView)itemView.findViewById(R.id.tv_text);
            this.scrap = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    //내부클래스로 생성. SCRAP에 데이터 삽입, 삭제, 보여주기
    class ScrapInsertTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String recipe_img = arg0[0];
                String title = arg0[1];
                String recipe_url = arg0[2];
                String rfg_name = arg0[3];

                //데이터 삽입하는 코드
                String post_data = "recipeImage=" + recipe_img + "&title=" + title + "&recipeAddr=" + recipe_url +
                        "&rfgName=" + rfg_name; //전송데이터
                System.out.println("MyAdapter, rfg_name : " + rfg_name);
                String url = "http://zoooz0616.dothome.co.kr/ScrapInsert.php";
                URL php_url = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) php_url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream os = conn.getOutputStream();
                os.write(post_data.getBytes("UTF-8"));
                os.flush(); //outputstream 내부의 작은 버퍼. 데이터 출력 전 쌓아뒀다가 순서대로 출력.
                os.close();
                String result = readStream(conn.getInputStream());
                conn.disconnect();
                System.out.println("ScrapInsert : " + result);
                return result; // success : 1, fail : -1
            } catch (Exception e) {
                Log.i("RequestCART", "request was failed.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        public String readStream(InputStream is) throws IOException { //** 수정 필요없이 그대로 복붙하면 됨
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = " ";

            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();
            return sb.toString();
        }
    }
}
