package com.hassanamr.simpleblogfeed.ui.activity.userprofile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.repository.PostsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserProfileViewModel extends ViewModel {

    private final PostsRepository postsRepository = new PostsRepository();

    private static final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> postStatusLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void getUserPosts(int userId) {

        Single<List<Post>> postsObservable = postsRepository
                .getUserPosts(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable.add(
                // Handle the coming posts list
                postsObservable.subscribe(postList -> {

                            // Add the posts list to posts live data
                            postsLiveData.setValue(postList);
                        }, error -> errorLiveData.setValue(error.getMessage())
                )
        );

    }

    public void updatePost(Post post) {

        List<Post> postList = postsLiveData.getValue();
        if (postList != null)
            for (int i = 0; i < postList.size(); i++)
                if (postList.get(i).getId() == post.getId()) {
                    postList.set(i, post);
                    postsLiveData.setValue(postList);
                    break;
                }

    }

    public void deletePost(Post post) {

        Single<Post> postObservable = postsRepository
                .deletePost(post.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable.add(
                postObservable.subscribe(deletedPost -> {
                            List<Post> postList = postsLiveData.getValue();
                            if (postList != null)
                                for (int i = 0; i < postList.size(); i++)

                                    // It have to be deletedPost.getId() instead of post.getId()
                                    // But this is test server & it acts that it actually deleted the post
                                    // but it will not delete it and will return another post in the response
                                    if (postList.get(i).getId() == post.getId()) {
                                        postList.remove(i);
                                        postsLiveData.setValue(postList);
                                        postStatusLiveData.setValue("Post Deleted Successfully");
                                        break;
                                    }

                        }, error -> errorLiveData.setValue(error.getMessage())
                )
        );
    }

    public MutableLiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public MutableLiveData<String> getPostStatusLiveData() {
        return postStatusLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Remove all the subscribes between observers and observables
        compositeDisposable.clear();
    }
}
