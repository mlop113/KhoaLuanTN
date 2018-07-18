package com.android.API;

import android.util.Log;

import com.android.Global.GlobalStaticData;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class APIFunction {
    static APIService apiService = ServiceGenerator.createService(APIService.class);
    private static APIFunction apiFunction = null;

    public static APIFunction getInstance() {
        if (apiFunction == null) {
            apiFunction = new APIFunction();
        }
        return apiFunction;
    }

    public APIFunction() {
    }

    public String getUrlImage(String url) {
        String urlImage = url;
        if (!urlImage.contains("http")) {
            urlImage = GlobalStaticData.URL_HOST + urlImage;
        }
        return urlImage;
    }

    //Category
    public List<Category> getListCategory() {
        List<Category> listCategory = new ArrayList<>();
        final Call<List<Category>> call = apiService.getListCategory();
        try {
            listCategory = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCategory;
    }


    //Article
    public List<Article> getListArticle() {
        List<Article> listArticle = new ArrayList<>();
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<List<Article>> call = apiService.getListArticle();
        try {
            listArticle = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    public List<Article> getListArticle_byCategoryID(String categoryID) {
        List<Article> listArticle = new ArrayList<>();
        final Call<List<Article>> call = apiService.getListArticle_byCategoryID(categoryID);
        try {
            listArticle = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    public List<Article> getListArticle_related(String articleID) {
        List<Article> listArticle = new ArrayList<>();
        final Call<List<Article>> call = apiService.getListArticle_related(articleID);
        try {
            listArticle = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    public List<Article> searchArticle(String strSearch) {
        List<Article> listArticle = new ArrayList<>();
        final Call<List<Article>> call = apiService.searchArticle(strSearch);
        try {
            listArticle = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    //User
    public User getUser_byID(String userID) {
        User user = null;
        final Call<User> call = apiService.getUser_byID(userID);
        try {
            user = call.execute().body();
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

    public Response insertComment(Comment comment) {
        Response response = null;
        Call<Response> call = apiService.insertComment(comment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response updateComment(Comment comment) {
        Response response = null;
        Call<Response> call = apiService.updateComment(comment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response deleteComment(Comment comment) {
        Response response = null;
        Call<Response> call = apiService.deleteComment(comment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response reportComment(ReportComment reportComment) {
        Response response = null;
        Call<Response> call = apiService.reportComment(reportComment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    //Tag
    public List<Tag> getListTag_byArticleID(String articleID) {
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
        Call<User> call = apiService.loginUserByEmail(email, password);
        try {
            user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    /*Bookmark*/
    public List<Article> getListBookmarkByUser(String userID) {
        List<Article> listArticle = new ArrayList<>();
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<List<Article>> call = apiService.getListBookmarkByUser(userID);
        try {
            listArticle = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("getListBookmarkByUser", "listArticle: " + listArticle.size());
        return listArticle;
    }

    public boolean checkBoorkmark(String articleID, String userID) {
        Article_UserModel article_user = null;
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<Article_UserModel> call = apiService.getBookmarkByArticle_User(articleID, userID);
        try {
            article_user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return article_user != null;
    }

    public Response insertBookmark(Article_UserModel article_user) {
        Response response = null;
        Call<Response> call = apiService.insertBookmark(article_user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response deleteBookmark(Article_UserModel article_user) {
        Response response = null;
        Call<Response> call = apiService.deleteBookmark(article_user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*Like Article*/
    public boolean checkLikeArticle(String articleID, String userID) {
        Article_UserModel article_user = null;
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<Article_UserModel> call = apiService.getLikeByArticle_User(articleID, userID);
        try {
            article_user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return article_user != null;
    }

    public Response insertLikeArticle(Article_UserModel article_user) {
        Response response = null;
        Call<Response> call = apiService.insertLikeAritcle(article_user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response deleteLikeArticle(Article_UserModel article_user) {
        Response response = null;
        Call<Response> call = apiService.deleteLikeAritcle(article_user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*Like Comment*/
    public List<Comment_UserModel> getListLikeComment(String commentID) {
        List<Comment_UserModel> list = new ArrayList<>();
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<List<Comment_UserModel>> call = apiService.getListLikeComment(commentID);
        try {
            list = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkLikeComment(String commentID, String userID) {
        Comment_UserModel comment_user = null;
        APIService apiService = ServiceGenerator.createService(APIService.class);
        final Call<Comment_UserModel> call = apiService.getLikeByComment_User(commentID, userID);
        try {
            comment_user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return comment_user != null;
    }

    public Response insertLikeComment(Comment_UserModel comment_user) {
        Response response = null;
        Call<Response> call = apiService.insertLikeComment(comment_user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response deleteLikeComment(Comment_UserModel comment_user) {
        Response response = null;
        Call<Response> call = apiService.deleteLikeComment(comment_user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*feedback comment*/
    public Response insertFeedbackComment(FeedbackComment feedbackComment) {
        Response response = null;
        Call<Response> call = apiService.insertFeedbackComment(feedbackComment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response updateFeedbackComment(FeedbackComment feedbackComment) {
        Response response = null;
        Call<Response> call = apiService.updateFeedbackComment(feedbackComment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response deleteFeedbackComment(FeedbackComment feedbackComment) {
        Response response = null;
        Call<Response> call = apiService.deleteFeedbackComment(feedbackComment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response reportFeedbackComment(ReportFeedbackComment reportFeedbackComment) {
        Response response = null;
        Call<Response> call = apiService.reportFeedbackComment(reportFeedbackComment);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*User*/
    public Response loginUser(User user) {
        Response response = null;
        Call<Response> call = apiService.loginUser(user);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response updatePoint(String userID, long point) {
        Log.d("loguser", "updatePoint: "+point);
        Response response = null;
        Call<Response> call = apiService.updatePoint(userID, point);
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
