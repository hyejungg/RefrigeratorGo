package com.example.refrigeratorgo;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FoodListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Food> food_list;
    int food_index;

    public FoodListAdapter(Context context, int layout, ArrayList<Food> foodsList) {
        this.context = context;
        this.layout = layout;
        this.food_list = foodsList;
    }

    @Override
    public int getCount() {
        return food_list.size();
    }

    @Override
    public Object getItem(int position) {
        return food_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtdate, txtMemo;
    }

    //음식삭제
    public void remove(int position){
        try{
            //db에서 삭제
            food_index = food_list.get(position).getIndex();
            new FoodDeleteTask().execute(food_list.get(position).getCategory());
            System.out.println("FoodListAdapter_remove, rfgName : " + food_list.get(position).getRfgName() + food_list.get(position).getIndex());

            //arrayList에서 삭제
            food_list.remove(position);
            notifyDataSetChanged();
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public void update(int position, String food_name, String memo, String food_date){
        PHPRequest2 request = new PHPRequest2(); //**서버연결요청 & 데이터삽입 클래스 객체생성
        food_index = food_list.get(position).getIndex();
        String result = request.PHPtest(food_name, memo, food_date, food_index); //**db에 데이터 삽입 결과값 반환
        System.out.println("\nfoodListAdapter, foodIndex : " +food_index + food_name + memo + food_date);
        if("1".equals(result)){
            System.out.println("phptest 성공");
        }
        else{ //**데이터 삽입 실패
            System.out.println("phptest 실패");
        }
    }


    @Override
    public View getView(int position, View view, ViewGroup view_group) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView)row.findViewById(R.id.txtFood);
            holder.txtdate = (TextView)row.findViewById(R.id.txtdate);
            holder.imageView = (ImageView) row.findViewById(R.id.imgFood);
            holder.txtMemo = (TextView)row.findViewById(R.id.txtMemo);

            row.setTag(holder);
        }
        else
            holder = (ViewHolder)row.getTag();

        Food food2 = food_list.get(position);
        holder.txtName.setText(food2.getName());
        holder.txtdate.setText(food2.getDate());

        String foodImage = food2.getImage();

        //바코드 파싱 이미지인 경우 이미지 적용
        if(foodImage.contains("http"))
            Glide.with(context).load(foodImage).override(100,100).skipMemoryCache(true).into(holder.imageView);

        //갤러리 이미지인 경우 이미지 적용
        else
            holder.imageView.setImageResource(R.drawable.main_logo); //임시로 일단 메인로고 고정

        holder.txtMemo.setText(food2.getMemo());
        return row;
    }

    //db에서 값을 삭제하는 내부클래스
    class FoodDeleteTask extends AsyncTask<String, Void ,Void> {
        String target;

        @Override
        protected Void doInBackground(String... arg) {
            String category = arg[0];
            target = "http://zoooz0616.dothome.co.kr/DeleteFood.php?category=" + category +
                    "&foodIndex=" + food_index;
            System.out.println("foodListAdapter category : " + category +", foodIndex : "+ food_index);

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
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
        }
    }
}
