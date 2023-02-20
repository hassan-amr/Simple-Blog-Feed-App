package com.hassanamr.simpleblogfeed.data.api;

import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.model.User;
import com.hassanamr.simpleblogfeed.utils.Constants;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient retrofitClientInstance;

    public static RetrofitClient getInstance() {
        if (retrofitClientInstance == null) retrofitClientInstance = new RetrofitClient();
        return retrofitClientInstance;
    }

    private Retrofit getRetrofitBuilder() {
        return new Retrofit
                .Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    private APIServices getAPIServices() {
        Retrofit retrofit = getRetrofitBuilder();
        return retrofit.create(APIServices.class);
    }

    //-------------------------- Posts -----------------------------------------------------------//

    public Single<List<Post>> getPosts() {
        return getAPIServices().getPosts();
    }

    public Single<List<Post>> getUserPosts(int userId) {
        return getAPIServices().getUserPosts(userId);
    }

    public Single<Post> uploadPost(Post post) {
        return getAPIServices().uploadPost(post);
    }

    public Single<Post> updatePost(Post post) {
        return getAPIServices().updatePost(post.getId(), post);
    }

    public Single<Post> deletePost(int id) {
        return getAPIServices().deletePost(id);
    }

    //-------------------------- Users -----------------------------------------------------------//

    public Single<List<User>> getUsers() {
        return getAPIServices().getUsers();
    }

}
