package com.android.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Models.Article;
import com.android.Models.Comment;
import com.android.R;

import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final int ITEMVIEWTYPE_CONTENT = 0;
    private final int ITEMVIEWTYPE_RECYCLERVIEWTAG = 1;
    private final int ITEMVIEWTYPE_RECYCLERVIEWRELATED = 2;
    private final int ITEMVIEWTYPE_RECYCLERVIEWCOMMENT = 3;
    private final int ITEMVIEWTYPE_PROGRESSBAR = 4;
    private int viewTypeCount = 5;

    private Context context;
    private Article article;

    RelatedAdapter related_adapter;
    RelatedAdapter related_adapterCategory;
    CommentAdapter comment_adapter;


    APIFunction apiFunction;
    public Handler handler;
    ProgressBar progressBar;

    public PostDetailAdapter() {
    }

    public PostDetailAdapter(Context context, Article article) {
        this.context = context;
        this.article = article;
        apiFunction = new APIFunction();
        this.related_adapter = new RelatedAdapter(context, removeArticle(apiFunction.getListArticle_related(article.getArticleID()), article));
        this.related_adapterCategory = new RelatedAdapter(context, removeArticle(apiFunction.getListArticle_byCategoryID(article.getCategoryID()), article));
        comment_adapter = new CommentAdapter(context, article, apiFunction.getListComment_byArticleID(article.getArticleID()));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ITEMVIEWTYPE_CONTENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_webview, parent, false);
                return new WebViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWTAG:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewrelated, parent, false);
                return new RelatedViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWRELATED:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewrelated, parent, false);
                return new RelatedViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWCOMMENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewcomment, parent, false);
                return new CommentViewHolder(view);
            case ITEMVIEWTYPE_PROGRESSBAR:
                view = LayoutInflater.from(context).inflate(R.layout.progressbar, parent, false);
                return new ProgressBarHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case ITEMVIEWTYPE_CONTENT:
                final WebViewHolder webViewHolder = (WebViewHolder) holder;
                webViewHolder.webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

                if (!GlobalFunction.isNetworkAvailable(context)) { // loading offline
                    webViewHolder.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                }

                webViewHolder.webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onLoadResource(WebView view, String url) {
                        Log.d(TAG, "onLoadResource: ");
                        // Check to see if there is a progress dialog
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // Page is done loading;
                        // hide the progress dialog and show the webview
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Log.d(TAG, "onReceivedError: " + description
                        );
                        view.loadDataWithBaseURL(null, article.getContentOffLine(), "text/html",
                                "UTF-8", null);
                        view.setWebViewClient(null);
                    }

                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                        // Redirect to deprecated method, so you can use it in all SDK versions
                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                    }
                });

                webViewHolder.webView.loadUrl(GlobalStaticData.URL_HOST + "news/detail/" + article.getArticleID());

                break;
            case ITEMVIEWTYPE_RECYCLERVIEWTAG:
                final RelatedViewHolder tagViewHolder = (RelatedViewHolder) holder;
                tagViewHolder.recyclerViewRelated.setAdapter(related_adapter);
                tagViewHolder.textViewRelated.setText("Bài viết viên quan");
                if (related_adapter.getItemCount() <= 0) {
                    tagViewHolder.textViewRelated.setVisibility(View.GONE);
                }
            case ITEMVIEWTYPE_RECYCLERVIEWRELATED:
                final RelatedViewHolder categoryViewHolder = (RelatedViewHolder) holder;
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
            case ITEMVIEWTYPE_PROGRESSBAR:
                ProgressBarHolder progressBarHolder = (ProgressBarHolder) holder;
                progressBar = progressBarHolder.progressBar;
                progressBar.setVisibility(View.GONE);
                break;

        }
    }

    public int getCommentItemCount() {
        return comment_adapter.getItemCount();
    }

    public void addComment(Comment comment) {
        comment_adapter.addComment(comment);
    }

    public void updateComment(Comment comment, int position) {
        comment_adapter.updateComment(comment, position);
    }

    public void reLoad(){
        comment_adapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    private boolean isLoading;

    public void loadMore() {
        Log.d(TAG, "getItemCount: " + comment_adapter.getItemCount());
        Log.d(TAG, "getListFull: " + comment_adapter.getListFull().size());
        if (!isLoading && comment_adapter.checkAvalidableLoadMore()) {
            if (progressBar != null) {
                isLoading = true;
                progressBar.setVisibility(View.VISIBLE);
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        comment_adapter.loadMore();
                        progressBar.setVisibility(View.GONE);
                        isLoading = false;
                    }
                }, 3000);
            }
        }
    }

    @Override
    public int getItemCount() {
        return viewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEMVIEWTYPE_CONTENT;
            case 1:
                return ITEMVIEWTYPE_RECYCLERVIEWTAG;
            case 2:
                return ITEMVIEWTYPE_RECYCLERVIEWRELATED;
            case 3:
                return ITEMVIEWTYPE_RECYCLERVIEWCOMMENT;
            case 4:
                return ITEMVIEWTYPE_PROGRESSBAR;

        }
        return position;
    }

    private List<Article> removeArticle(List<Article> list, Article article) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getArticleID().equals(article.getArticleID())) {
                list.remove(i);
                return list;
            }
        }
        return list;
    }


    static class RelatedViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewRelated;
        TextView textViewRelated;

        public RelatedViewHolder(View itemView) {
            super(itemView);
            textViewRelated = itemView.findViewById(R.id.textViewRelated);
            recyclerViewRelated = (RecyclerView) itemView.findViewById(R.id.recyclerViewRelated);
            LinearLayoutManager linearLayoutManagerCategory = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, true);
            linearLayoutManagerCategory.setStackFromEnd(true);
            recyclerViewRelated.setLayoutManager(linearLayoutManagerCategory);
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            recyclerViewComment = (RecyclerView) itemView.findViewById(R.id.recyclerViewComment);
        }
    }

    static class WebViewHolder extends RecyclerView.ViewHolder {
        private WebView webView;

        public WebViewHolder(View itemView) {
            super(itemView);
            this.webView = itemView.findViewById(R.id.webView);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);

            webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
            webView.getSettings().setAppCachePath(itemView.getContext().getCacheDir().getAbsolutePath());
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }

    static class ProgressBarHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressBarHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
