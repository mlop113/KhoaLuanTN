package com.android;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.Effect.Typewriter;
import com.android.Global.AppConfig;
import com.android.Global.GlobalStaticData;
import com.android.Models.UserMember;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity {

    ArrayList<Integer> arrayImage;
    private RelativeLayout relativeLayout;
    private static int SPLASH_TIME_OUT = 1500;
    private Typewriter typewriterAppname;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        typewriterAppname = (Typewriter) findViewById(R.id.typewriterAppname);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        arrayImage = new ArrayList<>();
        //arrayImage.add(R.drawable.bg_splashscreen1);
        //arrayImage.add(R.drawable.bg_splashscreen2);
        arrayImage.add(R.drawable.bg_splashscreen3);
        Random random = new Random();
        int p = random.nextInt(arrayImage.size());

        relativeLayout.setBackgroundResource(arrayImage.get(p));

        String appname = getString(R.string.app_name);
        typewriterAppname.setCharacterDelay(100);
        typewriterAppname.animateText(appname);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        // intuser();
         //initDataInFireBase();
    }

    private void intuser() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference postsRef = ref.child("UserMembers");
        postsRef.push().setValue(new UserMember("03 Lê Văn Việt", "24-01-2107 14:20:00", "ngoc117@gmail.com", "https://www.facebook.com/photo.php?fbid=1928810227435906&set=pcb.1928813470768915&type=3", "ngoc3", "Phan Vu Xuan Ngoc", "12345", "0981418198", "Nam", postsRef.push().getKey().toString()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void initData() {
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child("-KyUeplvVdI5vI4ghU01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalStaticData.currentUser = dataSnapshot.getValue(UserMember.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //listPost home
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*for(DataSnapshot dataPost:dataSnapshot.getChildren()){
                    GlobalStaticData.listPostHome.add(dataPost.getValue(Post.class));
                }*/

               /* GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {
                };
                Map<String, Post> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                GlobalStaticData.listPostHome = new ArrayList<Post>(objectHashMap.values());*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //listCategory home
        databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataCategory : dataSnapshot.getChildren()) {
                    final String category = (String) dataCategory.getValue();
                    GlobalStaticData.listCategoryHome.add(category);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /*GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {
                            };
                            Map<String, Post> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                            List<Post> listPost = new ArrayList<Post>(objectHashMap.values());
                            GlobalStaticData.listPostOfCategory.put(category, listPost);*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //listBookmarks
        databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(GlobalStaticData.currentUser.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataBookmark : dataSnapshot.getChildren()) {
                    GlobalStaticData.listBookmark.add(dataBookmark.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //listTag
        databaseReference.child(AppConfig.FIREBASE_FIELD_TAGS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataTag : dataSnapshot.getChildren()) {
                   // GlobalStaticData.listTag.add(dataTag.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

