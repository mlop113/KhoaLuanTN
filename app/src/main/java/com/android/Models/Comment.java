package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Comment implements Serializable
{
    @SerializedName("CommentID")
    private String commentID;
    @SerializedName("Content")
    private String content;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("UserID")
    private String userID;
    @SerializedName("ArticleID")
    private String articleID;

    public Comment(String commentID, String content, String dateCreate, String userID, String articleID) {
        this.commentID = commentID;
        this.content = content;
        this.dateCreate = dateCreate;
        this.userID = userID;
        this.articleID = articleID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }
}
