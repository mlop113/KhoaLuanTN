package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("UserID")
    private String userID;
    @SerializedName("Email")
    private String email;
    @SerializedName("FullName")
    private String fullName;
    @SerializedName("Image")
    private String image;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("User_LevelID")
    private String user_LevelID;

    public User(String userID, String email, String fullName, String image, String dateCreate, String user_LevelID) {
        this.userID = userID;
        this.email = email;
        this.fullName = fullName;
        this.image = image;
        this.dateCreate = dateCreate;
        this.user_LevelID = user_LevelID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUser_LevelID() {
        return user_LevelID;
    }

    public void setUser_LevelID(String user_LevelID) {
        this.user_LevelID = user_LevelID;
    }
}
