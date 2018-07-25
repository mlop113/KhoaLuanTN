package com.android.Global;

import com.android.Models.User;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class GlobalStaticData {
    static int currentPage=0;
    public static String URL_HOST = "http://192.168.1.11:3000/";
    public static User currentUser  = null;

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(int currentPage) {
        GlobalStaticData.currentPage = currentPage;
    }

    public static void setCurrentUser(User currentUser) {
        GlobalStaticData.currentUser = currentUser;
    }
}
