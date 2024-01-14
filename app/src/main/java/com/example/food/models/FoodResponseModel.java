package com.example.food.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodResponseModel {

   @SerializedName("food")
    private List<FoodModel> books;

    public List<FoodModel> getBooks() {
        return books;
    }
}
