package com.android.API;

import com.android.Global.GlobalStaticData;
import com.android.Models.Article;
import com.android.Models.Category;
import com.android.Models.Comment;
import com.android.Models.FeedbackComment;
import com.android.Models.Tag;
import com.android.Models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class APIFunction {
    static APIService apiService = ServiceGenerator.createService(APIService.class);
    private static APIFunction apiFunction = null;

    public static APIFunction getInstance() {
        if(apiFunction==null){
            apiFunction = new APIFunction();
        }
        return apiFunction;
    }

    public APIFunction() {
    }

    public String getUrlImage(String url){
        String urlImage=url;
        if(!urlImage.contains("http")){
            urlImage = GlobalStaticData.URL_HOST+urlImage;
        }
        return urlImage;
    }

    //Category
    public List<Category> getListCategory(){
        List<Category> listCategory = new ArrayList<>();
        final Call<List<Category>> call = apiService.getListCategory();
        try {
            listCategory= call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCategory;
    }


    //Article
    public List<Article>getListArticle(){
        List<Article> listArticle = new ArrayList<>();
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<List<Article>> call = apiService.getListArticle();
        try {
            listArticle= call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    public List<Article> getListArticle_byCategoryID(String categoryID){
        List<Article> listArticle = new ArrayList<>();
        final Call<List<Article>> call = apiService.getListArticle_byCategoryID(categoryID);
        try {
            listArticle= call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    public List<Article> getListArticle_related(String articleID){
        List<Article> listArticle = new ArrayList<>();
        final Call<List<Article>> call = apiService.getListArticle_related(articleID);
        try {
            listArticle= call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    //User
    public User getUser_byID(String userID){
        User user = null;
        final Call<User> call = apiService.getUser_byID(userID);
        try {
            user= call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    //Comment
    public List<Comment> getListComment_byArticleID(String articleID) {
        List<Comment> listComment = new ArrayList<>();
        Call<List<Comment>> call = apiService.getListComment_byArticleID(articleID);
        try {
            listComment = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listComment;
    }
    public List<FeedbackComment> getListFeedback_byCommentID(String commentID) {
        List<FeedbackComment> listFeedback = new ArrayList<>();
        Call<List<FeedbackComment>> call = apiService.getListFeedback_byCommentID(commentID);
        try {
            listFeedback = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listFeedback;
    }

    //Tag
    public List<Tag>getListTag_byArticleID(String articleID){
        List<Tag> listTag = new ArrayList<>();
        Call<List<Tag>> call = apiService.getListTag_byArticleID(articleID);
        try {
            listTag = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listTag;
    }

    //User
    public User loginUserByEmail(String email, String password) {
        User user = null;
        Call<User> call = apiService.loginUserByEmail(email,password);
        try {
            user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    //Comment
    public ResponseModel insertComment(Comment comment) {
        ResponseModel response = null;
        Call<ResponseModel> call = apiService.insertComment(comment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
