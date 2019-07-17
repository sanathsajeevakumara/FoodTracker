package com.example.foodtracker;

public class ApiItem {

    private String imageURL;
    private String nameOfTheFood;
    private String creator;
    private String url;
    private String rank;

    public ApiItem(String imageURL, String nameOfTheFood, String creator, String url, String rank) {
        this.imageURL = imageURL;
        this.nameOfTheFood = nameOfTheFood;
        this.creator = creator;
        this.url = url;
        this.rank = rank;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getNameOfTheFood() {
        return nameOfTheFood;
    }

    public String getCreator() {
        return creator;
    }

    public String getRank() {
        return rank;
    }

    public String getUrl() {
        return url;
    }

}
