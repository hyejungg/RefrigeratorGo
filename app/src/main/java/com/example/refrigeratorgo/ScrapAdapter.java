package com.example.refrigeratorgo;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//길게 눌렀을 때 리스트뷰를 삭제
public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.CustomViewHolder> {
    //리스트에 담을 배열
    private ArrayList<ScrapData> scrap_data;

    //생성자에서 데이터 리스트 객체를 전달받음.
    public ScrapAdapter(ArrayList<ScrapData> arrayList) {
        this.scrap_data = arrayList;
    }

    String fix_url = "http://www.10000recipe.com";

    //리사이클뷰가 생성
    @NonNull
    @Override
    public ScrapAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    //실제 각 뷰에 아이템이 추가될 때
    @Override
    public void onBindViewHolder(@NonNull final ScrapAdapter.CustomViewHolder holder, final int position) {
        holder.food_name.setText(scrap_data.get(position).getTitle());   //음식명
        holder.scrap.setText(scrap_data.get(position).getScrap());      //스크랩 취소버튼
        String detail_url = scrap_data.get(position).getRecipeAddr();   //레시피 상세링크
        final String final_url = fix_url +detail_url;                     //상세링크
        System.out.println("final_url : " + final_url);

        Glide.with(holder.itemView).load(scrap_data.get(position).getRecipeImage()).override(650,650).skipMemoryCache(true).into(holder.food_img); //음식 사진

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //뷰를 누르면 상세링크로 가도록
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(final_url));
                view.getContext().startActivity(intent);
            }
        });

        holder.scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스크랩 버튼을 누르면 삭제
                remove(holder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() { return (null!=scrap_data ? scrap_data.size() : 0); }

    public void remove(int position){
        try{
            //db에서 삭제
            new ScrapDeleteTask().execute(
                    scrap_data.get(position).getTitle(),
                    scrap_data.get(position).getRfgName());

            //arrayList에서 삭제
            scrap_data.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView food_name;
        protected ImageView food_img;
        protected TextView scrap;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.food_name = (TextView)itemView.findViewById(R.id.tv_text);
            this.food_img = (ImageView)itemView.findViewById(R.id.foodimg);
            this.scrap = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    //db에서 값을 삭제하는 내부클래스
    class ScrapDeleteTask extends AsyncTask<String, Void ,Void>{
        String url;

        @Override
        protected Void doInBackground(String... arg) {
            String title = arg[0];
            String rfg_name = arg[1];
            //파싱으로 가져올 웹페이지
            url = "http://zoooz0616.dothome.co.kr/ScrapDelete2.php?title=" + title +
                    "&rfgName=" + rfg_name;

            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuffer sb = new StringBuffer();

            try {
                URL php_url = new URL(url);//URL 객체 생성
                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection conn = (HttpURLConnection) php_url.openConnection();
                conn.setConnectTimeout(10000);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));

                    //한줄씩 읽어서 stringBuilder에 저장함
                    while (true) {
                        String line = br.readLine();
                        if (line == null) break;
                        sb.append(line + "\n");
                    }
                }
                System.out.println(sb.toString());
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}