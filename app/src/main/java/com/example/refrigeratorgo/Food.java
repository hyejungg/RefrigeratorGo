package com.example.refrigeratorgo;

public class Food {
    private int index;
    private String name;
    private String date;
    private String category;
    private String memo;
    private String img;
    private String rfg_name;

    public Food() {
    }

    public Food(String name, String date, String memo, String img, String rfg_name) {
        this.name = name;
        this.date = date;
        this.memo = memo;
        this.img = img;
        this.rfg_name = rfg_name;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() { return img; }
    public void setImage(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRfgName() {
        return rfg_name;
    }
    public void setRfgName(String rfg_name) {
        this.rfg_name = rfg_name;
    }
}
