package com.android.Activity_Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.API.APIFunction;
import com.android.API.ResponseModel;
import com.android.Adapters.PostDetailAdapter;
import com.android.BroadcastReceiver.NetworkChangeReceiver;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Models.Article;
import com.android.Models.Comment;
import com.android.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = this.getClass().getName();
    Handler handler;
    AppPreferences appPreferences;
    InputMethodManager inputMethodManager;
    //
    SwipeRefreshLayout swiperefresh;
    //animation
    Animation animlike;
    Animation animshake;
    //back
    LinearLayout linearLayoutBack;
    //bookmark
    LinearLayout linearLayoutBookmark;

    //data Post
    RecyclerView recyclerViewPostDetail;
    PostDetailAdapter postDetailAdapter;
    Intent intent;
    Article article;

    //bottom
    LinearLayout linearLayoutLike;
    ImageView imageViewLike;
    EditText editTextComment;
    LinearLayout linearLayoutSend;
    ImageView imageViewSend;

    DatabaseReference databaseReference;

    //dialog wait
    SpotsDialog progressDialog;
    //progressbass load more
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager linearLayoutManager;
    //socket
    private final String CLIENT_SEND_GETPOST = "CLIENT_SEND_GETPOST";
    private final String SERVER_SEND_GETPOST = "SERVER_SEND_GETPOST";
    private Socket mSocket;
    Emitter.Listener onGetPost;

    {
        try {
            mSocket = IO.socket(GlobalStaticData.URL_HOST);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        onGetPost = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handleGetPost(args);
            }
        };
    }

    APIFunction apiFunction;
    //
    TextView tvPostDetailStatus;
    BroadcastReceiver updateNetworkReciver;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        progressDialog = new SpotsDialog(this, R.style.CustomAlertDialog);

        initAdmob();

        appPreferences = AppPreferences.getInstance(this);
        //connect firebase
        //fireBase();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        overridePendingTransitionEnter();
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //animation
        animlike = AnimationUtils.loadAnimation(this, R.anim.animlike);
        animshake = AnimationUtils.loadAnimation(this, R.anim.animshake);

        intent = getIntent();
        if (intent != null) {
            article = (Article) intent.getSerializableExtra(AppConfig.POST);
            if (article == null) {
                Toast.makeText(this, "Error data transfer", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error data transfer", Toast.LENGTH_SHORT).show();
            finish();
        }

        apiFunction = APIFunction.getInstance();
        handler = new Handler();

        mapping();
        initView();
        event();

        //SOCKET
        initSocket();
        addEvent();
        updateNetWork(GlobalFunction.isNetworkAvailable(this));
        NetworkChangeReceiver.register(this);
        registerUpdateNetworkReciver();
    }

    private void initAdmob() {
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
        mAdView = findViewById(R.id.adView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mAdView.setVisibility(View.GONE);
            }
        });

        loadAdmob();
    }

    private void loadAdmob() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void updateAdmobNetwork(boolean isAvailable) {
        if (isAvailable) {
            mAdView.setVisibility(View.VISIBLE);
            loadAdmob();
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    private void mapping() {
        swiperefresh = findViewById(R.id.swipp_refresh);
        //back
        linearLayoutBack = (LinearLayout) findViewById(R.id.linearLayoutBack);
        //bookmark
        linearLayoutBookmark = (LinearLayout) findViewById(R.id.linearLayoutBookmark);
        //status
        tvPostDetailStatus = findViewById(R.id.tv_post_detail_status);

        //recyclerView contant content,tag,related and comment
        recyclerViewPostDetail = (RecyclerView) findViewById(R.id.recyclerViewPostDetail);

        //bottom
        linearLayoutLike = (LinearLayout) findViewById(R.id.linearLayoutLike);
        imageViewLike = (ImageView) findViewById(R.id.imageViewLike);
        editTextComment = (EditText) findViewById(R.id.editTextComment);
        linearLayoutSend = (LinearLayout) findViewById(R.id.linearLayoutSend);
        imageViewSend = (ImageView) findViewById(R.id.imageViewSend);
    }

    private void initView() {

        swiperefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postDetailAdapter.notifyDataSetChanged();
                swiperefresh.setRefreshing(false);
            }
        });

        postDetailAdapter = new PostDetailAdapter(PostDetailActivity.this, article);
        recyclerViewPostDetail.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));
        linearLayoutManager = (LinearLayoutManager) recyclerViewPostDetail.getLayoutManager();
        recyclerViewPostDetail.setAdapter(postDetailAdapter);

        checkLiked(imageViewLike);

    }

    private void event() {
        //back
        linearLayoutBack.setOnClickListener(this);
        //bookmark
        linearLayoutBookmark.setOnClickListener(this);
        //bottom
        linearLayoutLike.setOnClickListener(this);
        linearLayoutSend.setOnClickListener(this);
        linearLayoutManager = (LinearLayoutManager) recyclerViewPostDetail.getLayoutManager();
        recyclerViewPostDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!isLoading && GlobalFunction.isNetworkAvailable(PostDetailActivity.this)
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //postDetailAdapter.updateProgressBarLoadMore(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onScrolled: ");
                            List<Comment> comments = new ArrayList<>();
                            comments.add(new Comment("1", "1", "1", "1", "1"));
                            comments.add(new Comment("1", "1", "1", "1", "1"));
                            postDetailAdapter.loadMore(comments);
                            //postDetailAdapter.updateProgressBarLoadMore(false);
                            isLoading = false;
                        }
                    }, 2000);
                    isLoading = true;
                }
            }
        });
    }

    private void checkAction() {
        if (intent != null) {
            if (intent.getStringExtra(AppConfig.ACTION) != null) {
                String action = intent.getStringExtra(AppConfig.ACTION);
                if (action.equals(AppConfig.COMMENT)) {
                    editTextComment.requestFocus();
                    inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        } else {
            inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayoutBack:
                finish();
                break;
            case R.id.linearLayoutBookmark:
                onClickBookMark();
                break;
            //bottom
            case R.id.linearLayoutLike:
                onClickLikePost(imageViewLike);
                break;
            case R.id.linearLayoutSend:
                if (GlobalFunction.isNetworkAvailable(this)) {
                    long time = new java.util.Date().getTime();
                    String commentID = Long.toString(time);
                    Date myDate = new Date();
                    String content = editTextComment.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        Comment comment = new Comment(String.valueOf(myDate.getTime()), content,
                                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                "1", article.getArticleID());
                        ResponseModel response = apiFunction.insertComment(comment);
                        if (response != null && response.getMessage().contains("complete")) {
                            Log.d(TAG, "onClick: response: " + response.getMessage());
                            Toast.makeText(this, "Bình luận thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onClick: response: " + response.getMessage());
                            Toast.makeText(this, "Bình luận không thành công!", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(this, "Không có kết nối mạng!", Toast.LENGTH_SHORT).show();
                }
                editTextComment.setText("");
                inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                break;
        }
    }

    private void checkLiked(final ImageView imageViewLike) {

        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = appPreferences.getUserId();
                post = dataSnapshot.getValue(Post.class);
                if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
                    if (post.getUserLikeIds().contains(userId)) {
                        imageViewLike.setImageResource(R.drawable.ic_liked);
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_like);
                    }
                } else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    private void onClickLikePost(final ImageView imageViewLike) {
        /*if(appPreferences.isLogin()) {
            //get from user_post
            if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
                String userId = appPreferences.getUserId();
                //if user liked this post
                if (post.getUserLikeIds().contains(userId)) {
                    int index = post.getUserLikeIds().indexOf(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child(String.valueOf(index)).removeValue();
                } else {
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child(String.valueOf(post.getUserLikeIds().size())).setValue(String.valueOf(userId));
                    imageViewLike.startAnimation(animlike);
                }
            } else {
                String userId = appPreferences.getUserId();
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child("0").setValue(String.valueOf(userId));
                imageViewLike.startAnimation(animlike);
            }
        }
        else {
            Intent intentLogin = new Intent(this,Login.class);
            startActivityForResult(intentLogin,AppConfig.REQUEST_CODE_LOGIN);
        }*/
    }

    private void onClickBookMark() {
        /*if(appPreferences.isLogin()) {
            progressDialog.show();
            databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> listBookmark = new ArrayList<String>();
                    //have not been save bookmark
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataBookmark : dataSnapshot.getChildren()) {
                            listBookmark.add(dataBookmark.getValue().toString());
                        }
                        if (listBookmark.contains(post.getPostId())) {
                            listBookmark.remove(post.getPostId());
                            databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).setValue(listBookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PostDetailActivity.this, getString(R.string.removebookmark), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            listBookmark.add(post.getPostId());
                            databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).setValue(listBookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    CustomSnackbar customSnackbar = CustomSnackbar.make(linearLayoutBookmark, 1);
                                    customSnackbar.setDuration(CustomSnackbar.LENGTH_LONG);
                                    customSnackbar.setText(getString(R.string.savebookmark));
                                    customSnackbar.setAction("Xem " + getString(R.string.action_bookmarks), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            GlobalFunction.onClickViewBookMark(PostDetailActivity.this, progressDialog);
                                        }
                                    });
                                    customSnackbar.show();
                                }
                            });
                        }
                    } else {
                        listBookmark.add(post.getPostId());
                        databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).setValue(listBookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                CustomSnackbar customSnackbar = CustomSnackbar.make(linearLayoutBookmark, 1);
                                customSnackbar.setDuration(CustomSnackbar.LENGTH_LONG);
                                customSnackbar.setText(getString(R.string.savebookmark));
                                customSnackbar.setAction("Xem " + getString(R.string.action_bookmarks), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        GlobalFunction.onClickViewBookMark(PostDetailActivity.this, progressDialog);
                                    }
                                });
                                customSnackbar.show();
                            }
                        });
                    }
                    if (BookMarkActivity.listPost != null) {
                        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BookMarkActivity.listPost = new ArrayList<Post>();
                                for (String pId : listBookmark) {
                                    BookMarkActivity.listPost.add(dataSnapshot.child(pId).getValue(Post.class));
                                }
                                if (BookMarkActivity.postsOnRequestAdapter != null)
                                    BookMarkActivity.postsOnRequestAdapter.setData(BookMarkActivity.listPost);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Intent intentLogin = new Intent(this,Login.class);
            startActivityForResult(intentLogin,AppConfig.REQUEST_CODE_LOGIN);
        }*/
    }


    @Override
    public void finish() {
        super.finish();
        inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
        overridePendingTransitionExit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAction();
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalStaticData.getCurrentPage() == 0)
                    recyclerViewPostDetail.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        NetworkChangeReceiver.unregister(this);
        unregisterUpdateNetworkReciver();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (data == null) {
            return;
        }

        if (requestCode == AppConfig.REQUEST_CODE_LOGIN && resultCode == AppConfig.RESULT_CODE_LOGIN) {
            postDetailAdapter.notifyDataSetChanged();
        }*/
    }


    //socket
    private void handleGetPost(Object[] args) {
        Log.d("PostDetailActivity", "handleGetPost: " + String.valueOf(args));
    }

    private void initSocket() {
        mSocket.connect();
        mSocket.on(SERVER_SEND_GETPOST, onGetPost);
    }

    private void addEvent() {
        mSocket.emit(CLIENT_SEND_GETPOST, 1);
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
                        updateNetWork(isNetworkAvailable);
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

    private void updateStatus(boolean isAvailable) {
        if (tvPostDetailStatus != null) {
            tvPostDetailStatus.setVisibility(isAvailable ? View.GONE : View.VISIBLE);
        }
    }

    private void updateNetWork(boolean isAvailable) {
        updateStatus(isAvailable);
        updateAdmobNetwork(isAvailable);
    }
}
