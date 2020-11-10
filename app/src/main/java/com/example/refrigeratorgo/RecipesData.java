package com.example.refrigeratorgo;

import java.util.ArrayList;

//item의 TextView 2개와 ImageView 1개에 들어갈 data를 저장할 class.
public class RecipesData extends ArrayList<RecipesData> {

    private String img_url;
    private String food_name;
    private String scrap;
    private String detail_url;
    private String rfg_name;

    public String getRfgName() {
        return rfg_name;
    }

    public void setRfgName(String rfgName) {
        this.rfg_name = rfgName;
    }

    public String getDetailUrl() {
        return detail_url;
    }

    public void setDetailUrl(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getFoodName() {
        return food_name;
    }

    public void setFoodName(String foodName) {
        this.food_name = foodName;
    }

    public String getImgUrl() { return img_url; }

    public void setImgUrl(String imgUrl) {
        this.img_url = imgUrl;
    }

    public String getScrap() {
        return scrap;
    }

    public void setScrap(String scrap) {
        this.scrap = scrap;
    }

    public RecipesData(String img_url, String food_name, String scrap, String detail_url, String rfg_name) {
        this.img_url = img_url;
        this.food_name = food_name;
        this.scrap = scrap;
        this.detail_url = detail_url;
        this.rfg_name = rfg_name;
    }
}
