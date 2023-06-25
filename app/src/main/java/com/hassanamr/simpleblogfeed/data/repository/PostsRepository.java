package com.hassanamr.simpleblogfeed.data.repository;

import com.hassanamr.simpleblogfeed.data.api.RetrofitClient;
import com.hassanamr.simpleblogfeed.data.model.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class PostsRepository {

    public Single<List<Post>> getPosts() {
        return RetrofitClient.getInstance().getPosts();
    }

    public Single<List<Post>> getUserPosts(int userId) {
        return RetrofitClient.getInstance().getUserPosts(userId);
    }

    public Single<Post> uploadPost(Post post) {
        return RetrofitClient.getInstance().uploadPost(post);
    }

    public Single<Post> updatePost(Post post) {
        return RetrofitClient.getInstance().updatePost(post);
    }

    public Single<Post> deletePost(int id) {
        return RetrofitClient.getInstance().deletePost(id);
    }
}