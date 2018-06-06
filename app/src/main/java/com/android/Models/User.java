package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("UserID")
    private String userID;
    @SerializedName("Email")
    private String email;
    @SerializedName("PassWord")
    private String passWord;
    @SerializedName("FullName")
    private String fullName;
    @SerializedName("Image")
    private String image;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("User_LevelID")
    private String User_LevelID;
    @SerializedName("Note")
    private String note;
    @SerializedName("Status")
    private String status;

    public User(String userID, String email, String passWord, String fullName, String image, String dateCreate, String user_LevelID, String note, String status) {
        this.userID = userID;
        this.email = email;
        this.passWord = passWord;
        this.fullName = fullName;
        this.image = image;
        this.dateCreate = dateCreate;
        User_LevelID = user_LevelID;
        this.note = note;
        this.status = status;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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
        return User_LevelID;
    }

    public void setUser_LevelID(String user_LevelID) {
        User_LevelID = user_LevelID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
