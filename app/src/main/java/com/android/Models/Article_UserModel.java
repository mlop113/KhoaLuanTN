package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Article_UserModel implements Serializable {
    @SerializedName("ArticleID")
    private String articleID;
    @SerializedName("UserID")
    private String userID;

    public Article_UserModel(String articleID, String userID) {
        this.articleID = articleID;
        this.userID = userID;
    }


}
