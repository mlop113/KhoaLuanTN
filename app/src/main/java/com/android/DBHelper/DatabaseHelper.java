package com.android.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.android.Models.Article;

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
                Toast.makeText(context, "copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "copy database fail", Toast.LENGTH_SHORT).show();
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


}
