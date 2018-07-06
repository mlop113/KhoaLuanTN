package com.android;

import android.support.multidex.MultiDexApplication;

import com.android.API.APIFunction;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalStaticData;
import com.android.Models.User;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AppPreferences appPreferences = AppPreferences.getInstance(this);
        String userID = appPreferences.getUserId();
        User user = null;
        APIFunction apiFunction = APIFunction.getInstance();
        if (userID != null) {
            user = apiFunction.getUser_byID(userID);
        }
        GlobalStaticData.setCurrentUser(user);
    }

}
