package com.hassanamr.simpleblogfeed.data.api;

import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServices {

    //-------------------------- Posts -----------------------------------------------------------//

    // Resulted URL: baseurl/posts
    @GET("posts")
    public Single<List<Post>> getPosts();

    // Resulted URL: baseurl/posts?userId=<userId>
    @GET("posts")
    public Single<List<Post>> getUserPosts(@Query("userId") int userId);

    // Resulted URL: baseurl/posts/
    @POST("posts")
    public Single<Post> uploadPost(@Body Post post);

    // Resulted URL: baseurl/posts/<id>
    @PUT("posts/{id}")
    public Single<Post> updatePost(@Path("id") int id, @Body Post post);

    // Resulted URL: baseurl/posts/<id>
    @DELETE("posts/{id}")
    public Single<Post> deletePost(@Path("id") int id);

    //-------------------------- Users -----------------------------------------------------------//

    // Resulted URL: baseurl/users
    @GET("users")
    public Single<List<User>> getUsers();
}