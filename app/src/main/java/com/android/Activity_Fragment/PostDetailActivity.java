package com.android.Activity_Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import com.android.API.Response;
import com.android.Adapters.CommentAdapter;
import com.android.Adapters.PostDetailAdapter;
import com.android.BroadcastReceiver.NetworkChangeReceiver;
import com.android.CustomView.CustomSnackbar;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Models.Article;
import com.android.Models.Article_UserModel;
import com.android.Models.Comment;
import com.android.Models.ReportComment;
import com.android.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener, CommentAdapter.CommentInterface {
    private String TAG = this.getClass().getName();
    private final String ACTION_COMMENT_INSERT = "ACTION_COMMENT_INSERT";
    private final String ACTION_COMMENT_EDIT = "ACTION_COMMENT_EDIT";
    private final String ACTION_COMMENT_REPORT = "ACTION_COMMENT_REPORT";
    Handler handler;
    AppPreferences appPreferences;
    InputMethodManager inputMethodManager;
    //
    SwipeRefreshLayout swipeRefresh;
    //animation
    Animation animlike;
    Animation animshake;
    //back
    LinearLayout linearLayoutBack;
    //bookmark
    LinearLayout linearLayoutBookmark;
    ImageView imageViewBookmark;
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
    LinearLayout linearLayoutClear;
    TextView textViewAction;
    ;

    DatabaseReference databaseReference;

    //dialog wait
    SpotsDialog progressDialog;
    //progressbass load more

    private boolean isConnectNetwork;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager linearLayoutManager;

    APIFunction apiFunction;
    //
    TextView tvPostDetailStatus;
    BroadcastReceiver updateNetworkReciver;
    private AdView mAdView;

    //edit comment
    String action = ACTION_COMMENT_INSERT;
    Comment currentCommnet = null;
    int currentCommentPosition = -1;

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

        updateNetwork(GlobalFunction.isNetworkAvailable(this));
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
        swipeRefresh = findViewById(R.id.swipp_refresh);
        //back
        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        //bookmark
        linearLayoutBookmark = findViewById(R.id.linearLayoutBookmark);
        imageViewBookmark = findViewById(R.id.imageViewBookmark);
        //status
        tvPostDetailStatus = findViewById(R.id.tv_post_detail_status);

        //recyclerView contant content,tag,related and comment
        recyclerViewPostDetail = findViewById(R.id.recyclerViewPostDetail);

        //bottom
        linearLayoutLike = findViewById(R.id.linearLayoutLike);
        imageViewLike = findViewById(R.id.imageViewLike);
        editTextComment = findViewById(R.id.editTextComment);
        linearLayoutSend = findViewById(R.id.linearLayoutSend);
        imageViewSend = findViewById(R.id.imageViewSend);
        linearLayoutClear = findViewById(R.id.linearLayoutClear);
        textViewAction = findViewById(R.id.textViewAction);
    }

    private void initView() {

        swipeRefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postDetailAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                updateNetwork(GlobalFunction.isNetworkAvailable(PostDetailActivity.this));
            }
        });

        postDetailAdapter = new PostDetailAdapter(PostDetailActivity.this, article);
        recyclerViewPostDetail.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));
        linearLayoutManager = (LinearLayoutManager) recyclerViewPostDetail.getLayoutManager();
        recyclerViewPostDetail.setAdapter(postDetailAdapter);

        checkLiked(imageViewLike);

        if (GlobalStaticData.getCurrentUser() != null) {
            if (apiFunction.checkBoorkmark(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID())) {
                imageViewBookmark.setImageResource(R.drawable.ic_favorite_selected);
            }
        }

    }

    private void event() {
        //back
        linearLayoutBack.setOnClickListener(this);
        //bookmark
        linearLayoutBookmark.setOnClickListener(this);
        //bottom
        linearLayoutLike.setOnClickListener(this);
        linearLayoutSend.setOnClickListener(this);
        linearLayoutClear.setOnClickListener(this);

        linearLayoutManager = (LinearLayoutManager) recyclerViewPostDetail.getLayoutManager();
        recyclerViewPostDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (isConnectNetwork
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //postDetailAdapter.updateProgressBarLoadMore(true);
                    Log.d(TAG, "onScrolled: ");
                    postDetailAdapter.loadMore();
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
                inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                if (GlobalStaticData.getCurrentUser()!=null) {
                    if (isConnectNetwork) {
                        long time = new java.util.Date().getTime();
                        String commentID = Long.toString(time);
                        Date myDate = new Date();
                        String content = editTextComment.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            editTextComment.setText("");
                            imageViewSend.startAnimation(animshake);
                            if (action.equals(ACTION_COMMENT_INSERT)) {
                                Comment comment = new Comment(String.valueOf(myDate.getTime()), content,
                                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                        GlobalStaticData.getCurrentUser().getUserID(), article.getArticleID());
                                new SendRequestAsyncTask().execute(comment);
                            } else if (action.equals(ACTION_COMMENT_EDIT)) {
                                currentCommnet.setContent(content);
                                new SendRequestAsyncTask().execute(currentCommnet);
                            } else if (action.equals(ACTION_COMMENT_REPORT)) {
                                Comment comment = new Comment(String.valueOf(myDate.getTime()), content,
                                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                        GlobalStaticData.getCurrentUser().getUserID(), article.getArticleID());
                                new SendRequestAsyncTask().execute(comment);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Không có kết nối mạng!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(new Intent(this,LoginDialogActivity.class));
                }
                break;
            case R.id.linearLayoutClear:
                clearAction();
                break;
        }
    }

    private void clearAction() {
        action = ACTION_COMMENT_INSERT;
        editTextComment.setText("");
        textViewAction.setText("");
        currentCommentPosition = -1;
        currentCommnet = null;
        inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
    }

    @Override
    public void onEditComment(Comment comment, int position) {
        action = ACTION_COMMENT_EDIT;
        currentCommnet = comment;
        currentCommentPosition = position;
        editTextComment.requestFocus();
        editTextComment.setText(comment.getContent());
        editTextComment.setSelection(editTextComment.getText().toString().length());
        textViewAction.setText(R.string.action_edit);
        inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onReportComment(Comment comment) {
        action = ACTION_COMMENT_REPORT;
        currentCommnet = comment;
        editTextComment.requestFocus();
        textViewAction.setText(R.string.action_report);
        inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onClickReply(Comment comment, int position) {
        Intent intent = new Intent(this, FeedbackCommentActivity.class);
        intent.putExtra(AppConfig.COMMENT, comment);
        intent.putExtra(AppConfig.POST, article);
        intent.putExtra(AppConfig.COMMENT_POSITION, position);
        startActivityForResult(intent, AppConfig.REQUEST_CODE_FEEDBACK);
    }

    class SendRequestAsyncTask extends AsyncTask<Comment, Void, Response> {
        Comment comment = null;

        @Override
        protected Response doInBackground(Comment... comments) {
            comment = comments[0];
            if (comment != null) {
                switch (action) {
                    case ACTION_COMMENT_INSERT:
                        return apiFunction.insertComment(comment);
                    case ACTION_COMMENT_EDIT:
                        return apiFunction.updateComment(comment);
                    case ACTION_COMMENT_REPORT:
                        ReportComment reportComment = new ReportComment(comment.getCommentID(), currentCommnet.getCommentID(), comment.getContent()
                                , comment.getDateCreate(), comment.getUserID());
                        return apiFunction.reportComment(reportComment);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            String tempAction = "";
            String result = "";
            switch (action) {
                case ACTION_COMMENT_INSERT:
                    tempAction = "Bình luận ";
                    break;
                case ACTION_COMMENT_EDIT:
                    tempAction = "Chỉnh sửa bình luận ";
                    break;
                case ACTION_COMMENT_REPORT:
                    tempAction = "Tố cáo bình luận ";
                    break;
            }
            if (response == null || !response.isSuccess()) {
                result = "không thành công";
            } else {
                switch (action) {
                    case ACTION_COMMENT_INSERT:
                        postDetailAdapter.addComment(comment);
                        break;
                    case ACTION_COMMENT_EDIT:
                        postDetailAdapter.updateComment(comment, currentCommentPosition);
                        break;
                    case ACTION_COMMENT_REPORT:
                        break;
                }
                result = "thành công";
            }
            clearAction();
            Toast.makeText(PostDetailActivity.this, tempAction + result, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLiked(final ImageView imageViewLike) {
        if (GlobalStaticData.getCurrentUser()!=null) {
            if (apiFunction.checkLikeArticle(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID())) {
                imageViewLike.setImageResource(R.drawable.ic_liked);
            } else {
                imageViewLike.setImageResource(R.drawable.ic_like);
            }
        } else {
            imageViewLike.setImageResource(R.drawable.ic_like);
        }
    }

    private void onClickLikePost(final ImageView imageViewLike) {
        if (GlobalStaticData.getCurrentUser()!=null) {
            if (apiFunction.checkLikeArticle(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID())) {
                Response response = apiFunction.deleteLikeArticle(new Article_UserModel(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID()));
                if (response != null && response.getMessage().contains("complete")) {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                } else {
                    Log.d(TAG, "deleteLikeArticle: false");
                }
            } else {
                Response response = apiFunction.insertLikeArticle(new Article_UserModel(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID()));
                if (response != null && response.getMessage().contains("complete")) {
                    imageViewLike.startAnimation(animlike);
                    imageViewLike.setImageResource(R.drawable.ic_liked);
                } else {
                    Log.d(TAG, "insertLikeArticle: false");
                }
            }
        } else {
            startActivity(new Intent(this,LoginDialogActivity.class));
        }
    }

    private void onClickBookMark() {
        if (GlobalStaticData.getCurrentUser()!=null) {
            progressDialog.show();
            if (apiFunction.checkBoorkmark(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID())) {
                Response response = apiFunction.deleteBookmark(new Article_UserModel(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID()));
                if (response != null && response.getMessage().contains("complete")) {
                    Toast.makeText(PostDetailActivity.this, getString(R.string.removebookmark), Toast.LENGTH_SHORT).show();
                    imageViewBookmark.setImageResource(R.drawable.ic_favorite_normal);
                    progressDialog.dismiss();
                } else {
                    Log.d(TAG, "deleteBookmark: false");
                    progressDialog.dismiss();
                }
            } else {
                Response response = apiFunction.insertBookmark(new Article_UserModel(article.getArticleID(), GlobalStaticData.getCurrentUser().getUserID()));
                if (response != null && response.getMessage().contains("complete")) {
                    imageViewBookmark.setImageResource(R.drawable.ic_favorite_selected);
                    progressDialog.dismiss();
                    CustomSnackbar customSnackbar = CustomSnackbar.make(linearLayoutBookmark, 1);
                    customSnackbar.setDuration(CustomSnackbar.LENGTH_LONG);
                    customSnackbar.setText(getString(R.string.savebookmark));
                    customSnackbar.setAction("Xem " + getString(R.string.action_bookmarks), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.show();
                            Intent intent = new Intent(PostDetailActivity.this, BookMarkActivity.class);
                            intent.putExtra(AppConfig.BARNAME, AppConfig.FIREBASE_FIELD_BOOKMARKS);
                            intent.putExtra(AppConfig.LISTPOST, (ArrayList) apiFunction.getListBookmarkByUser(GlobalStaticData.getCurrentUser().getUserID()));
                            intent.putExtra(AppConfig.ACTION, AppConfig.FIREBASE_FIELD_BOOKMARKS);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    });
                    customSnackbar.show();
                } else {
                    Log.d(TAG, "insertBookmark: false");
                    progressDialog.dismiss();
                }
            }
        } else {
            startActivity(new Intent(this,LoginDialogActivity.class));
        }
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
        FloatingActionButton fab = findViewById(R.id.fab);
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
        Log.d(TAG, "onActivityResult: requestCode " + requestCode);
        Log.d(TAG, "onActivityResult: resultCode " + resultCode);
        if (data == null) {
            return;
        }
        if (requestCode == AppConfig.REQUEST_CODE_FEEDBACK && resultCode == AppConfig.RESULT_CODE_FEEDBACK) {
            Log.d(TAG, "onActivityResult: 1");
            Comment commentUpdate = (Comment) data.getSerializableExtra(AppConfig.COMMENT);
            Log.d(TAG, "commentUpdate: " + commentUpdate.getCommentID());
            Log.d(TAG, "DATA_CHANGE: " + data.getBooleanExtra(AppConfig.DATA_CHANGE, false));
            Log.d(TAG, "COMMENT_POSITION: " + data.getIntExtra(AppConfig.COMMENT_POSITION, -1));

            if (data.getBooleanExtra(AppConfig.DATA_CHANGE, false)
                    && commentUpdate != null) {
                Log.d(TAG, "onActivityResult: 2");
                int positionUpdate = data.getIntExtra(AppConfig.COMMENT_POSITION, -1);
                if (positionUpdate != -1) {
                    Log.d(TAG, "onActivityResult: 3");
                    postDetailAdapter.updateComment(commentUpdate, positionUpdate);
                }
            }
        }
    }


    //socket
    private void handleGetPost(Object[] args) {
        Log.d("PostDetailActivity", "handleGetPost: " + String.valueOf(args));
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
                        updateNetwork(isNetworkAvailable);
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

    private void updateNetwork(boolean isAvailable) {
        updateStatus(isAvailable);
        updateAdmobNetwork(isAvailable);
        isConnectNetwork = isAvailable;
    }


}
