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
import com.android.Activity_Fragment.PostDetailActivity;
import com.android.Global.AppConfig;
import com.android.Models.Article;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder>{
    Context context;
    List<Article> listArticle = new ArrayList<>();
    APIFunction apiFunction;
    public RelatedAdapter(Context context, List<Article> listArticle) {
        this.context = context;
        this.listArticle = listArticle;
        apiFunction = new APIFunction();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final Article article = listArticle.get(position);
        holder.textViewTitile.setText(article.getTitle());
        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST,article);
                context.startActivity(intent);
            }
        });
        try{
            Glide.with(context).load(apiFunction.getUrlImage(article.getCoverImage())).into(holder.imageViewCover);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listArticle.get(position).getArticleID());
    }


    @Override
    public int getItemCount() {
        return listArticle ==null ? 0: listArticle.size();
    }


    public void setData(List<Article> list){
        this.listArticle.clear();
        this.listArticle.addAll(list);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View v;
        LinearLayout linearLayoutItem;
        ImageView imageViewCover;
        TextView textViewTitile;
        public ViewHolder( View v) {
            super(v);
            this.v = v;
            linearLayoutItem = (LinearLayout) v.findViewById(R.id.linearLayoutItem);
            imageViewCover = (ImageView) v.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) v.findViewById(R.id.textViewBarName);
        }
    }
}
