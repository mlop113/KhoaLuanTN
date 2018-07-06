package com.android.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.API.APIFunction;
import com.android.API.Response;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    final static String TAG = "CommentAdapter";
    Context context;
    Article article;
    List<Comment> listDataComment;
    AppPreferences appPreferences;
    APIFunction apiFunction;

    List<Comment> listFullComment;
    int commentLoadMore = 5;
    int startCommentPostion = 0;
    int lastCommentPostion = 0;
    boolean isLogin = true;
    private CommentInterface commentInterface;

    public CommentAdapter(Context context, Article article, List<Comment> listFullComment) {
        this.context = context;
        this.article = article;
        this.listFullComment = listFullComment;
        this.listDataComment = new ArrayList<>();
        appPreferences = AppPreferences.getInstance(context);
        apiFunction = new APIFunction();
        try {
            commentInterface = (CommentInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Handle");
        }
    }

    private List<Comment> getListLoadMoreAndUpdatePostion() {
        List<Comment> listMore = new ArrayList<>();
        if (lastCommentPostion >= listFullComment.size()) {
            return listMore;
        }
        if (lastCommentPostion + commentLoadMore >= listFullComment.size()) {
            startCommentPostion = lastCommentPostion;
            lastCommentPostion = listFullComment.size();
        } else {
            startCommentPostion = lastCommentPostion;
            lastCommentPostion += commentLoadMore;
        }
        listMore = listFullComment.subList(startCommentPostion, lastCommentPostion);
        return listMore;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Comment comm = listDataComment.get(position);
        User user = apiFunction.getUser_byID(comm.getUserID());
        holder.textViewUsername.setText(user.getFullName());
        try {
            Glide.with(context).load(apiFunction.getUrlImage(user.getImage())).into(holder.imageViewUserComment);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        holder.textViewContent.setText(comm.getContent());
        holder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(comm.getDateCreate()));
        holder.textViewReplyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentInterface.onClickReply(comm, position);
            }
        });

        List<FeedbackComment> listFeedback = apiFunction.getListFeedback_byCommentID(comm.getCommentID());
        if (listFeedback != null) {
            if (listFeedback.size() > 0) {
                holder.linearLayoutViewFeedback.setVisibility(View.VISIBLE);
                holder.linearLayoutViewFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentInterface.onClickReply(comm, position);
                    }
                });
                if (listFeedback.size() > 1) {
                    holder.textViewViewFeedback.setVisibility(View.VISIBLE);
                    holder.textViewViewFeedback.setText("Xem " + String.valueOf(listFeedback.size()) + " câu trả lời trước");
                } else {
                    holder.textViewViewFeedback.setVisibility(View.GONE);
                }
                FeedbackComment feedbackComment = listFeedback.get(0);
                User userFeedback = apiFunction.getUser_byID(feedbackComment.getUserID());
                holder.textViewFeedbackUsername.setText(userFeedback.getFullName());
                try {
                    Glide.with(context).load(apiFunction.getUrlImage(userFeedback.getImage())).into(holder.imageViewUserReply);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                holder.textViewFeedbackContent.setText(feedbackComment.getContent());
            } else {
                holder.linearLayoutViewFeedback.setVisibility(View.GONE);
            }
        } else {
            holder.linearLayoutViewFeedback.setVisibility(View.GONE);
        }

        List<Comment_UserModel> listComment_user = apiFunction.getListLikeComment(comm.getCommentID());
        if (listComment_user != null && listComment_user.size() > 0) {
            holder.textViewLikeNumber.setVisibility(View.VISIBLE);
            holder.textViewLikeNumber.setText(String.valueOf(listComment_user.size()));
        } else {
            holder.textViewLikeNumber.setVisibility(View.GONE);
        }

        checkLiked(listComment_user,
                new Comment_UserModel(comm.getCommentID(), "1"), holder.textViewLike, holder.imageViewLike);

        holder.textViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLikeComment(position, new Comment_UserModel(comm.getCommentID(), "1"), holder.textViewLike);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalFunction.isNetworkAvailable(context)) {
                    final List<CharSequence> charSequences = new ArrayList<>();
                    if (apiFunction.checkLikeComment(comm.getCommentID(), "1")) {
                        charSequences.add(context.getString(R.string.comment_unlike));
                    } else {
                        charSequences.add(context.getString(R.string.comment_like));
                    }
                    charSequences.add(context.getString(R.string.comment_reply));
                    if (comm.getUserID().equals("1")) {
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
                                if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_like))
                                        || charSequences.get(positionSelected).equals(context.getString(R.string.comment_unlike))) {
                                    onClickLikeComment(position, new Comment_UserModel(comm.getCommentID(), "1"), holder.textViewLike);
                                } else if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_reply))) {
                                    commentInterface.onClickReply(comm, position);
                                } else if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_edit))) {
                                    commentInterface.onEditComment(comm, position);
                                } else if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_delete))) {
                                    deleteComment(comm, position);
                                } else if (charSequences.get(positionSelected).equals(context.getString(R.string.comment_report))) {
                                    commentInterface.onReportComment(comm);
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
    }

    public void loadMore() {
        if (checkAvalidableLoadMore()) {
            addList(getListLoadMoreAndUpdatePostion());
        }
    }

    public boolean checkAvalidableLoadMore() {
        Log.d(TAG, "lastCommentPostion: " + lastCommentPostion);
        Log.d(TAG, "listFullComment: " + (listFullComment.size()));
        if (listFullComment == null) {
            return false;
        }
        if (lastCommentPostion >= listFullComment.size()) {
            return false;
        }
        return true;
    }

    public List<Comment> getListFull() {
        return listFullComment;
    }

    public void deleteComment(final Comment comment, final int position) {
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
                        Response response = apiFunction.deleteComment(comment);
                        if (response != null) {
                            if (response.isSuccess()) {
                                Toast.makeText(context, "Xóa bình luận thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "deleteComment: khong the delete");
                            }
                        } else {
                            Log.d(TAG, "deleteComment: khong the delete");
                        }
                        listDataComment.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                    }
                }).show();
    }

    public void addComment(Comment comment) {
        this.listDataComment.add(0, comment);
        this.listFullComment.add(0, comment);
        startCommentPostion += 1;
        lastCommentPostion += 1;
        notifyItemInserted(0);
    }

    public void updateComment(Comment comment, int position) {
        this.listDataComment.set(position, comment);
        notifyItemChanged(position);
    }

    public void addList(List<Comment> comments) {
        int positionStart = getItemCount();
        this.listDataComment.addAll(comments);
        notifyItemRangeInserted(positionStart, comments.size());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listDataComment.get(position).getCommentID());
    }

    @Override
    public int getItemCount() {
        return listDataComment == null ? 0 : listDataComment.size();
    }

    public void setData(List<Comment> listComment) {
        this.listDataComment.clear();
        this.listDataComment.addAll(listComment);
        notifyDataSetChanged();
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
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(AppConfig.FIREBASE_FIELD_USERLIKEIDS)) {
                    String userId = appPreferences.getUserId();
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
                    }
                }
                else {
                    textViewLike.setTextColor(context.getResources().getColor(R.color.black));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    private void onClickLikeComment(int position, Comment_UserModel comment_user, final TextView textViewLike) {
        Log.d(TAG, "onClickLikeComment: 1");
        if (isLogin) {
            Log.d(TAG, "onClickLikeComment: 2");
            if (apiFunction.checkLikeComment(comment_user.getCommentID(), comment_user.getUserID())) {
                Log.d(TAG, "onClickLikeComment: 3");
                Response response = apiFunction.deleteLikeComment(comment_user);
                if (response != null && response.getMessage().contains("complete")) {
                    notifyItemChanged(position);
                } else {
                    Log.d(TAG, "deleteLikeComment: false");
                }
            } else {
                Log.d(TAG, "onClickLikeComment: 4");
                Response response = apiFunction.insertLikeComment(comment_user);
                if (response != null && response.getMessage().contains("complete")) {
                    notifyItemChanged(position);
                } else {
                    Log.d(TAG, "insertComment: false");
                }
            }
        } else {

        }
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
        else{
            Intent intentLogin = new Intent(context,Login.class);
            context.startActivity(intentLogin);
        }*/
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        TextView textViewUsername;
        TextView textViewContent;
        TextView textViewReplyComment;
        ImageView imageViewUserComment;
        ImageView imageViewLike;
        //View feedback
        LinearLayout linearLayoutViewFeedback;
        TextView textViewViewFeedback;
        TextView textViewFeedbackUsername;
        TextView textViewFeedbackContent;
        TextView textViewTimeAgo;
        TextView textViewLike;
        TextView textViewLikeNumber;
        ImageView imageViewUserReply;

        public ViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            textViewUsername = (TextView) v.findViewById(R.id.textViewUsername);
            textViewContent = (TextView) v.findViewById(R.id.textViewContent);
            textViewReplyComment = (TextView) v.findViewById(R.id.textViewReplyComment);
            textViewTimeAgo = (TextView) v.findViewById(R.id.textViewTimeAgo);
            imageViewUserComment = v.findViewById(R.id.imageViewUserComment);
            imageViewLike = v.findViewById(R.id.imageViewLike);

            linearLayoutViewFeedback = (LinearLayout) v.findViewById(R.id.linearLayoutViewFeedback);
            textViewViewFeedback = (TextView) v.findViewById(R.id.textViewViewFeedback);
            textViewFeedbackUsername = (TextView) v.findViewById(R.id.textViewFeedbackUsername);
            textViewFeedbackContent = (TextView) v.findViewById(R.id.textViewFeedbackContent);
            textViewLike = (TextView) v.findViewById(R.id.textViewLike);
            textViewLikeNumber = (TextView) v.findViewById(R.id.textViewLikeNumber);
            imageViewUserReply = v.findViewById(R.id.imageViewUserReply);

        }
    }

    public interface CommentInterface {
        void onEditComment(Comment comment, int position);

        void onReportComment(Comment comment);

        void onClickReply(Comment comment, int position);
    }

}
