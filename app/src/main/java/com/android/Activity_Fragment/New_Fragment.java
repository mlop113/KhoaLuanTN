package com.android.Activity_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.API.APIFunction;
import com.android.Adapters.PostsOnRequestAdapter;
import com.android.Interface.IOnClickFilter;
import com.android.MainActivity;
import com.android.Models.Article;
import com.android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class New_Fragment extends Fragment implements IOnClickFilter {
    //RecyclerView summary
    public static RecyclerView recyclerViewSummary;
    public static PostsOnRequestAdapter postsOnRequestAdapter;
    static List<Article> listArticle = new ArrayList<>();
    LayoutInflater inflater;
    View v;
    DatabaseReference databaseReference;
    APIFunction apiFunction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.inflater = inflater;

        //interface
        MainActivity.iOnClickFilterNew = New_Fragment.this;
        MainActivity.iOnClickClearFilterNew = New_Fragment.this;
        apiFunction = new APIFunction();
        mappings();
        initView();
        event();
        return v;
    }

    private void mappings() {
        //RecyclerView summary
        recyclerViewSummary = (RecyclerView) v.findViewById(R.id.recyclerViewSummary);

    }

    private void initView() {
        //RecyclerView summary
        recyclerViewSummary.setLayoutManager(new LinearLayoutManager(getContext()));
        listArticle = apiFunction.getListArticle();
        postsOnRequestAdapter = new PostsOnRequestAdapter(getContext(), listArticle);
        recyclerViewSummary.setAdapter(postsOnRequestAdapter);
    }

    private void event() {
    }

    @Override
    public void onClickFilter(final String date) {
        listArticle = new ArrayList<>();
        postsOnRequestAdapter.setData(listArticle);
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPost = new ArrayList<>();
                for(DataSnapshot dataPost:dataSnapshot.getChildren()){
                    Post post = dataPost.getValue(Post.class);
                    if(GlobalFunction.filter(post,date))
                    {
                        listPost.add(post);
                        postsOnRequestAdapter.setData(listPost);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onClickClearFilter() {
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPost=new ArrayList<Post>();
                for(DataSnapshot datapost:dataSnapshot.getChildren()){
                    listPost.add(datapost.getValue(Post.class));
                    postsOnRequestAdapter.setData(listPost);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
