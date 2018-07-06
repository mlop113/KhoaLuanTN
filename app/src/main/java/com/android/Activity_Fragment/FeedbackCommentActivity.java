package com.android.Activity_Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.android.Adapters.FeedbackCommentAdapter;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Interface.IOnClickFeedback;
import com.android.Models.Article;
import com.android.Models.Comment;
import com.android.Models.Comment_UserModel;
import com.android.Models.FeedbackComment;
import com.android.Models.ReportFeedbackComment;
import com.android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class FeedbackCommentActivity extends AppCompatActivity implements View.OnClickListener, IOnClickFeedback, FeedbackCommentAdapter.FeedbackCommentInterface  {
    final static String TAG = "FeedbackCommentActivity";
    private final String ACTION_COMMENT_INSERT = "ACTION_COMMENT_INSERT";
    private final String ACTION_COMMENT_EDIT = "ACTION_COMMENT_EDIT";
    private final String ACTION_COMMENT_REPORT = "ACTION_COMMENT_REPORT";
    InputMethodManager inputMethodManager;
    //animation
    Animation animlike;
    Animation animshake;

    //back
    LinearLayout linearLayoutback;

    //post
    Article article;

    //comm
    Comment comment;

    Intent intent;
    //feedbackcomment
    RecyclerView recyclerViewFeedbackComment;
    FeedbackCommentAdapter feedbackCommentAdapter;

    //bottom
    LinearLayout linearLayoutLike;
    ImageView imageViewLike;
    EditText editTextComment;
    LinearLayout linearLayoutSend;
    ImageView imageViewSend;
    LinearLayout linearLayoutClear;
    TextView textViewAction;

    DatabaseReference databaseReference;
    AppPreferences appPreferences;

    private APIFunction apiFunction;

    //edit comment
    String action = ACTION_COMMENT_INSERT;
    FeedbackComment currentFeedbackComment = null;
    int currentFeedbackCommentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_comment);
        appPreferences = AppPreferences.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        apiFunction = APIFunction.getInstance();
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        //animation
        animlike = AnimationUtils.loadAnimation(this, R.anim.animlike);
        animshake = AnimationUtils.loadAnimation(this, R.anim.animshake);

        intent = getIntent();
        if (intent != null) {
            comment = (Comment) intent.getSerializableExtra(AppConfig.COMMENT);
            article = (Article) intent.getSerializableExtra(AppConfig.POST);
        } else {
            finish();
            Toast.makeText(this, "Error data tranfer", Toast.LENGTH_SHORT).show();
        }
        mapping();
        initView();
        event();
    }

    private void mapping() {
        //back
        linearLayoutback = (LinearLayout) findViewById(R.id.linearLayoutBack);
        //
        recyclerViewFeedbackComment = (RecyclerView) findViewById(R.id.recyclerViewFeedbackComment);

        //bottom
        linearLayoutLike = (LinearLayout) findViewById(R.id.linearLayoutLike);
        imageViewLike = (ImageView) findViewById(R.id.imageViewLike);
        editTextComment = (EditText) findViewById(R.id.editTextComment);
        linearLayoutSend = (LinearLayout) findViewById(R.id.linearLayoutSend);
        imageViewSend = (ImageView) findViewById(R.id.imageViewSend);
        linearLayoutClear = findViewById(R.id.linearLayoutClear);
        textViewAction = findViewById(R.id.textViewAction);
    }

    private void initView() {
        feedbackCommentAdapter = new FeedbackCommentAdapter(this, article, comment);
        feedbackCommentAdapter.setiOnClickFeedback(this);
        recyclerViewFeedbackComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFeedbackComment.setAdapter(feedbackCommentAdapter);
        checkLiked();
    }

    private void event() {
        linearLayoutback.setOnClickListener(this);

        //bottom
        linearLayoutLike.setOnClickListener(this);
        linearLayoutSend.setOnClickListener(this);
        linearLayoutClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayoutBack:
                finish();
                break;
            //bottom
            case R.id.linearLayoutLike:
                onClickLikeComment();
                break;
            case R.id.linearLayoutSend:
                inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                if (GlobalStaticData.getCurrentUser()!=null) {
                    if (GlobalFunction.isNetworkAvailable(this)) {
                        long time = new java.util.Date().getTime();
                        String feedbackID = Long.toString(time);
                        Date myDate = new Date();
                        String content = editTextComment.getText().toString();
                        /*if (!TextUtils.isEmpty(content)) {
                            editTextComment.setText("");
                            imageViewSend.startAnimation(animshake);
                            FeedbackComment feedbackComment = new FeedbackComment(String.valueOf(myDate.getTime()), comment.getCommentID()
                                    , content,
                                    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                    "1");
                            new SendFeedbackAsyncTask().execute(feedbackComment);
                        }*/
                        if (!TextUtils.isEmpty(content)) {
                            editTextComment.setText("");
                            imageViewSend.startAnimation(animshake);
                            if (action.equals(ACTION_COMMENT_INSERT)) {
                                FeedbackComment feedbackComment = new FeedbackComment(String.valueOf(myDate.getTime()), comment.getCommentID()
                                        , content,
                                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                        GlobalStaticData.getCurrentUser().getUserID());
                                new SendRequestAsyncTask().execute(feedbackComment);
                            } else if (action.equals(ACTION_COMMENT_EDIT)) {
                                currentFeedbackComment.setContent(content);
                                new SendRequestAsyncTask().execute(currentFeedbackComment);
                            } else if (action.equals(ACTION_COMMENT_REPORT)) {
                                FeedbackComment feedbackComment = new FeedbackComment(String.valueOf(myDate.getTime()), comment.getCommentID()
                                        , content,
                                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                        GlobalStaticData.getCurrentUser().getUserID());
                                new SendRequestAsyncTask().execute(feedbackComment);
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
        currentFeedbackCommentPosition = -1;
        currentFeedbackComment = null;
        inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
    }

    @Override
    public void onEditFeedbackComment(FeedbackComment feedbackComment, int position) {
        action = ACTION_COMMENT_EDIT;
        currentFeedbackComment = feedbackComment;
        currentFeedbackCommentPosition = position;
        editTextComment.requestFocus();
        editTextComment.setText(feedbackComment.getContent());
        editTextComment.setSelection(editTextComment.getText().toString().length());
        textViewAction.setText(R.string.action_edit);
        inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onReportFeedbackComment(FeedbackComment feedbackComment) {
        action = ACTION_COMMENT_REPORT;
        currentFeedbackComment = feedbackComment;
        editTextComment.requestFocus();
        textViewAction.setText(R.string.action_report);
        inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
    }

    class SendRequestAsyncTask extends AsyncTask<FeedbackComment, Void, Response> {
        FeedbackComment feedbackComment = null;

        @Override
        protected Response doInBackground(FeedbackComment... feedbackComments) {
            feedbackComment = feedbackComments[0];
            if (feedbackComment != null) {
                switch (action) {
                    case ACTION_COMMENT_INSERT:
                        return apiFunction.insertFeedbackComment(feedbackComment);
                    case ACTION_COMMENT_EDIT:
                        return apiFunction.updateFeedbackComment(feedbackComment);
                    case ACTION_COMMENT_REPORT:
                        ReportFeedbackComment reportFeedbackComment = new ReportFeedbackComment(feedbackComment.getFeedbackCommentID(),
                                currentFeedbackComment.getFeedbackCommentID(), feedbackComment.getContent()
                                , feedbackComment.getDateCreate(), feedbackComment.getUserID());
                        return apiFunction.reportFeedbackComment(reportFeedbackComment);
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
                        feedbackCommentAdapter.addFeedbackComment(feedbackComment);
                        break;
                    case ACTION_COMMENT_EDIT:
                        feedbackCommentAdapter.updateFeedbackComment(feedbackComment,currentFeedbackCommentPosition);
                        break;
                    case ACTION_COMMENT_REPORT:
                        break;
                }
                result = "thành công";
            }
            clearAction();
            Toast.makeText(FeedbackCommentActivity.this, tempAction + result, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void finish() {
        inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
        Intent intent = new Intent();
        intent.putExtra(AppConfig.DATA_CHANGE, true);
        intent.putExtra(AppConfig.COMMENT, comment);
        intent.putExtra(AppConfig.COMMENT_POSITION, getIntent().getIntExtra(AppConfig.COMMENT_POSITION, -1));
        setResult(AppConfig.RESULT_CODE_FEEDBACK, intent);
        super.finish();
        Log.d(TAG, "finish: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAction();
        feedbackCommentAdapter.notifyDataSetChanged();
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
        }
    }

    @Override
    public void onClickFeedback() {
        editTextComment.requestFocus();
        inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onClickLike() {
        onClickLikeComment();
    }

    private void checkLiked() {
        if (GlobalStaticData.getCurrentUser()!=null) {
            if (apiFunction.checkLikeComment(comment.getCommentID(), GlobalStaticData.getCurrentUser().getUserID())) {
                imageViewLike.setImageResource(R.drawable.ic_liked);
            } else {
                imageViewLike.setImageResource(R.drawable.ic_like);
            }
        } else {
            imageViewLike.setImageResource(R.drawable.ic_like);
        }
    }

    private void onClickLikeComment() {
        if (GlobalStaticData.getCurrentUser()!=null) {
            if (apiFunction.checkLikeComment(comment.getCommentID(), GlobalStaticData.getCurrentUser().getUserID())) {
                Response response = apiFunction.deleteLikeComment(new Comment_UserModel(comment.getCommentID(), GlobalStaticData.getCurrentUser().getUserID()));
                if (response != null && response.getMessage().contains("complete")) {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                } else {
                    Log.d(TAG, "deleteLikeComment: false");
                }
            } else {
                Response response = apiFunction.insertLikeComment(new Comment_UserModel(comment.getCommentID(), GlobalStaticData.getCurrentUser().getUserID()));
                if (response != null && response.getMessage().contains("complete")) {
                    imageViewLike.startAnimation(animlike);
                    imageViewLike.setImageResource(R.drawable.ic_liked);
                } else {
                    Log.d(TAG, "insertLikeComment: false");
                }
            }
            if (feedbackCommentAdapter != null) {
                feedbackCommentAdapter.notifyItemChanged(0);
            }
        } else {
            startActivity(new Intent(this,LoginDialogActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (requestCode == AppConfig.REQUEST_CODE_LOGIN && resultCode == AppConfig.RESULT_CODE_LOGIN) {
            feedbackCommentAdapter.notifyDataSetChanged();
        }
    }
}
