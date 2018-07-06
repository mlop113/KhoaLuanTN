package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportComment implements Serializable {
    @SerializedName("ReportID")
    private String reportID;
    @SerializedName("CommentID")
    private String commentID;
    @SerializedName("Content")
    private String content;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("UserID")
    private String userID;

    public ReportComment(String reportID, String commentID, String content, String dateCreate, String userID) {
        this.reportID = reportID;
        this.commentID = commentID;
        this.content = content;
        this.dateCreate = dateCreate;
        this.userID = userID;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
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
}
