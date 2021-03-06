package com.android.Activity_Fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.API.APIFunction;
import com.android.Adapters.SummaryAdapter;
import com.android.DBHelper.DatabaseHelper;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Interface.IOnClickFilter;
import com.android.MainActivity;
import com.android.Models.Article;
import com.android.Models.Category;
import com.android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Hot_Fragment extends Fragment implements IOnClickFilter {
    SwipeRefreshLayout swipeRefresh;
    //listdata article
    List<Article> listArticle = new ArrayList<>();
    //listdata category
    List<Category> listCategory = new ArrayList<>();
    //RecyclerView summary
    RecyclerView recyclerViewSummary;
    SummaryAdapter summary_adapter;
    LayoutInflater inflater;
    View v;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference databaseReference;

    APIFunction apiFunction;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_hot, container, false);

        this.inflater = inflater;
        //interface
        MainActivity.iOnClickFilterHot = Hot_Fragment.this;
        MainActivity.iOnClickClearFilterHot = Hot_Fragment.this;
        apiFunction = new APIFunction();
        mappings();
        initView();
        return v;
    }

    private void mappings() {
        //sroll to top
        swipeRefresh = v.findViewById(R.id.swipp_refresh);
        //RecyclerView summary
        recyclerViewSummary = (RecyclerView) v.findViewById(R.id.recyclerViewSummary);
    }

    private void initView() {
        if (GlobalFunction.isNetworkAvailable(getActivity())) {
            listArticle = apiFunction.getListArticle();
            new SaveDataOffLineTask().execute(listArticle.toArray(new Article[listArticle.size()]));
        } else {
            listArticle = databaseHelper.getListArticle();
        }
        listCategory = apiFunction.getListCategory();
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);

        //RecyclerView summary
        recyclerViewSummary.setLayoutManager(new LinearLayoutManager(getContext()));
        summary_adapter = new SummaryAdapter(getContext(), listArticle, listCategory);

        recyclerViewSummary.setAdapter(summary_adapter);

        swipeRefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateNetwork(GlobalFunction.isNetworkAvailable(getActivity()));
                swipeRefresh.setRefreshing(false);
                updateNetwork(GlobalFunction.isNetworkAvailable(getActivity()));
            }
        });
    }

    public void smoothScrollToTop() {
        recyclerViewSummary.smoothScrollToPosition(0);
    }

    @Override
    public void onClickFilter(final String date) {

    }

    @Override
    public void onClickClearFilter() {

    }

    public void updateNetwork(boolean isAvaiable) {
        if (isAvaiable) {
            if (listArticle.size()<=0 || listArticle==null) {
                listArticle = apiFunction.getListArticle();
                new SaveDataOffLineTask().execute(listArticle.toArray(new Article[listArticle.size()]));
                summary_adapter.setListArticle(listArticle);
            }
            listCategory = apiFunction.getListCategory();
        } else {
            listArticle = databaseHelper.getListArticle();
            listCategory.clear();
            summary_adapter.setListArticle(listArticle);
        }
        summary_adapter.setListCategory(listCategory);
    }


    //Download HTML bằng AsynTask
    private class SaveDataOffLineTask extends AsyncTask<Article, Void, ArrayList<Article>> {

        private static final String TAG = "SaveDataOffLineTask";

        @Override
        protected ArrayList<Article> doInBackground(Article... articles) {
            Document document = null;
            ArrayList<Article> listArticle = new ArrayList<>();
            try {
                for (int i = 0; i < articles.length; i++) {
                    document = (Document) Jsoup.connect(GlobalStaticData.URL_HOST + "news/detail/" + articles[i].getArticleID()).get();
                    if (document != null) {
                        try {
                            //Lấy  html có thẻ như sau: div#latest-news > div.row > div.col-md-6 hoặc chỉ cần dùng  div.col-md-6
                            articles[i].setContentOffLine(document.toString());
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            Bitmap bmp = GlobalFunction.getBitmapFromURL(apiFunction.getUrlImage(articles[i].getCoverImage()));
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            bmp.recycle();
                            articles[i].setCoverImageOffLine(byteArray);

                            listArticle.add(articles[i]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            } catch (IOException e) {
            }
            return listArticle;
        }


        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            super.onPostExecute(articles);
            databaseHelper.deleteAllArticle();
            for (int i = 0; i < articles.size(); i++) {
                databaseHelper.insertArticle(articles.get(i));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
