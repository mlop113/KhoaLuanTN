package com.android;

import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.android.API.APIFunction;
import com.android.DBHelper.DatabaseHelper;
import com.android.Global.GlobalFunction;
import com.android.Models.UserTimeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (GlobalFunction.isNetworkAvailable(this)) {
            new UpdatePoint().execute("update");
        }
    }

    class UpdatePoint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d("loguser", "doInBackground: ");
            DatabaseHelper databaseHelper = new DatabaseHelper(MyApplication.this);
            APIFunction apiFunction = APIFunction.getInstance();
            List<UserTimeModel> userTimeModels = databaseHelper.getListUserTime();
            if (userTimeModels != null && userTimeModels.size() > 0) {
                for (UserTimeModel userTimeModel : userTimeModels) {
                    Log.d("loguser", "doInBackground: userid= " + userTimeModel.getUserID());
                    apiFunction.updatePoint(userTimeModel.getUserID(),
                            GlobalFunction.calculatePoint(userTimeModel.getTimeLogin(), userTimeModel.getTimeLogout()));
                }
                databaseHelper.deleteAllUserTime();
            }
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                GlobalFunction.loginUser(MyApplication.this, firebaseUser.getUid(), new Date().getTime());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
