package com.hassanamr.simpleblogfeed.ui.activity.createpost;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hassanamr.simpleblogfeed.R;
import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.databinding.ActivityPostDetailsBinding;
import com.hassanamr.simpleblogfeed.ui.activity.postsfeed.PostsFeedActivity;
import com.hassanamr.simpleblogfeed.ui.activity.postsfeed.PostsFeedViewModel;
import com.hassanamr.simpleblogfeed.utils.Util;

public class CreatePostActivity extends AppCompatActivity {

    private ActivityPostDetailsBinding binding;

    private CreatePostViewModel createPostViewModel;

    private String postTitle, postContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        initUI();
        initListeners();
        initViewModel();
        initObservers();
    }

    private void initUI() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_details);

        binding.buttonAction.setText(getResources().getString(R.string.create_post));
        binding.buttonAction.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void initListeners() {

        binding.buttonAction.setOnClickListener(view -> {
            initPostData();
            if (isValidPost())
                createPost();
        });
    }

    private void initViewModel() {
        createPostViewModel = ViewModelProviders.of(this).get(CreatePostViewModel.class);
    }

    private void initObservers() {

        createPostViewModel.getPostLiveData().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {

                Toast.makeText(CreatePostActivity.this, getResources().getString(R.string.post_uploaded_successfully), Toast.LENGTH_SHORT).show();

                PostsFeedViewModel postsFeedViewModel = ViewModelProviders.of(CreatePostActivity.this).get(PostsFeedViewModel.class);
                postsFeedViewModel.appendPost(post);

                finish();
            }
        });

        createPostViewModel.getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!Util.isConnectedToInternet(CreatePostActivity.this))
                    Toast.makeText(CreatePostActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initPostData() {
        postTitle = binding.editTextPostTitle.getText().toString().trim();
        postContent = binding.editTextPostBody.getText().toString().trim();
    }

    private boolean isValidPost() {

        if (postTitle.length() == 0) {
            binding.editTextPostTitle.setError(getResources().getString(R.string.empty_post_title));
            Toast.makeText(this, getResources().getString(R.string.empty_post_title), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (postContent.length() == 0) {
            binding.editTextPostBody.setError(getResources().getString(R.string.empty_post_content));
            Toast.makeText(this, getResources().getString(R.string.empty_post_content), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createPost() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonAction.setVisibility(View.GONE);

        int userId = 1; // Let 1 is the value got from the current user's data
        // Post id is added in the backend
        Post post = new Post(
                userId,
                postTitle,
                postContent
        );

        createPostViewModel.uploadPost(post);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}