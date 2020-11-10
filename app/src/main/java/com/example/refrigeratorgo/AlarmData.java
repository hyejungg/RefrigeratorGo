package com.example.refrigeratorgo;

import android.media.Image;

import java.io.Serializable;

public class AlarmData implements Serializable {
    private String foodName;
    private String foodDate;

    public AlarmData(String foodName, String foodDate) {
        this.foodName = foodName;
        this.foodDate = foodDate;
    }

    private int foodImage;

    public AlarmData(String foodName, String foodDate, int foodImage) {
        this.foodName = foodName;
        this.foodDate = foodDate;
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDate() {
        return foodDate;
    }

    public void setFoodDate(String foodDate) {
        this.foodDate = foodDate;
    }

    public int getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(int foodImage) {
        this.foodImage = foodImage;
    }
}