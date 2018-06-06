package com.android.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Global.GlobalStaticData;
import com.android.Models.Article;
import com.android.Models.Comment;
import com.android.R;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEMVIEWTYPE_CONTENT=0;
    private final int ITEMVIEWTYPE_RECYCLERVIEWTAG=1;
    private final int ITEMVIEWTYPE_RECYCLERVIEWRELATED=2;
    private final int ITEMVIEWTYPE_RECYCLERVIEWCOMMENT=3;
    private Context context;
    private Article article;


    RelatedAdapter related_adapter;
    RelatedAdapter related_adapterCategory;
    CommentAdapter comment_adapter;

    APIFunction apiFunction;
    public PostDetailAdapter(Context context, Article article) {
        this.context = context;
        this.article = article;
        apiFunction = new APIFunction();
        this.related_adapter = new RelatedAdapter(context,apiFunction.getListArticle_related(article.getArticleID()));
        this.related_adapterCategory = new RelatedAdapter(context,apiFunction.getListArticle_byCategoryID(article.getCategoryID()));
        comment_adapter = new CommentAdapter(context,article, apiFunction.getListComment_byArticleID(article.getArticleID()));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case ITEMVIEWTYPE_CONTENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_webview,parent,false);
                return new WebViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWTAG:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewrelated,parent,false);
                return new RelatedViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWRELATED:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewrelated,parent,false);
                return new RelatedViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWCOMMENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewcomment,parent,false);
                return new CommentViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case ITEMVIEWTYPE_CONTENT:
                final WebViewHolder webViewHolder = (WebViewHolder) holder;
                webViewHolder.webView.getSettings().setLoadsImagesAutomatically(true);
                webViewHolder.webView.getSettings().setJavaScriptEnabled(true);
                webViewHolder.webView.getSettings().setBuiltInZoomControls(true);
                webViewHolder.webView.getSettings().setUseWideViewPort(true);
                webViewHolder.webView.getSettings().setLoadWithOverviewMode(true);
                webViewHolder.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webViewHolder.webView.getSettings().setLoadWithOverviewMode(true);
                webViewHolder.webView.getSettings().setUseWideViewPort(true);
                webViewHolder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                webViewHolder.webView.setScrollbarFadingEnabled(false);
                webViewHolder.webView.loadUrl(GlobalStaticData.URL_HOST+"news/detail/"+article.getArticleID());
                break;
            case ITEMVIEWTYPE_RECYCLERVIEWTAG:
                RelatedViewHolder tagViewHolder = (RelatedViewHolder)holder;
                tagViewHolder.recyclerViewRelated.setAdapter(related_adapter);
                tagViewHolder.textViewRelated.setText("Bài viết viên quan");
                if (related_adapter.getItemCount() <= 0) {
                    tagViewHolder.textViewRelated.setVisibility(View.GONE);
                }
                break;
            case ITEMVIEWTYPE_RECYCLERVIEWRELATED:
                RelatedViewHolder categoryViewHolder = (RelatedViewHolder)holder;
                categoryViewHolder.textViewRelated.setText("Bài viết cùng chuyên mục");
                categoryViewHolder.recyclerViewRelated.setAdapter(related_adapterCategory);
                if (related_adapterCategory.getItemCount() <= 0) {
                    categoryViewHolder.textViewRelated.setVisibility(View.GONE);
                }
                break;
            case ITEMVIEWTYPE_RECYCLERVIEWCOMMENT:
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                commentViewHolder.recyclerViewComment.setLayoutManager(new LinearLayoutManager(context));

                commentViewHolder.recyclerViewComment.setAdapter(comment_adapter);

                break;

        }
    }


    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return ITEMVIEWTYPE_CONTENT;
            case 1:
                return ITEMVIEWTYPE_RECYCLERVIEWTAG;
            case 2:
                return ITEMVIEWTYPE_RECYCLERVIEWRELATED;
            case 3:
                return ITEMVIEWTYPE_RECYCLERVIEWCOMMENT;
        }
        return position;
    }


    static class RelatedViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerViewRelated;
        TextView textViewRelated;
        public RelatedViewHolder(View itemView) {
            super(itemView);
            textViewRelated = itemView.findViewById(R.id.textViewRelated);
            recyclerViewRelated = (RecyclerView) itemView.findViewById(R.id.recyclerViewRelated);
            LinearLayoutManager linearLayoutManagerCategory = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,true);
            linearLayoutManagerCategory.setStackFromEnd(true);
            recyclerViewRelated.setLayoutManager(linearLayoutManagerCategory);
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerViewComment;
        public CommentViewHolder(View itemView) {
            super(itemView);
            recyclerViewComment = (RecyclerView) itemView.findViewById(R.id.recyclerViewComment);
        }
    }

    static class WebViewHolder extends RecyclerView.ViewHolder{
        private WebView webView;

        public WebViewHolder(View itemView) {
            super(itemView);
            this.webView = itemView.findViewById(R.id.webView);
        }
    }

    public void addComment(Comment comment){
        comment_adapter.addComment(comment);
        notifyDataSetChanged();
    }
}
