package com.android.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.Models.Article;
import com.android.Models.NotificationModel;
import com.android.Models.UserTimeModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";
    public static final String DBNAME = "footballnews.sqlite";
    public static final String DBLOCATION = "data/data/com.android/databases/";
    private SQLiteDatabase mDatabase;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;

        File database = context.getDatabasePath(DatabaseHelper.DBNAME);
        if (!database.exists()) {
            this.getReadableDatabase();
            if (copyDatabase(context)) {
                Log.d(TAG, "DatabaseHelper: copy database succes");
            } else {
                Log.d(TAG, "DatabaseHelper: copy database fail");
            }
        }
        /*this.getReadableDatabase();
        if(copyDatabase(context)){
            Toast.makeText(context, "copy database succes", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "copy database fail", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buuf = new byte[1024];
            int legnth = 0;
            while ((legnth = inputStream.read(buuf)) > 0) {
                outputStream.write(buuf, 0, legnth);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void openDatabase() {
        String dbPath = this.context.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    Cursor cursor;

    public List<Article> getListArticle() {
        Log.d(TAG, "getListArticle: ");
        List<Article> listArticle = new ArrayList<>();
        Article article = null;
        openDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM Article", null);
        while (cursor.moveToNext()) {
            article = new Article(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getBlob(9));
            listArticle.add(article);
        }
        cursor.close();
        closeDatabase();
        return listArticle;
    }

    public boolean insertArticle(Article newArticle) {
        Log.d(TAG, "insertArticle: ");
        cursor = null;
        try {
            openDatabase();
            String sql = "INSERT INTO Article VALUES(?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindString(1, newArticle.getArticleID());
            insertStmt.bindString(2, newArticle.getTitle());
            insertStmt.bindString(3, newArticle.getDescription());
            insertStmt.bindString(4, newArticle.getContent());
            insertStmt.bindString(5, newArticle.getCategoryID());
            insertStmt.bindString(6, newArticle.getDateCreate());
            insertStmt.bindString(7, newArticle.getCoverImage());
            insertStmt.bindString(8, newArticle.getUserID());
            insertStmt.bindString(9, newArticle.getContentOffLine());
            insertStmt.bindBlob(10, newArticle.getCoverImageOffLine());
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }


    public boolean deleteAllArticle() {
        Log.d(TAG, "deleteAllArticle: ");
        cursor = null;
        try {
            openDatabase();
            String sql = "delete from Article";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }

    public boolean checkUserExist(String userID) {
        Log.d(TAG, "getListUserTime: ");
        UserTimeModel userTimeModel = null;
        openDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM UserTime WHERE userID = '" + userID + "'", null);
        while (cursor.moveToNext()) {
            userTimeModel = new UserTimeModel(cursor.getString(0), cursor.getLong(1), cursor.getLong(2));
        }
        cursor.close();
        closeDatabase();
        return userTimeModel != null;
    }

    public boolean loginUser(String userID, long timeLogin) {
        Log.d("loguser", "loginUser: " + timeLogin);
        cursor = null;
        try {
            openDatabase();
            if (checkUserExist(userID)) {

            } else {
                String sql = "INSERT INTO UserTime VALUES(?,?,?)";
                SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
                insertStmt.clearBindings();
                insertStmt.bindString(1, userID);
                insertStmt.bindLong(2, timeLogin);
                insertStmt.bindLong(3, 0);
                insertStmt.execute();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }

    public boolean logoutUser(String userID, long timeLogout) {
        Log.d("loguser", "logoutUser: " + timeLogout);
        cursor = null;
        try {
            openDatabase();
            String sql = "UPDATE UserTime SET timeLogout=? WHERE userID = ?";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindLong(1, timeLogout);
            insertStmt.bindString(2, userID);
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }


    public List<UserTimeModel> getListUserTime() {
        Log.d(TAG, "getListUserTime: ");
        List<UserTimeModel> listUserTime = new ArrayList<>();
        UserTimeModel userTimeModel;
        openDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM UserTime", null);
        while (cursor.moveToNext()) {
            userTimeModel = new UserTimeModel(cursor.getString(0), cursor.getLong(1), cursor.getLong(2));
            listUserTime.add(userTimeModel);
        }
        cursor.close();
        closeDatabase();
        return listUserTime;
    }

    public boolean deleteAllUserTime() {
        Log.d(TAG, "deleteAllUserTime: ");
        cursor = null;
        try {
            openDatabase();
            String sql = "delete from UserTime";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }

    public List<NotificationModel> getListNotification() {
        Log.d(TAG, "getListNotification: ");
        List<NotificationModel> listNotifi = new ArrayList<>();
        NotificationModel notifi = null;
        openDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM Notification WHERE push = 0 AND categoryID in (SELECT * FROM CategoryNotification)", null);
        while (cursor.moveToNext()) {
            notifi = new NotificationModel(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6), cursor.getInt(7));
            listNotifi.add(notifi);
        }
        cursor.close();
        closeDatabase();
        return listNotifi;
    }

    public boolean insertNotification(NotificationModel newNotifi) {
        Log.d(TAG, "insertNotification: " + newNotifi.getNotificationID());
        cursor = null;
        try {
            openDatabase();
            String sql = "INSERT INTO Notification VALUES(?,?,?,?,?,?,?,?)";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindString(1, newNotifi.getNotificationID());
            insertStmt.bindString(2, newNotifi.getArticleID());
            insertStmt.bindString(3, newNotifi.getTitle());
            insertStmt.bindString(4, newNotifi.getDescription());
            insertStmt.bindString(5, newNotifi.getCategoryID());
            insertStmt.bindString(6, newNotifi.getDateBegin());
            insertStmt.bindString(7, newNotifi.getDateEnd());
            insertStmt.bindLong(8, newNotifi.getIsPush());
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }

    public boolean updateNotication(String notificationID) {
        Log.d(TAG, "updateNotication: " + notificationID);
        cursor = null;
        try {
            openDatabase();
            String sql = "Update Notification set push = 1 where notificationID = ?";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindString(1, notificationID);
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }

    public boolean deleteNotication(String notificationID) {
        Log.d(TAG, "deleteNotication: " + notificationID);
        cursor = null;
        try {
            openDatabase();
            String sql = "delete from Notification where notificationID=?";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.bindString(1, notificationID);
            insertStmt.clearBindings();
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }

    public boolean checkNotificationExist(String notificationID) {
        Log.d(TAG, "checkNotificationExist: " + notificationID);
        NotificationModel notificationModel = null;
        openDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM Notification WHERE notificationID = '" + notificationID + "'", null);
        while (cursor.moveToNext()) {
            notificationModel = new NotificationModel(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6), cursor.getInt(7));
        }
        cursor.close();
        closeDatabase();
        return notificationModel != null;
    }

    public List<String> getListCategoryNotification() {
        Log.d(TAG, "getListCategoryNotification: ");
        List<String> listCateNotifi = new ArrayList<>();
        openDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM CategoryNotification", null);
        while (cursor.moveToNext()) {
            listCateNotifi.add(cursor.getString(0));
        }
        cursor.close();
        closeDatabase();
        return listCateNotifi;
    }

    public boolean insertCategoryNotification(String categoryID) {
        Log.d(TAG, "insertCategoryNotification: " + categoryID);
        cursor = null;
        try {
            openDatabase();
            String sql = "INSERT INTO CategoryNotification VALUES(?)";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindString(1, categoryID);
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }


    public boolean deleteAllCategoryNotification() {
        Log.d(TAG, "deleteCategoryNotification: ");
        cursor = null;
        try {
            openDatabase();
            String sql = "delete from CategoryNotification";
            SQLiteStatement insertStmt = getWritableDatabase().compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            closeDatabase();
        }
    }
}
