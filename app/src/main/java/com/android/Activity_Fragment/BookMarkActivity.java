package com.android.Activity_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Adapters.PostsOnRequestAdapter;
import com.android.Global.AppConfig;
import com.android.Models.Article;
import com.android.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class BookMarkActivity extends AppCompatActivity implements View.OnClickListener {
    public static List<Article> listArticle = new ArrayList<>();
    String barname="";
    //back
    LinearLayout linearLayoutBack;
    //barname
    TextView textViewBarName;
    //FAB
    FloatingActionButton fab;

    //recyclerViewPostOnReQuest
    RecyclerView recyclerViewPostOnReQuest;
    public static PostsOnRequestAdapter postsOnRequestAdapter;


    ValueEventListener valueEventListener;

    //dialog wait
    SpotsDialog progressDialog;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        overridePendingTransitionEnter();

        intent = getIntent();
        if(intent!=null)
        {
            listArticle=new ArrayList<Article>();
            listArticle =  (List<Article>) intent.getSerializableExtra(AppConfig.LISTPOST);
            barname = intent.getStringExtra(AppConfig.BARNAME);
        }
        else
        {
            Toast.makeText(this, "Error data transfer", Toast.LENGTH_SHORT).show();
            finish();
        }

        mappings();
        initViews();
        events();
    }
    private void mappings() {
        //back
        linearLayoutBack = (LinearLayout) findViewById(R.id.linearLayoutBookmark);
        //Barname
        textViewBarName = (TextView) findViewById(R.id.textViewBarName);
        //FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //recycler
        recyclerViewPostOnReQuest= (RecyclerView) findViewById(R.id.recyclerViewPostOnReQuest);
    }

    private void initViews() {
        //Barname
        textViewBarName.setText(barname);
        //recyclerview listpost
        recyclerViewPostOnReQuest.setLayoutManager(new LinearLayoutManager(this));
        postsOnRequestAdapter = new PostsOnRequestAdapter(BookMarkActivity.this,listArticle);
        recyclerViewPostOnReQuest.setAdapter(postsOnRequestAdapter);
        progressDialog = new SpotsDialog(this,R.style.CustomAlertDialog);


    }


    private void events() {
        linearLayoutBack.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayoutBookmark:
                finish();
                break;
            case R.id.fab:
                recyclerViewPostOnReQuest.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
