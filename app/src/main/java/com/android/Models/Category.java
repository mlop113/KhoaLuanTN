package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("CategoryID")
    private String categoryID;
    @SerializedName("Name")
    private String name;
    @SerializedName("Image")
    private String image;

    public Category(String categoryID, String name, String image) {
        this.categoryID = categoryID;
        this.name = name;
        this.image = image;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
