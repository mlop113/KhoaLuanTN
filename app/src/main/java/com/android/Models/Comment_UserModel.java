package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment_UserModel implements Serializable {
    @SerializedName("CommentID")
    private String commentID;
    @SerializedName("UserID")
    private String userID;

    public Comment_UserModel(String commentID, String userID) {
        this.commentID = commentID;
        this.userID = userID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
