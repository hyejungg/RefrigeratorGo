package com.example.refrigeratorgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class RecipesInside extends Fragment {
    private ArrayList<RecipesData> recipe_list;
    private MyAdapter main_adapter;
    private RecyclerView recycler_view;
    private LinearLayoutManager linear_layout_manager; //recycleView에서 사용하는 친구
    private View view;
    String url, rfg_name; //전역변수로 바꿈

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments(); //번들 받기
        if(bundle != null){
            String btn_id = bundle.getString("btnId"); // 전달한 key 값
            initUrl(btn_id); //bcode_url 초기화
        }

        view = inflater.inflate(R.layout.recipes_inside, null);
        recycler_view = (RecyclerView) view.findViewById(R.id.rv);
        linear_layout_manager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(linear_layout_manager);

        init(); // + 냉장고 이름 초기화, 번들 -> 쉐어드프리퍼런스로 수정

        new Description().execute(); //AsyncTask 작동(파싱) - (파싱은 네트워크 작업을 해야하기 때문에 AsyncTask 이용)

        return view;
    }

    public void initUrl(String btnId) {
        switch (btnId){
            case "r_btn_sidedish" :
                url = "https://www.10000recipe.com/recipe/list.html?cat4=56&order=reco&page=";
                break;
            case "r_btn_soup" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=54&order=reco&page=";
                break;
            case "r_btn_rice" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=52&order=reco&page=";
                break;

            case "r_btn_noodles" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=53&order=reco&page=";
                break;
            case "r_btn_pizza" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=65&order=reco&page=";
                break;
            case "r_btn_baking" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=66&order=reco&page=";
                break;

            case "r_btn_dessert" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=60&order=reco&page=";
                break;
            case "r_btn_jam" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=58&order=reco&page=";
                break;
            case "r_btn_tea" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=59&order=reco&page="; //프레그먼트1 끝!
                break;

            case "r_btn_daily" :
                url="https://www.10000recipe.com/recipe/list.html?cat2=12&order=reco&page=";
                break;
            case "r_btn_night" :
                url="https://www.10000recipe.com/recipe/list.html?cat2=45&order=reco&page=";
                break;
            case "r_btn_diet" :
                url="https://www.10000recipe.com/recipe/list.html?cat2=21&order=reco&page=";
                break;

            case "r_btn_guest" :
                url="https://www.10000recipe.com/recipe/list.html?cat2=13&order=reco&page=";
                break;
            case "r_btn_health" :
                url="https://www.10000recipe.com/recipe/list.html?cat2=43&order=reco&page=";
                break;
            case "r_btn_simple" :
                url="https://www.10000recipe.com/recipe/list.html?cat2=18&order=reco&page=";
                break;
            case "r_btn_dessert2" :
                url="https://www.10000recipe.com/recipe/list.html?cat4=60&order=reco&page="; //프레그먼트2 끝!
                break;

            case "r_btn_oven" :
                url="https://www.10000recipe.com/recipe/list.html?q=%EC%98%A4%EB%B8%90&order=reco&page=";
                break;
            case "r_btn_microwave" :
                url="https://www.10000recipe.com/recipe/list.html?q=%EC%A0%84%EC%9E%90%EB%A0%88%EC%9D%B8%EC%A7%80&order=reco&page=";
                break;
            case "r_btn_air" :
                url="https://www.10000recipe.com/recipe/list.html?q=%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4&order=reco&page=";
                break;
            case "r_btn_Nofire" :
                url="https://www.10000recipe.com/recipe/list.html?q=%EB%B6%88%20%EC%82%AC%EC%9A%A9&order=reco&page="; //프레그먼트3 끝!
                break;
        }
    }

    private void init(){
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.user_info_file), Context.MODE_PRIVATE); //프레그먼트에서는 쉐어드프리퍼런스 사용시 string.xml사용
        String defaultValue = getResources().getString(R.string.default_value);
        rfg_name = pref.getString(getString(R.string.rfgname), defaultValue);

        System.out.println("RecipeInside, rfgName : " + rfg_name); // 기존 코드로 했을때 널값나와서 바꿨는데 내파일이 잘못된걸 수도 있으니까 기존에 혜정이꺼 한번 확인해주세요!

        recipe_list = new ArrayList<>();
        main_adapter = new MyAdapter(recipe_list);
        recycler_view.setAdapter(main_adapter);
    }

    //반찬 파싱
    //  밑에코드  https://forteleaf.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-HTML-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0-JSOUP 참고함
    private class Description extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                for(int i = 1; i <= 10; i++) { //i=1부터 임을 주의!! 영진 추가
                    url += i;
                    Document document = Jsoup.connect(url).get();
                    //필요한거만 선택하여 지정(li)
                    Elements element_size = ((Document) document).select("div.col-xs-3").select("a");

                    for (Element elem : element_size) {
                        String food_name = elem.select("div.caption h4").text();
                        String img_url = elem.select("img[style=width:275px; height:275px;]").attr("src");
                        String detail_url = elem.attr("href");
                        String scrap = "스크랩";
                        System.out.println("recipesInside, rfgName ------ : "+ rfg_name);
                        recipe_list.add(new RecipesData(img_url, food_name, scrap, detail_url, rfg_name));
                        Log.i(this.getClass().getName(), detail_url);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                Log.w("예외발생", "catch문 읽음");
            }

            return null;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(Void result) {
            //doInBackground 작업이 끝나고 난뒤의 작업

            main_adapter.notifyDataSetChanged(); //새로고침 완료 ㅇㅇ add 나 remove 이후엔 이 거 꼭 해줘야 새로고침 정상 됨 ㅇㅇ
            main_adapter = new MyAdapter(recipe_list);
            linear_layout_manager = new LinearLayoutManager(getContext());
            linear_layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
            recycler_view.setLayoutManager(linear_layout_manager);
            recycler_view.setAdapter(main_adapter);
        }

    }

}