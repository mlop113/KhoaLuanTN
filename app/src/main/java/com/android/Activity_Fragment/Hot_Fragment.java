package com.android.Activity_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.API.APIFunction;
import com.android.Adapters.SummaryAdapter;
import com.android.Adapters.TagAdapter;
import com.android.Interface.IOnClickFilter;
import com.android.MainActivity;
import com.android.Models.Article;
import com.android.Models.Category;
import com.android.R;
import com.android.RetrofitServices.Models_R.WeaService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Hot_Fragment extends Fragment implements IOnClickFilter {
    private WeaService weaService;
    //listdata article
    List<Article> listArticle = new ArrayList<>();
    //listdata category
    List<Category> listCategory = new ArrayList<>();
    //RecyclerView summary
    public static RecyclerView recyclerViewSummary;
    public static SummaryAdapter summary_adapter;
    LayoutInflater inflater;
    View v;
    //tag
    LinearLayout linearLayoutTag;
    RecyclerView recyclerViewTag;
    TagAdapter tagAdapter;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference databaseReference;

    APIFunction apiFunction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_hot,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        //Tag
        linearLayoutTag = (LinearLayout) v.findViewById(R.id.linearLayoutTag);
        recyclerViewTag = (RecyclerView) v.findViewById(R.id.recyclerViewTag);

        //RecyclerView summary
        recyclerViewSummary = (RecyclerView) v.findViewById(R.id.recyclerViewSummary);
    }

    private void initView() {
        listArticle = apiFunction.getListArticle();
        listCategory = apiFunction.getListCategory();
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        linearLayoutManager.setStackFromEnd(true);

       /* weaService= Api_Utils.getSOService();
        weaService.getAnswers().enqueue(new Callback<ResponeServices>() {
            @Override
            public void onResponse(Call<ResponeServices> call, Response<ResponeServices> response) {
                if (response.isSuccessful())
                {
                    tagAdapter = new TagAdapter(getContext(),response.body().getQuery().getResults().getChannel().getItem().getForecast();
                }
            }

            @Override
            public void onFailure(Call<ResponeServices> call, Throwable t) {

            }
        });*/
       //tagAdapter = new TagAdapter(getContext(),GlobalStaticData.listTag);
        recyclerViewTag.setLayoutManager(linearLayoutManager);
       recyclerViewTag.setAdapter(tagAdapter);

        //RecyclerView summary
        recyclerViewSummary.setLayoutManager(new LinearLayoutManager(getContext()));
        summary_adapter = new SummaryAdapter(getContext(),listArticle,listCategory);

        recyclerViewSummary.setAdapter(summary_adapter);
    }




    @Override
    public void onClickFilter(final String date) {
        /*summary_adapter.setListArticle(listArticle);
        if(GlobalFunction.filter(post,date))
        {
            listPost.add(post);
            summary_adapter.setListPost(listPost);
        }*/
    }

    @Override
    public void onClickClearFilter() {
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPost=new ArrayList<Post>();
                for(DataSnapshot datapost:dataSnapshot.getChildren()){
                    listPost.add(datapost.getValue(Post.class));
                    summary_adapter.setListPost(listPost);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }


}
