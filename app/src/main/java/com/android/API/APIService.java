package com.android.API;

import com.android.Models.Article;
import com.android.Models.Category;
import com.android.Models.Comment;
import com.android.Models.FeedbackComment;
import com.android.Models.Tag;
import com.android.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    //Category
    @GET("api/categories/getlist")
    Call<List<Category>> getListCategory();

    //Article
    @GET("api/news/getlist")
    Call<List<Article>> getListArticle();
    @GET("api/news/getlist/{categoryid}")
    Call<List<Article>> getListArticle_byCategoryID(@Path("categoryid") String categoryID);
    @GET("api/news/getlist/related/{articleid}")
    Call<List<Article>> getListArticle_related(@Path("articleid") String articleID);

    //User
    @GET("api/user/getlist")
    Call<List<User>> getListUser();
    @GET("api/user/{userid}")
    Call<User> getUser_byID(@Path("userid") String userID);
    @POST("api/user/login/{email}/{password}")
    Call<User> loginUserByEmail(@Path("email") String email, @Path("password") String password);

    //Comment
    @GET("api/comment/getlist/{articleid}")
    Call<List<Comment>> getListComment_byArticleID(@Path("articleid") String articleID);
    @GET("api/feedbackcomment/getlist/{commentid}")
    Call<List<FeedbackComment>> getListFeedback_byCommentID(@Path("commentid") String commentID);
    @POST("api/comment/insert")
    Call<ResponseModel> insertComment(@Body Comment comment);

    //Tag
    @GET("api/tag/getlist/{articleid}")
    Call<List<Tag>> getListTag_byArticleID(@Path("articleid") String articleID);


}
