package com.android.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.API.APIFunction;
import com.android.DBHelper.DatabaseHelper;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Models.NotificationModel;
import com.android.R;
import com.android.SplashScreenActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PushNotificationService extends Service {
    private static final String TAG = "PushNotificationService";
    private Handler handler;
    private Socket mSocket;
    private APIFunction apiFunction;
    private DatabaseHelper databaseHelper;
    private AppPreferences appPreferences;
    private static final String PUSH_NOTIFICATION_CHANNEL = "PUSH_NOTIFICATION_CHANNEL";

    private NotificationManager notificationManager;
    //private MyBroadcastReceiver mBroadcastReceiver;
    private BroadcastReceiver updateNetworkReciver;

    // constant
    public static final long NOTIFY_INTERVAL = 5 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    {
        try {
            mSocket = IO.socket(GlobalStaticData.URL_HOST);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public PushNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        apiFunction = APIFunction.getInstance();
        databaseHelper = new DatabaseHelper(this);
        appPreferences = AppPreferences.getInstance(this);
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        mSocket.connect();
        mSocket.on("SERVER_PUSH_NOTIFICATION", onPushNotification);
        registerUpdateNetworkReciver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
// schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        mSocket.connect();
        mSocket.on("SERVER_PUSH_NOTIFICATION", onPushNotification);
        registerUpdateNetworkReciver();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void displayNotification(String articleID, String title, String description) {
        Log.d(TAG, "displayNotification: " + articleID);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(PUSH_NOTIFICATION_CHANNEL,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent(this,
                SplashScreenActivity.class);
        notificationIntent.putExtra(AppConfig.POST_ID, articleID);
        notificationIntent.setAction("com.android" + articleID);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashScreenActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_app)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(notificationPendingIntent)
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(PUSH_NOTIFICATION_CHANNEL);
        }

        builder.setAutoCancel(true);
        notificationManager.notify(Integer.parseInt(articleID.substring(4)), builder.build());
    }

    public void removeNotification(String notificationID) {
        Log.d(TAG, "removeNotification: " + notificationID);
        notificationManager.cancel(Integer.parseInt(notificationID));
    }

    private Emitter.Listener onPushNotification = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onPushNotification " + args[0]);

                    NotificationModel notificationModel = new Gson().fromJson(args[0].toString(), NotificationModel.class);

                    Log.d(TAG, "onPushNotification: " + notificationModel.getNotificationID());

                    if (!databaseHelper.checkNotificationExist(notificationModel.getNotificationID())) {
                        databaseHelper.insertNotification(notificationModel);
                    }
                }
            });
        }
    };

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void startService(Context context) {
        try {
            Log.d(TAG, "startService ");
            Intent intent = new Intent(context, PushNotificationService.class);
            context.startService(intent);
        } catch (IllegalArgumentException e) {

        }
    }

    public static void stopService(Context context) {
        try {
            Log.d(TAG, "stopService ");
            Intent intent = new Intent(context, PushNotificationService.class);
            context.stopService(intent);
        } catch (IllegalArgumentException e) {

        }
    }

    private void registerUpdateNetworkReciver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConfig.BROADCAST_UPDATE_UI);
            updateNetworkReciver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        boolean isNetworkAvailable = intent.getBooleanExtra(AppConfig.BROADCAST_NETWORK_AVAILABLE, true);
                        if (isNetworkAvailable) {
                            List<NotificationModel> listNotifi = apiFunction.getListNotification();
                            if (listNotifi != null && listNotifi.size() > 0) {
                                Log.d(TAG, "listNotifi: " + listNotifi.size());
                                for (NotificationModel notifi : listNotifi) {
                                    if (!databaseHelper.checkNotificationExist(notifi.getNotificationID())) {
                                        databaseHelper.insertNotification(notifi);
                                    }
                                }
                            }
                        }
                    }
                }
            };
            registerReceiver(updateNetworkReciver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void unregisterUpdateNetworkReciver() {
        try {
            unregisterReceiver(updateNetworkReciver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                   /* Toast.makeText(PushNotificationService.this, getDateTime(),
                            Toast.LENGTH_SHORT).show();*/
                    try {
                        List<NotificationModel> listNotifi = databaseHelper.getListNotification();
                        if (listNotifi != null && listNotifi.size() > 0) {
                            Log.d(TAG, "listNotifi: " + listNotifi.size());
                            for (NotificationModel notifi : listNotifi) {
                                Log.d(TAG, "time distance: " + GlobalFunction.calculateDistance(notifi.getDateBegin()));
                                if (GlobalFunction.calculateDistance(notifi.getDateBegin()) >= 0) {
                                    Log.d(TAG, "run: 1");
                                    displayNotification(notifi.getArticleID(), notifi.getTitle(), notifi.getDescription());
                                    databaseHelper.updateNotication(notifi.getNotificationID());
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }
}
