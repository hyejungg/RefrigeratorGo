package com.example.refrigeratorgo;

import java.util.ArrayList;

//item의 TextView 2개와 ImageView 1개에 들어갈 data를 저장할 class.
public class ScrapData extends ArrayList<ScrapData> {

    private String title;
    private String recipe_addr;
    private String recipe_img;
    private String scrap; //스크랩 이름만 필요.
    private String rfg_name;

    public String getRfgName() {
        return rfg_name;
    }

    public void setRfgName(String rfg_name) {
        this.rfg_name = rfg_name;
    }

    public String getScrap() {
        return "스크랩 취소";
    }

    public void setScrap(String scrap) {
        this.scrap = "스크랩 취소";
    }

    public String getRecipeAddr() {
        return recipe_addr;
    }

    public void setRecipeAddr(String recipe_addr) {
        this.recipe_addr = recipe_addr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getRecipeImage() { return recipe_img; }

    public void setRecipeImage(String recipe_img) {
        this.recipe_img = recipe_img;
    }

    public ScrapData(String food_name, String detail_url, String img_url, String rfg_name) {
        this.title = food_name;
        this.recipe_addr = detail_url;
        this.recipe_img = img_url;
        this.rfg_name = rfg_name;
    }
}
