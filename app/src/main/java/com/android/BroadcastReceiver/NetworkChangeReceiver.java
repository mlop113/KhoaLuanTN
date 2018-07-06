package com.android.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.Global.AppConfig;
import com.android.Global.GlobalStaticData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private final static String TAG = "NetworkChangeReceiver";
    private static NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            Intent intentSendData = new Intent();
            intentSendData.setAction(AppConfig.BROADCAST_UPDATE_UI);
            intentSendData.putExtra(AppConfig.BROADCAST_NETWORK_AVAILABLE, isOnline(context));
            context.sendBroadcast(intentSendData);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline2(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isOnline(Context context) {
        if (isOnline2(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(GlobalStaticData.URL_HOST).openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("isNetworkAvailable", "Error checking internet connection", e);
            }
        } else {
            Log.d("isNetworkAvailable", "No network available!");
        }
        return false;
    }

    public static void register(Context context) {
        try{
            if (networkChangeReceiver == null) {
                networkChangeReceiver = new NetworkChangeReceiver();
                context.registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public static void unregister(Context context) {
        try{
            if (networkChangeReceiver != null) {
                context.unregisterReceiver(networkChangeReceiver);
                networkChangeReceiver = null;
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
