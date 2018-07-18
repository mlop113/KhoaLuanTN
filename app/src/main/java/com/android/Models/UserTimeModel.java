package com.android.Models;

public class UserTimeModel {
    private String userID;
    private long timeLogin;
    private long timeLogout;

    public UserTimeModel(String userID, long timeLogin, long timeLogout) {
        this.userID = userID;
        this.timeLogin = timeLogin;
        this.timeLogout = timeLogout;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getTimeLogin() {
        return timeLogin;
    }

    public void setTimeLogin(long timeLogin) {
        this.timeLogin = timeLogin;
    }

    public long getTimeLogout() {
        return timeLogout;
    }

    public void setTimeLogout(long timeLogout) {
        this.timeLogout = timeLogout;
    }
}
