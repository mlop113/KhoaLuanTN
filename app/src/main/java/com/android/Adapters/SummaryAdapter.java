package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Activity_Fragment.PostDetailActivity;
import com.android.Activity_Fragment.PostsOnRequestActivity;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Models.Article;
import com.android.Models.Category;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEMVIEWTYPE_HEADER = 0;
    private final int ITEMVIEWTYPE_ITEM = 1;
    private final int ITEMVIEWTYPE_CATEGORY = 2;
    Context context;
    Animation animation0to180, animation180to0;
    List<Article> listArticle = new ArrayList<>();
    List<Category> listCategory = new ArrayList<>();
    Animation hyperspaceJumpAnimation;
    AppPreferences appPreferences;
    APIFunction apiFunction;
    public SummaryAdapter(Context context, List<Article> listArticle, List<Category> listCategory) {
        this.context = context;
        this.listArticle = listArticle;
        this.listCategory = listCategory;
        animation0to180 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_0to180);
        animation180to0 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_180to0);
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animlike);
        apiFunction = new APIFunction();
        appPreferences = AppPreferences.getInstance(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ITEMVIEWTYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_header, parent, false);
                return new HeaderViewHolder(view);
            case ITEMVIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false);
                return new ItemViewHolder(view);
            case ITEMVIEWTYPE_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_category, parent, false);
                return new CategoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case ITEMVIEWTYPE_HEADER:

                final Article articleHeader = listArticle.get(position);
                final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                try{
                    if (articleHeader.getCoverImageOffLine() != null) {
                        Bitmap bm = BitmapFactory.decodeByteArray(articleHeader.getCoverImageOffLine(), 0, articleHeader.getCoverImageOffLine().length);
                        Glide.with(context).load(bm).into(headerViewHolder.imageViewCover);
                    } else {
                        Glide.with(context).load(apiFunction.getUrlImage(articleHeader.getCoverImage())).into(headerViewHolder.imageViewCover);
                    }
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                headerViewHolder.textViewTitile.setText(articleHeader.getTitle());
               // Blur blur = new Blur(context);
               // blur.applyBlur(headerViewHolder.imageViewCover, headerViewHolder.textViewDescription);
                headerViewHolder.textViewDescription.setText(articleHeader.getDescription());
              //  checkLiked(postHeader, headerViewHolder.imageViewLike, headerViewHolder.textViewLike);



                headerViewHolder.linearLayoutSummary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra(AppConfig.POST, articleHeader);
                        context.startActivity(intent);
                    }
                });

                break;
            case ITEMVIEWTYPE_ITEM:
                final Article article = listArticle.get(position);
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                //imageCover
                try{
                    if (article.getCoverImageOffLine() != null) {
                        Bitmap bm = BitmapFactory.decodeByteArray(article.getCoverImageOffLine(), 0, article.getCoverImageOffLine().length);
                        Glide.with(context).load(bm).into(itemViewHolder.imageViewCover);
                    } else {
                        Glide.with(context).load(apiFunction.getUrlImage(article.getCoverImage())).into(itemViewHolder.imageViewCover);
                    }
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                itemViewHolder.textViewTitile.setText(article.getTitle());
                //    checkLiked(post, itemViewHolder.imageViewLike, itemViewHolder.textViewLike);



                itemViewHolder.relativeLayoutSummary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra(AppConfig.POST, article);
                        context.startActivity(intent);
                    }
                });


//                        itemViewHolder.linearLayoutLike.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                onClickLikePost(post, itemViewHolder.imageViewLike);
//                            }
//                        });


                break;
            case ITEMVIEWTYPE_CATEGORY:

                final Category category = listCategory.get(position - listArticle.size());
                final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;

                //listCategory home
                final List<Article> listArticleByCategory = apiFunction.getListArticle_byCategoryID(category.getCategoryID());

                categoryViewHolder.relativeLayoutCategoryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostsOnRequestActivity.class);
                        intent.putExtra(AppConfig.LISTPOST, (ArrayList) listArticleByCategory);
                        intent.putExtra(AppConfig.BARNAME, category.getName());
                        context.startActivity(intent);
                    }
                });

                categoryViewHolder.textViewCateGoryName.setText(category.getName());

                if (listArticleByCategory != null && listArticleByCategory.size() > 0) {
                    final Article article1 = listArticleByCategory.get(0);
                    categoryViewHolder.linearLayoutPost1.setVisibility(View.VISIBLE);
                    try{
                        Glide.with(context).load(article1.getCoverImage()).into(categoryViewHolder.imageViewPost1);
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    categoryViewHolder.textViewTitlePost1.setText(article1.getTitle());
                    categoryViewHolder.linearLayoutPost1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPostDetail(article1);
                        }
                    });
                    if (listArticleByCategory.size() > 1) {
                        final Article article2 = listArticleByCategory.get(1);
                        categoryViewHolder.linearLayoutPost2.setVisibility(View.VISIBLE);
                        try{
                            Glide.with(context).load(article2.getCoverImage()).into(categoryViewHolder.imageViewPost2);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                        categoryViewHolder.textViewDescriptionPost2.setText(article2.getDescription());
                        categoryViewHolder.linearLayoutPost2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPostDetail(article2);
                            }
                        });
                    }
                    if (listArticleByCategory.size() > 2) {
                        final Article article3 = listArticleByCategory.get(2);
                        categoryViewHolder.linearLayoutPost3.setVisibility(View.VISIBLE);
                        try{
                            Glide.with(context).load(article3.getCoverImage()).into(categoryViewHolder.imageViewPost3);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                        categoryViewHolder.textViewDescriptionPost3.setText(article3.getDescription());
                        categoryViewHolder.linearLayoutPost3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPostDetail(article3);
                            }
                        });
                    }
                }
                break;
        }
    }



    private static int LIKE_TIME_OUT = 1500;

//    private void checkLiked(final Post post, final ImageView imageViewLike, final TextView textViewLike) {
//
//        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String userId = appPreferences.getUserId();
//                long count = dataSnapshot.child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount();
//                if (post.getUserLikeIds() != null && count > 0) {
//                    if (post.getUserLikeIds().contains(userId)) {
//                        imageViewLike.setImageResource(R.drawable.ic_liked);
//                    } else {
//                        imageViewLike.setImageResource(R.drawable.ic_like);
//                    }
//                    textViewLike.setText(String.valueOf(count));
//                } else {
//                    imageViewLike.setImageResource(R.drawable.ic_like);
//                    textViewLike.setText("0");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

//    private void onClickLikePost(final Post post, final ImageView imageViewLike) {
//        if(appPreferences.isLogin()) {
//            //get from user_post
//            String userId = appPreferences.getUserId();
//            if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
//                //if user liked this post
//                if (post.getUserLikeIds().contains(userId)) {
//                    post.getUserLikeIds().remove(userId);
//                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
//                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(post.getUserLikeIds());
//                } else {
//                    post.getUserLikeIds().add(userId);
//                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
//                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(post.getUserLikeIds());
//                }
//            } else {
//                post.setUserLikeIds(new ArrayList<String>());
//                post.getUserLikeIds().add(userId);
//                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
//                        .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child("0").setValue(String.valueOf(userId), new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                        imageViewLike.startAnimation(hyperspaceJumpAnimation);
//                    }
//                });
//            }
//        }
//        else{
//            Intent intentLogin = new Intent(context,Login.class);
//            context.startActivity(intentLogin);
//        }
//    }


    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < listArticle.size()) {
            if (position == 0)
                return ITEMVIEWTYPE_HEADER;
            return ITEMVIEWTYPE_ITEM;
        }
        if (position >= listArticle.size())
            return ITEMVIEWTYPE_CATEGORY;
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listArticle.get(position).getArticleID());
    }


    @Override
    public int getItemCount() {
        return listArticle == null && listCategory == null ? 0 : listArticle.size() + listCategory.size();
    }

    public List<Article> getListArticle() {
        return listArticle;
    }

    public void setListArticle(List<Article> list) {
        this.listArticle.clear();
        this.listArticle.addAll(list);
        notifyDataSetChanged();
    }

    public void setListCategory(List<Category> listCategory) {
        this.listCategory.clear();
        this.listCategory.addAll(listCategory);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Article> list) {
        int startIndex = this.listArticle.size();
        this.listArticle.addAll(startIndex, list);
        notifyItemRangeInserted(startIndex, list.size());
    }

    private void openPostDetail(Article article) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(AppConfig.POST, article);
        context.startActivity(intent);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayoutSummary;
        ImageView imageViewCover;
        TextView textViewTitile;


        public ItemViewHolder(View itemView) {
            super(itemView);
            this.relativeLayoutSummary = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutSummary);
            imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) itemView.findViewById(R.id.textViewBarName);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutSummary;
        ImageView imageViewCover;
        TextView textViewTitile, textViewDescription;



        public HeaderViewHolder(View itemView) {
            super(itemView);
            linearLayoutSummary = (LinearLayout) itemView.findViewById(R.id.linearLayoutSummary);
            imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) itemView.findViewById(R.id.textViewBarName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);


        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View v;
        //CategoryName
        RelativeLayout relativeLayoutCategoryName;
        TextView textViewCateGoryName;
        RelativeLayout linearLayoutPost1;
        ImageView imageViewPost1;
        TextView textViewTitlePost1;
        LinearLayout linearLayoutPost2;
        ImageView imageViewPost2;
        TextView textViewDescriptionPost2;
        LinearLayout linearLayoutPost3;
        ImageView imageViewPost3;
        TextView textViewDescriptionPost3;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            relativeLayoutCategoryName = (RelativeLayout) v.findViewById(R.id.relativeLayoutCategoryName);
            textViewCateGoryName = (TextView) v.findViewById(R.id.textViewCateGoryName);
            linearLayoutPost1 = (RelativeLayout) v.findViewById(R.id.linearLayoutPost1);
            imageViewPost1 = (ImageView) v.findViewById(R.id.imageViewPost1);
            textViewTitlePost1 = (TextView) v.findViewById(R.id.textViewTitlePost1);
            linearLayoutPost2 = (LinearLayout) v.findViewById(R.id.linearLayoutPost2);
            imageViewPost2 = (ImageView) v.findViewById(R.id.imageViewPost2);
            textViewDescriptionPost2 = (TextView) v.findViewById(R.id.textViewDescriptionPost2);
            linearLayoutPost3 = (LinearLayout) v.findViewById(R.id.linearLayoutPost3);
            imageViewPost3 = (ImageView) v.findViewById(R.id.imageViewPost3);
            textViewDescriptionPost3 = (TextView) v.findViewById(R.id.textViewDescriptionPost3);
        }
    }
}