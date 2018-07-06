package com.android.API;

import com.android.Models.Article;
import com.android.Models.Article_UserModel;
import com.android.Models.Category;
import com.android.Models.Comment;
import com.android.Models.Comment_UserModel;
import com.android.Models.FeedbackComment;
import com.android.Models.ReportComment;
import com.android.Models.ReportFeedbackComment;
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

    @POST("api/comment/insert")
    Call<Response> insertComment(@Body Comment comment);

    @POST("api/comment/update")
    Call<Response> updateComment(@Body Comment comment);

    @POST("api/comment/delete")
    Call<Response> deleteComment(@Body Comment comment);

    @POST("api/comment/report")
    Call<Response> reportComment(@Body ReportComment reportComment);

    //Tag
    @GET("api/tag/getlist/{articleid}")
    Call<List<Tag>> getListTag_byArticleID(@Path("articleid") String articleID);


    //Bookmark
    @GET("api/bookmark/{articleid}/{userid}")
    Call<Article_UserModel> getBookmarkByArticle_User(@Path("articleid") String articleID, @Path("userid") String UserID);

    @GET("api/bookmark/{userid}")
    Call<List<Article>> getListBookmarkByUser(@Path("userid") String UserID);

    @POST("api/bookmark/insert")
    Call<Response> insertBookmark(@Body Article_UserModel article_user);

    @POST("api/bookmark/delete")
    Call<Response> deleteBookmark(@Body Article_UserModel article_user);

    //like article
    @GET("api/news/like/{articleid}/{userid}")
    Call<Article_UserModel> getLikeByArticle_User(@Path("articleid") String articleID, @Path("userid") String UserID);

    @POST("api/news/like/insert")
    Call<Response> insertLikeAritcle(@Body Article_UserModel article_user);

    @POST("api/news/like/delete")
    Call<Response> deleteLikeAritcle(@Body Article_UserModel article_user);

    //like commnet
    @GET("api/comment/like/{commentid}")
    Call<List<Comment_UserModel>> getListLikeComment(@Path("commentid") String commentID);

    @GET("api/comment/like/{commentid}/{userid}")
    Call<Comment_UserModel> getLikeByComment_User(@Path("commentid") String commentID, @Path("userid") String UserID);

    @POST("api/comment/like/insert")
    Call<Response> insertLikeComment(@Body Comment_UserModel comment_user);

    @POST("api/comment/like/delete")
    Call<Response> deleteLikeComment(@Body Comment_UserModel comment_user);

    //feed back
    @GET("api/feedbackcomment/{commentid}")
    Call<List<FeedbackComment>> getListFeedback_byCommentID(@Path("commentid") String commentID);

    @POST("api/feedbackcomment/insert")
    Call<Response> insertFeedbackComment(@Body FeedbackComment feedbackComment);

    @POST("api/feedbackcomment/update")
    Call<Response> updateFeedbackComment(@Body FeedbackComment feedbackComment);

    @POST("api/feedbackcomment/delete")
    Call<Response> deleteFeedbackComment(@Body FeedbackComment feedbackComment);

    @POST("api/feedbackcomment/report")
    Call<Response> reportFeedbackComment(@Body ReportFeedbackComment reportFeedbackComment);

    //User
    @POST("api/user/login")
    Call<Response> loginUser(@Body User user);
}
