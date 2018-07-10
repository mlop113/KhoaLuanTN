package com.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.aigestudio.wheelpicker.widgets.WheelDayPicker;
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.android.API.APIFunction;
import com.android.Activity_Fragment.Hot_Fragment;
import com.android.Activity_Fragment.LoginDialogActivity;
import com.android.Activity_Fragment.PostsOnRequestActivity;
import com.android.Activity_Fragment.Profile_Activity;
import com.android.Adapters.CategoryAdapter;
import com.android.Adapters.MyFragmentPagerAdapter;
import com.android.BroadcastReceiver.NetworkChangeReceiver;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Interface.IOnClickCategory;
import com.android.Interface.IOnClickFilter;
import com.android.Models.Article;
import com.android.Models.Category;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joooonho.SelectableRoundedImageView;

import org.droidparts.widget.ClearableEditText;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements IOnClickCategory, ViewPager.OnPageChangeListener, View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private final String CLIENT_SEND_IMAGE = "CLIENT_SEND_IMAGE";
    private final String SERVER_SEND_IMAGE = "SERVER_SEND_IMAGE";
    AppPreferences appPreferences;
    InputMethodManager inputMethodManager;
    //actionbar
    SelectableRoundedImageView imageButtonPlus;
    DrawerLayout drawer;
    //drawer and listCategory
    private RecyclerView recyclerViewCategory;
    List<Category> listCategory = new ArrayList<>();
    List<Article> listArticleRequest;
    CategoryAdapter categoryAdapter;
    ClearableEditText editTextSearch;
    TextView tvHomeStatus;

    //main
    Hot_Fragment hot_fragment = new Hot_Fragment();


    List<Fragment> fragmentList;

    MyFragmentPagerAdapter myFragmentPagerAdapter;
    //tag hot & view pager
    TabHost tabHost;

    ViewPager viewPager;
    //WheelDPicker
    WheelDayPicker wheelDayPicker;
    WheelMonthPicker wheelMonthPicker;
    WheelYearPicker wheelYearPicker;

    //interface click filter
    public static IOnClickFilter iOnClickFilterHot;
    public static IOnClickFilter iOnClickFilterNew;
    public static IOnClickFilter iOnClickClearFilterHot;
    public static IOnClickFilter iOnClickClearFilterNew;
    boolean isFilterHot = false;
    boolean isFilterNew = false;
    private String p, u, i;
    DatabaseReference databaseReference;

    //intent
    Intent intentPostsOnReQuest;

    //dialog wait
    SpotsDialog progressDialog;

    APIFunction apiFunction;
    BroadcastReceiver updateNetworkReciver;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cap quyen bo qua loi nullthread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        appPreferences = AppPreferences.getInstance(this);
        //connect firebase
        //fireBase();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //set tollbar and displaytitile
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //drawer contain layoutuser and listCategory
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                inputMethodManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //tạo viewpager
        initViewPager();
        //tạo tabhot
        //  inittabhot();
        //show menu "+"
        initMenuplus();

        apiFunction = new APIFunction();
        initIntent();
        mappings();
        initView();

        NetworkChangeReceiver.register(this);
        registerUpdateNetworkReciver();

    }


    private void initIntent() {
        intentPostsOnReQuest = new Intent(this, PostsOnRequestActivity.class);
    }

    private void mappings() {
        //header contean Layoutuser
        editTextSearch = (ClearableEditText) findViewById(R.id.editTextSearch);
        //listCategory
        recyclerViewCategory = (RecyclerView) findViewById(R.id.recyclerViewCategory);
    }

    private void initView() {
        tvHomeStatus = findViewById(R.id.tv_home_status);
        updateHomeStatus(GlobalFunction.isNetworkAvailable(this));

        listCategory = apiFunction.getListCategory();
        categoryAdapter = new CategoryAdapter(this, listCategory);
        categoryAdapter.setiOnClickCategory(this);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategory.setAdapter(categoryAdapter);
        progressDialog = new SpotsDialog(this, R.style.CustomAlertDialog);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPost(editTextSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    /*private void logout() {
        if (appPreferences.isLoginWithGoogle()) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
        }
        //appPreferences.setUserId("");
        Toast.makeText(MainActivity.this, "Đã đăng xuất tài khoản!", Toast.LENGTH_SHORT).show();
        appPreferences.setLogin(false);


    }*/

    private void searchPost(final String strSearch) {
        progressDialog.show();
        new SearchAsyncTask().execute(strSearch);
    }

    class SearchAsyncTask extends AsyncTask<String, Void, List<Article>> {
        String strSearch = null;

        @Override
        protected List<Article> doInBackground(String... strings) {
            strSearch = strings[0];
            List<Article> listSearch = new ArrayList<>();
            if (strSearch != null) {
                listSearch = apiFunction.searchArticle(strSearch);
            }
            return listSearch;
        }

        @Override
        protected void onPostExecute(List<Article> listArticle) {
            super.onPostExecute(listArticle);
            if (listArticle != null) {
                Intent intentSearch = new Intent(MainActivity.this, PostsOnRequestActivity.class);
                intentSearch.putExtra(AppConfig.LISTPOST, (ArrayList) listArticle);
                intentSearch.putExtra(AppConfig.BARNAME, strSearch);
                startActivity(intentSearch);
            }
            progressDialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //tạo viewpager
    private void initViewPager() {
        ///tạo list pager
        fragmentList = new ArrayList<>();
        fragmentList.add(hot_fragment);


        myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentList);

        viewPager = (ViewPager) findViewById(R.id.home_view_pager);

        //đưa list payger vào viewpayger
        viewPager.setAdapter(myFragmentPagerAdapter);
        //băt sự kiện trượt viewpager
        viewPager.addOnPageChangeListener(this);

    }
    //tạo tabhost
//    private void inittabhot()
//    {
//        tabHost = (TabHost)findViewById(R.id.tabhost);
//        tabHost.setup();
//        //tạo số lượng và tên tabhot
//        String[] tabnames = {""};
//        for(String t:tabnames){
//            TabHost.TabSpec tabSpec;
//            tabSpec = tabHost.newTabSpec(t);
//            tabSpec.setContent(new FakeContent(this));
//            TextView tv = new TextView(this);
//            tv.setWidth(200);
//            tv.setHeight(200);
//            tv.setTextSize(11);
//            tabSpec.setIndicator(tv);
//            tabHost.addTab(tabSpec);
//        }
//        //bắt sự kiện click thay đổi tab
//        tabHost.setOnTabChangedListener(this);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
    }


    //funtion of Interface implement click item
    @Override
    public void onClickCategory(final Category category) {
        //close drawer contain listcategory
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        //Toast.makeText(this, "clicked category id="+category.getName(), Toast.LENGTH_SHORT).show();

        //PostsOnRequestActivity.listPost.clear();
        //GlobalStaticData.listPost.clear();
        progressDialog.show();
        listArticleRequest = apiFunction.getListArticle_byCategoryID(category.getCategoryID());
        //GlobalFunction.saveOffLineList(this, listArticleRequest);

        intentPostsOnReQuest.putExtra(AppConfig.BARNAME, category.getName());
        intentPostsOnReQuest.putExtra(AppConfig.LISTPOST, (ArrayList) listArticleRequest);
        startActivity(intentPostsOnReQuest);
        progressDialog.dismiss();


    }

    //thiết lập tab của viewpager để trượt qua
    public class FakeContent implements TabHost.TabContentFactory {
        Context context;

        public FakeContent(Context mcontext) {
            context = mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeview = new View(context);
            fakeview.setMinimumHeight(0);
            fakeview.setMinimumWidth(0);
            return fakeview;
        }
    }
    /*
        Viewpager listener
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //khi trượt thay đổi trang hiện tại của viewpager thì tab hiện tại của tabhot cũng thay đổi theo
    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /*
            Tabhost listener
            bắt sự kiện thay đổi tab
         */
//    @Override
//    public void onTabChanged(String tabId) {
//        TextView textViewNew = (TextView) findViewById(R.id.textViewNew);
//        TextView textViewHot = (TextView)findViewById(R.id.textViewHot);
//
//        int selectedPage = tabHost.getCurrentTab();
//        //sét các màu của tab widget
//        if(selectedPage==0) {
//            textViewHot.setBackgroundResource(R.drawable.tabs_pressed_left);
//            textViewHot.setTextColor(getResources().getColor(R.color.textcolor_selected));
//
//            textViewNew.setBackgroundResource(R.drawable.tabs_normal_right);
//            textViewNew.setTextColor(getResources().getColor(R.color.white));
//        }else{
//            textViewHot.setBackgroundResource(R.drawable.tabs_normal_left);
//            textViewHot.setTextColor(getResources().getColor(R.color.white));
//
//            textViewNew.setBackgroundResource(R.drawable.tabs_pressed_right);
//            textViewNew.setTextColor(getResources().getColor(R.color.textcolor_selected));
//        }
//        viewPager.setCurrentItem(selectedPage);
//        GlobalStaticData.setCurrentPage(selectedPage);
//    }

    //tạo Diaglog ic dấu "+"
    private void initMenuplus() {
        imageButtonPlus = findViewById(R.id.imageButtonPlus);
        imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser == null) {
                    startActivity(new Intent(MainActivity.this, LoginDialogActivity.class));
                } else {
                    if (GlobalFunction.isNetworkAvailable(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, Profile_Activity.class));
                    }
                }
            }
        });

    }

    private void updateUser(boolean networkAvailable) {
        if (networkAvailable) {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imageButtonPlus);
            } else {
                imageButtonPlus.setImageResource(R.drawable.icon_user1);
            }
        } else {
            firebaseUser = null;
            imageButtonPlus.setImageResource(R.drawable.icon_user1);
        }
    }


    //funtion clickView: LayoutUser, dialog
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void clearFilter() {
        switch (GlobalStaticData.getCurrentPage()) {
            case 0:
                iOnClickFilterHot.onClickClearFilter();
                isFilterHot = false;
                break;
            case 1:
                iOnClickFilterNew.onClickClearFilter();
                isFilterNew = false;
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (GlobalStaticData.getCurrentPage() == 0)
                    hot_fragment.smoothScrollToTop();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUser(GlobalFunction.isNetworkAvailable(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkChangeReceiver.unregister(this);
        unregisterUpdateNetworkReciver();
    }

    private void registerUpdateNetworkReciver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConfig.BROADCAST_UPDATE_UI);
            updateNetworkReciver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        boolean isNetworkAvailable = intent.getBooleanExtra(AppConfig.BROADCAST_NETWORK_AVAILABLE, true);
                        updateNetWork(isNetworkAvailable);
                    }
                }
            };
            registerReceiver(updateNetworkReciver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void unregisterUpdateNetworkReciver() {
        try {
            unregisterReceiver(updateNetworkReciver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateHomeStatus(boolean isVisibility) {
        if (tvHomeStatus != null) {
            tvHomeStatus.setVisibility(isVisibility ? View.GONE : View.VISIBLE);
        }
    }

    private void updateCategoryNetwork(boolean isAvailable) {
        if (isAvailable) {
            listCategory = apiFunction.getListCategory();
        } else {
            listCategory.clear();
        }
        categoryAdapter.setData(listCategory);
    }

    private void updateNetWork(boolean isAvailable) {
        updateHomeStatus(isAvailable);
        hot_fragment.updateNetwork(isAvailable);
        updateCategoryNetwork(isAvailable);
        updateUser(isAvailable);
    }

}
