package com.android.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.API.APIFunction;
import com.android.API.Response;
import com.android.Activity_Fragment.LoginDialogActivity;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Interface.IOnClickFeedback;
import com.android.Models.Article;
import com.android.Models.Comment;
import com.android.Models.Comment_UserModel;
import com.android.Models.FeedbackComment;
import com.android.Models.User;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class FeedbackCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = "FeedbackCommentAdapter";
    private final int ITEMVIEWTYPE_COMMENT = 0;
    private final int ITEMVIEWTYPE_FEEDBACK = 1;
    Context context;
    Article article;
    Comment comment;
    List<FeedbackComment> listFeedback = new ArrayList<>();
    IOnClickFeedback iOnClickFeedback;
    AppPreferences appPreferences;
    APIFunction apiFunction;

    private FeedbackCommentInterface feedbackCommentInterface;

    public FeedbackCommentAdapter(Context context, Article article, Comment comment) {
        this.context = context;
        this.article = article;
        this.comment = comment;
        apiFunction = new APIFunction();
        this.listFeedback = new ArrayList<>();
        this.listFeedback = apiFunction.getListFeedback_byCommentID(comment.getCommentID());
        appPreferences = AppPreferences.getInstance(context);
        try {
            feedbackCommentInterface = (FeedbackCommentInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Handle");
        }

    }

    public void setiOnClickFeedback(IOnClickFeedback iOnClickFeedback) {
        this.iOnClickFeedback = iOnClickFeedback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEMVIEWTYPE_COMMENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
                return new CommentViewholder(view);
            case ITEMVIEWTYPE_FEEDBACK:
                view = LayoutInflater.from(context).inflate(R.layout.item_feedbackcomment, parent, false);
                return new FeedbackViewholer(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case ITEMVIEWTYPE_COMMENT:
                final CommentViewholder commentViewholder = (CommentViewholder) holder;
                User user = apiFunction.getUser_byID(comment.getUserID());
                commentViewholder.textViewUsername.setText(user.getFullName());
                try {
                    Glide.with(context).load(apiFunction.getUrlImage(user.getImage())).into(commentViewholder.imageViewUserComment);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                commentViewholder.textViewContent.setText(comment.getContent());
                commentViewholder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(comment.getDateCreate()));

                List<Comment_UserModel> listComment_user = apiFunction.getListLikeComment(comment.getCommentID());
                if (listComment_user != null && listComment_user.size() > 0) {
                    commentViewholder.textViewLikeNumber.setVisibility(View.VISIBLE);
                    commentViewholder.textViewLikeNumber.setText(String.valueOf(listComment_user.size()));
                } else {
                    commentViewholder.textViewLikeNumber.setVisibility(View.GONE);
                }

                checkLiked(listComment_user,
                        new Comment_UserModel(comment.getCommentID(), GlobalStaticData.getCurrentUser().getUserID()), commentViewholder.textViewLike, commentViewholder.imageViewLike);

                commentViewholder.textViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnClickFeedback.onClickLike();
                    }
                });
                //commentViewholder.textViewLikeNumber.setText(String.valueOf());

                commentViewholder.textViewReplyComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnClickFeedback.onClickFeedback();
                    }
                });
                break;
            case ITEMVIEWTYPE_FEEDBACK:
                final FeedbackComment feedbackComment = listFeedback.get(position - 1);
                final FeedbackViewholer feedbackViewholer = (FeedbackViewholer) holder;
                User userfeedback = apiFunction.getUser_byID(feedbackComment.getUserID());
                feedbackViewholer.textViewFeedbackUsername.setText(userfeedback.getFullName());
                try {
                    Glide.with(context).load(apiFunction.getUrlImage(userfeedback.getImage())).into(feedbackViewholer.imageViewUserReply);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                feedbackViewholer.textViewFeedbackContent.setText(feedbackComment.getContent());
                feedbackViewholer.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(feedbackComment.getDateCreate()));


                feedbackViewholer.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GlobalFunction.isNetworkAvailable(context)) {
                            final List<CharSequence> charSequences = new ArrayList<>();
                            charSequences.add(context.getString(R.string.comment_reply));
                            if (feedbackComment.getUserID().equals(GlobalStaticData.getCurrentUser().getUserID())) {
                                charSequences.add(context.getString(R.string.comment_edit));
                                charSequences.add(context.getString(R.string.comment_delete));
                            } else {
                                charSequences.add(context.getString(R.string.comment_report));
                            }

                            if (charSequences.size() > 0) {
                                new AlertDialog.Builder(context).setItems(charSequences.toArray(new CharSequence[charSequences.size()]), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int positionSelected) {
                                        Log.d(TAG, "onClick: " + positionSelected);
                                        if (GlobalStaticData.getCurrentUser() != null) {
                                            if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_edit))) {
                                                feedbackCommentInterface.onEditFeedbackComment(feedbackComment, position-1);
                                            } else if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_delete))) {
                                                deleteFeedbackComment(feedbackComment, position-1);
                                            } else if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_report))) {
                                                feedbackCommentInterface.onReportFeedbackComment(feedbackComment);
                                            }
                                        }else{
                                            context.startActivity(new Intent(context, LoginDialogActivity.class));
                                        }
                                        dialogInterface.dismiss();
                                    }
                                }).show();

                            }
                        } else {
                            //not network
                        }
                    }
                });
                break;
        }
    }

    public void deleteFeedbackComment(final FeedbackComment feedbackComment, final int position) {
        new AlertDialog.Builder(context).setTitle(R.string.comment_delete_title)
                .setMessage(R.string.comment_delete_content)
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Response response = apiFunction.deleteFeedbackComment(feedbackComment);
                        if (response != null) {
                            if (response.isSuccess()) {
                                Toast.makeText(context, "Xóa bình luận thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "deleteComment: khong the delete");
                            }
                        } else {
                            Log.d(TAG, "deleteComment: khong the delete");
                        }
                        listFeedback.remove(position);
                        notifyItemRemoved(position+1);
                        dialog.dismiss();
                    }
                }).show();
    }

    public void addFeedbackComment(FeedbackComment feedbackComment) {
        listFeedback.add(0,feedbackComment);
        notifyItemInserted(0);
    }

    public void updateFeedbackComment(FeedbackComment feedbackComment, int position) {
        this.listFeedback.set(position, feedbackComment);
        notifyItemChanged(position+1);
    }


    @Override
    public int getItemCount() {
        return listFeedback == null ? 1 : (listFeedback.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ITEMVIEWTYPE_COMMENT;
        else
            return ITEMVIEWTYPE_FEEDBACK;
    }

    private void checkLiked(final List<Comment_UserModel> listComment_user, Comment_UserModel comment_user, TextView textViewLike, ImageView imageViewLike) {
        if (listComment_user != null && listComment_user.size() > 0) {
            imageViewLike.setVisibility(View.VISIBLE);
            if (apiFunction.checkLikeComment(comment_user.getCommentID(), comment_user.getUserID())) {
                textViewLike.setText(R.string.unlike);
            } else {
                textViewLike.setText(R.string.like);
            }
        } else {
            textViewLike.setText(R.string.like);
            imageViewLike.setVisibility(View.GONE);
        }
        /*String userId =appPreferences.getUserId();
        Comment comment1 = dataSnapshot.getValue(Comment.class);
        List<String> userLikeId = comment1.getUserLikeIds();
        long count = dataSnapshot.child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount();
        if (userLikeId != null && count > 0) {
            if (userLikeId.contains(userId)) {
                textViewLike.setTextColor(context.getResources().getColor(R.color.likedcomment));
            } else {
                textViewLike.setTextColor(context.getResources().getColor(R.color.black));
            }
        } else {
            textViewLike.setTextColor(context.getResources().getColor(R.color.black));
        }*/
    }

    private void onClickLikeComment(final Article article, Comment comment, final TextView textViewLike) {
        /*if(appPreferences.isLogin()) {
            String userId = appPreferences.getUserId();
            //get from user_post
            if (comment.getUserLikeIds() != null && comment.getUserLikeIds().size() > 0) {
                //if user liked this post
                if (comment.getUserLikeIds().contains(userId)) {
                    comment.getUserLikeIds().remove(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
                } else {
                    comment.getUserLikeIds().add(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
                }
            } else {
                comment.setUserLikeIds(new ArrayList<String>());
                comment.getUserLikeIds().add(userId);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
            }
        }
        else
        {
            Intent intentLogin = new Intent(context,Login.class);
            context.startActivity(intentLogin);
        }*/
    }

    public void setData(Comment comment) {
        this.comment = comment;
        this.listFeedback = new ArrayList<>();
        this.listFeedback = apiFunction.getListFeedback_byCommentID(comment.getCommentID());
        notifyDataSetChanged();
    }

    static class CommentViewholder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        TextView textViewContent;
        TextView textViewTimeAgo;
        TextView textViewLike;
        TextView textViewLikeNumber;
        TextView textViewReplyComment;
        ImageView imageViewUserComment;
        ImageView imageViewLike;

        public CommentViewholder(View itemView) {
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
            textViewLike = (TextView) itemView.findViewById(R.id.textViewLike);
            textViewLikeNumber = (TextView) itemView.findViewById(R.id.textViewLikeNumber);
            textViewReplyComment = (TextView) itemView.findViewById(R.id.textViewReplyComment);
            imageViewUserComment = itemView.findViewById(R.id.imageViewUserComment);
            imageViewLike = itemView.findViewById(R.id.imageViewLike);
        }
    }

    static class FeedbackViewholer extends RecyclerView.ViewHolder {
        TextView textViewFeedbackUsername;
        TextView textViewFeedbackContent;
        TextView textViewTimeAgo;
        ImageView imageViewUserReply;

        public FeedbackViewholer(View itemView) {
            super(itemView);
            textViewFeedbackUsername = (TextView) itemView.findViewById(R.id.textViewFeedbackUsername);
            textViewFeedbackContent = (TextView) itemView.findViewById(R.id.textViewFeedbackContent);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
            imageViewUserReply = itemView.findViewById(R.id.imageViewUserReply);
        }
    }

    public interface FeedbackCommentInterface {
        void onEditFeedbackComment(FeedbackComment feedbackComment, int position);

        void onReportFeedbackComment(FeedbackComment feedbackComment);
    }
}
