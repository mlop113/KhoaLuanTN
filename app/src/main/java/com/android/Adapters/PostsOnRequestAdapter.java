package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Activity_Fragment.PostDetailActivity;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Models.Article;
import com.android.Models.Category;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostsOnRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Article> listArticle = new ArrayList<>();
    AppPreferences appPreferences;
    APIFunction apiFunction;
    List<Category> listCategory = new ArrayList<>();
    public PostsOnRequestAdapter(Context context, List<Article> listArticle) {
        this.context = context;
        this.listArticle = listArticle;
        appPreferences = AppPreferences.getInstance(context);
        apiFunction = new APIFunction();
        listCategory = apiFunction.getListCategory();

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
        itemViewHolder.textViewDescription.setText(article.getDescription());
        itemViewHolder.textViewCategory.setText(getCagoryName(article.getCategoryID()));
        itemViewHolder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(article.getDateCreate()));

        itemViewHolder.relativeLayoutSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST, article);
                context.startActivity(intent);
            }
        });
    }

    private String getCagoryName(String categoryId) {
        for (Category category : listCategory) {
            if (categoryId.equals(category.getCategoryID())) {
                return category.getName();
            }
        }
        return "";
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
        TextView textViewDescription;
        TextView textViewCategory;
        TextView textViewTimeAgo;


        public ItemViewHolder( View v) {
            super(v);
            this.v = v;
            this.relativeLayoutSummary = (RelativeLayout) v.findViewById(R.id.relativeLayoutSummary);
            imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewBarName);
            textViewCategory = (TextView) itemView.findViewById(R.id.tv_category);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.tv_time_ago);


        }
    }


}
