package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Activity_Fragment.PostDetailActivity;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Models.Article;
import com.android.Models.Post;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostsOnRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Animation animation0to180,animation180to0;
    List<Article> listArticle = new ArrayList<>();
    Animation hyperspaceJumpAnimation;
    AppPreferences appPreferences;
    APIFunction apiFunction;
    public PostsOnRequestAdapter(Context context, List<Article> listArticle) {
        this.context = context;
        this.listArticle = listArticle;
        animation0to180 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_0to180);
        animation180to0 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_180to0);
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animlike);
        appPreferences = AppPreferences.getInstance(context);
        apiFunction = new APIFunction();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Article article = listArticle.get(position);
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        //imageCover
        try{
            Glide.with(context).load(apiFunction.getUrlImage(article.getCoverImage())).into(itemViewHolder.imageViewCover);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        itemViewHolder.textViewTitile.setText(article.getTitle());

      //  checkLiked(post);



        itemViewHolder.relativeLayoutSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST, article);
                context.startActivity(intent);
            }
        });


    }

    private void checkLiked(final Post post, final ImageView imageViewLike, final TextView textViewLike){
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = appPreferences.getUserId();
                long count  = dataSnapshot.child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount();
                if(post.getUserLikeIds()!=null && count>0) {
                    if (post.getUserLikeIds().contains(userId)) {
                        imageViewLike.setImageResource(R.drawable.ic_liked);
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_like);
                    }
                    textViewLike.setText(String.valueOf(count));
                }
                else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                    textViewLike.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void onClickLikePost(final Post post, final ImageView imageViewLike){
        /*if(appPreferences.isLogin()) {
            //get from user_post
            String userId = appPreferences.getUserId();
            if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
                //if user liked this post
                if (post.getUserLikeIds().contains(userId)) {
                    post.getUserLikeIds().remove(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(post.getUserLikeIds());
                } else {
                    post.getUserLikeIds().add(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(post.getUserLikeIds());
                }
            } else {
                post.setUserLikeIds(new ArrayList<String>());
                post.getUserLikeIds().add(userId);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child("0").setValue(String.valueOf(userId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        imageViewLike.startAnimation(hyperspaceJumpAnimation);
                    }
                });
            }
        }
        else{
            Intent intentLogin = new Intent(context,Login.class);
            context.startActivity(intentLogin);
        }*/

    }

    @Override
    public int getItemCount() {
        return listArticle ==null ? 0: listArticle.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    public void setData(List<Article> list) {
        this.listArticle.clear();
        this.listArticle.addAll(list);
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        View v;
        RelativeLayout relativeLayoutSummary;
        ImageView imageViewCover;
        TextView textViewTitile;


        public ItemViewHolder( View v) {
            super(v);
            this.v = v;
            this.relativeLayoutSummary = (RelativeLayout) v.findViewById(R.id.relativeLayoutSummary);
            imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) itemView.findViewById(R.id.textViewBarName);


        }
    }


}
