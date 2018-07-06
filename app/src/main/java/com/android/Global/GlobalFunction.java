package com.android.Global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.Models.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class GlobalFunction {
    static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static String calculateTimeAgo(String date)
    {

        Date today=new Date(System.currentTimeMillis());
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        Date datepost=new Date();
        Calendar calendar =Calendar.getInstance();
        //Log.d("currentdate",String.valueOf(timeFormat.format(today)));
        try {
            datepost = timeFormat.parse(date);
            //Log.d("datepost",String.valueOf(timeFormat.format(datepost)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long distance = (today.getTime()-datepost.getTime());
        long minute = TimeUnit.MINUTES.convert(distance, TimeUnit.MILLISECONDS);

        if(minute>=60)
        {
            long hour = TimeUnit.HOURS.convert(distance, TimeUnit.MILLISECONDS);
            if(hour>=24)
            {
                long day = TimeUnit.DAYS.convert(distance, TimeUnit.MILLISECONDS);
                if(day>=7)
                {
                    if(day>=30)
                    {
                        return String.valueOf(day / 30) + " tháng trước";
                    }
                    return String.valueOf(day / 7) + " tuần " + String.valueOf(day % 7) + " ngày trước";
                }
                return String.valueOf(day) +" ngày trước";
            }
            return String.valueOf(hour) +" giờ trước";

        }
        else if(minute<=1)
            return  "vừa xong";
        return  String.valueOf(minute) +" phút trước";
    }

    public static List<Post> getFilterListPostByDate(List<Post> listPost, String date){
        List<Post> listPostResult = new ArrayList<>();
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        Date dateFilter=new Date();
        Date datePost = new Date();
        try {
            dateFilter = timeFormat.parse(date);
            Log.d("dateFilter",dateFilter.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Post p:listPost) {
            try {
                datePost = timeFormat.parse(p.getDateCreate());
                Log.d("datePost",datePost.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long distance = (datePost.getTime()-dateFilter.getTime());
            Log.d("distance",String.valueOf(distance));
            if(distance>=0)
                listPostResult.add(p);
        }
        return  listPostResult;
    }

    public static boolean filter( Post p, String date){
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        Date dateFilter=new Date();
        Date datePost = new Date();
        try {
            dateFilter = timeFormat.parse(date);
            Log.d("dateFilter",dateFilter.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            datePost = timeFormat.parse(p.getDateCreate());
            Log.d("datePost",datePost.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long distance = (datePost.getTime()-dateFilter.getTime());
        Log.d("distance",String.valueOf(distance));
        if(distance>=0)
            return  true;

        return  false;
    }

    //function get list post by all
    /*public static List<Post> getListPost() {
        final List<Post> list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Post post =child.getValue(Post.class);
                    list.add(post);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static List<Tag> getListTag(){
        final List<Tag> list = new ArrayList<>();
        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(AppConfig.FIREBASE_FIELD_TAGS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Tag tag =child.getValue(Tag.class);
                    list.add(tag);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static List<Post> getListPostByTagName(String tagName){
        List<Post> listPost = new ArrayList<>();
        for (Post p:GlobalStaticData.getListPost()) {
            if(p.getTags().contains(tagName))
                listPost.add(p);
        }
        return listPost;
    }

    public static List<Category> getlistCategory(){
        final List<Category> list = new ArrayList<>();
        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Category category =child.getValue(Category.class);
                    list.add(category);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static UserMember LoginByPhoneNumber(String phonenumber, String password){
        UserMember user=null;
        return  user;
    }

    public static List<Post> getListPostByCategoryId(String categoryId){
        List<Post> listPost = new ArrayList<>();

        for (Post p:GlobalStaticData.getListPost()) {
                if(p.getCategoryId().equals(categoryId))
                    listPost.add(p);
        }
        return  listPost;
    }

    public static Category getCategoryById(String categoryId)
    {

        return null;
    }



    public static ArrayList<PostModel> ListBookmark = new ArrayList<>();

    public static ArrayList<PostModel> getListBookmark() {
        return ListBookmark;
    }

    public static void setListBookmark(ArrayList<PostModel> listBookmark) {
        ListBookmark = listBookmark;
    }

    public static ArrayList<PostModel> getListBookmarkByUserId(String userId){
        ArrayList<PostModel> listPost = new ArrayList<>();
        return listPost;
    }*/


    public static boolean isNetworkAvailable2(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailable(Context context) {
        if (isNetworkAvailable2(context)) {
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


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap getBitmapFromByteArray(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
