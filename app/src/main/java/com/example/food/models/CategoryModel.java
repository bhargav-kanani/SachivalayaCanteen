package com.example.food.models;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {

    @SerializedName("category")
    private String category;

    @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }
}
