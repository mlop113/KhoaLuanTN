package com.android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportFeedbackComment implements Serializable {
    @SerializedName("ReportID")
    private String reportID;
    @SerializedName("FeedbackCommentID")
    private String feedbackCommentID;
    @SerializedName("Content")
    private String content;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("UserID")
    private String userID;

    public ReportFeedbackComment(String reportID, String feedbackCommentID, String content, String dateCreate, String userID) {
        this.reportID = reportID;
        this.feedbackCommentID = feedbackCommentID;
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

    public String getFeedbackCommentID() {
        return feedbackCommentID;
    }

    public void setFeedbackCommentID(String feedbackCommentID) {
        this.feedbackCommentID = feedbackCommentID;
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
