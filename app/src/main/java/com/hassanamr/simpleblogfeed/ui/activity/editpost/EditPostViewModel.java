package com.hassanamr.simpleblogfeed.ui.activity.editpost;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.repository.PostsRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditPostViewModel extends ViewModel {

    private final PostsRepository postsRepository = new PostsRepository();
    private final MutableLiveData<Post> postLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void updatePost(Post post) {

        Single<Post> postObservable = postsRepository
                .updatePost(post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable.add(
                // Handle the coming post response
                postObservable.subscribe(postResponse -> {
                    // Add the post to live data
                    postLiveData.setValue(postResponse);
                }, error -> errorLiveData.setValue(error.getMessage()))
        );
    }

    public MutableLiveData<Post> getPostLiveData() {
        return postLiveData;
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
