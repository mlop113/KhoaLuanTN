package com.android.Models;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("NotificationID")
    private String notificationID;
    @SerializedName("ArticleID")
    private String articleID;
    @SerializedName("Title")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("CategoryID")
    private String categoryID;
    @SerializedName("DateBegin")
    private String dateBegin;
    @SerializedName("DateEnd")
    private String dateEnd;

    private int isPush;

    public NotificationModel(String notificationID, String articleID, String title, String description, String categoryID, String dateBegin, String dateEnd, int isPush) {
        this.notificationID = notificationID;
        this.articleID = articleID;
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.isPush = isPush;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getIsPush() {
        return isPush;
    }

    public void setIsPush(int isPush) {
        this.isPush = isPush;
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
}
