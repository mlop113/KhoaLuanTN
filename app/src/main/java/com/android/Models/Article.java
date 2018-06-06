package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Article implements Serializable{
    @SerializedName("ArticleID")
    private String articleID;
    @SerializedName("Title")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("Content")
    private String content;
    @SerializedName("CategoryID")
    private String categoryID;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("CoverImage")
    private String coverImage;
    @SerializedName("UserID")
    private String userID;

    public Article(String articleID, String title, String description, String content, String categoryID, String dateCreate, String coverImage, String userID) {
        this.articleID = articleID;
        this.title = title;
        this.description = description;
        this.content = content;
        this.categoryID = categoryID;
        this.dateCreate = dateCreate;
        this.coverImage = coverImage;
        this.userID = userID;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
