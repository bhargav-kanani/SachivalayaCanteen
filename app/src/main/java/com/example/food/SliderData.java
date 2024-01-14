package com.example.food;

public class SliderData {
    // image url is used to
    // store the url of image
    private String imgUrl;

    // Constructor method.
    public SliderData(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
