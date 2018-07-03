package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Activity_Fragment.FeedbackCommentActivity;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Models.Article;
import com.android.Models.Comment;
import com.android.Models.FeedbackComment;
import com.android.Models.Post;
import com.android.Models.User;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    Article article;
    List<Comment> listComment = new ArrayList<>();
    AppPreferences appPreferences;
    APIFunction apiFunction;

    public CommentAdapter(Context context, Article article, List<Comment> listComment) {
        this.context = context;
        this.article = article;
        this.listComment = listComment;
        appPreferences = AppPreferences.getInstance(context);
        apiFunction = new APIFunction();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comment comm = listComment.get(position);
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
                Intent intent = new Intent(context, FeedbackCommentActivity.class);
                intent.putExtra(AppConfig.COMMENT, comm);
                intent.putExtra(AppConfig.POST, article);
                intent.putExtra(AppConfig.ACTION, AppConfig.COMMENT);
                context.startActivity(intent);
            }
        });

        List<FeedbackComment> listFeedback = apiFunction.getListFeedback_byCommentID(comm.getCommentID());
        if (listFeedback != null) {
            if (listFeedback.size() > 0) {
                holder.linearLayoutViewFeedback.setVisibility(View.VISIBLE);
                holder.linearLayoutViewFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FeedbackCommentActivity.class);
                        intent.putExtra(AppConfig.COMMENT, comm);
                        intent.putExtra(AppConfig.POST, article);
                        context.startActivity(intent);
                    }
                });
                if (listFeedback.size() > 1) {
                    holder.textViewViewFeedback.setVisibility(View.VISIBLE);
                    holder.textViewViewFeedback.setText("Xem " + String.valueOf(listFeedback.size()) + " câu trả lời trước");
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
            }
        }
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comm.getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.textViewLikeNumber.setText(String.valueOf(dataSnapshot.child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        checkLiked(comm, holder.textViewLike);

        /*holder.textViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLikeComment(post,comm,holder.textViewLike);
            }
        });*/

    }


    public void addComment(Comment comment) {
        this.listComment.add(comment);
        notifyDataSetChanged();
    }

    public void addList(List<Comment> comments) {
        int positionStart = getItemCount();
        this.listComment.addAll(comments);
        notifyItemRangeInserted(positionStart, comments.size());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listComment.get(position).getCommentID());
    }

    @Override
    public int getItemCount() {
        return listComment == null ? 0 : listComment.size();
    }

    public void setData(List<Comment> listComment) {
        this.listComment.clear();
        this.listComment.addAll(listComment);
        notifyDataSetChanged();
    }

    private void checkLiked(final Comment comment, final TextView textViewLike) {
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

    private void onClickLikeComment(final Post post, Comment comment, final TextView textViewLike) {
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

            linearLayoutViewFeedback = (LinearLayout) v.findViewById(R.id.linearLayoutViewFeedback);
            textViewViewFeedback = (TextView) v.findViewById(R.id.textViewViewFeedback);
            textViewFeedbackUsername = (TextView) v.findViewById(R.id.textViewFeedbackUsername);
            textViewFeedbackContent = (TextView) v.findViewById(R.id.textViewFeedbackContent);
            textViewLike = (TextView) v.findViewById(R.id.textViewLike);
            textViewLikeNumber = (TextView) v.findViewById(R.id.textViewLikeNumber);
            imageViewUserReply = v.findViewById(R.id.imageViewUserReply);

        }
    }

}
