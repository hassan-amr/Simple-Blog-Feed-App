package com.hassanamr.simpleblogfeed.ui.activity.editpost;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.hassanamr.simpleblogfeed.ui.activity.userprofile.UserProfileViewModel;
import com.hassanamr.simpleblogfeed.utils.Constants;
import com.hassanamr.simpleblogfeed.utils.Util;

public class EditPostActivity extends AppCompatActivity {

    private ActivityPostDetailsBinding binding;

    private EditPostViewModel editPostViewModel;
    private Post post;

    private String postTitle, postContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        initUI();
        handleReceivedIntent();
        initListeners();
        initViewModel();
        initObservers();
    }

    private void initUI() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_details);

        binding.buttonAction.setText("Update Post");
        binding.buttonAction.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void handleReceivedIntent() {

        // Receive the Post object sent from the posts adapter
        post = (Post) getIntent().getSerializableExtra(Constants.POST);

        binding.editTextPostTitle.setText(post.getTitle());
        binding.editTextPostBody.setText(post.getBody());
    }

    private void initListeners() {

        binding.buttonAction.setOnClickListener(view -> {
            initPostData();
            if (isValidPost())
                updatePost();
        });
    }

    private void initViewModel() {
        editPostViewModel = ViewModelProviders.of(this).get(EditPostViewModel.class);
    }

    private void initObservers() {

        editPostViewModel.getPostLiveData().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {

                Toast.makeText(EditPostActivity.this, getResources().getString(R.string.post_updated_successfully), Toast.LENGTH_SHORT).show();

                PostsFeedViewModel postsFeedViewModel = ViewModelProviders.of(EditPostActivity.this).get(PostsFeedViewModel.class);
                postsFeedViewModel.updatePost(post);

                UserProfileViewModel userProfileViewModel = ViewModelProviders.of(EditPostActivity.this).get(UserProfileViewModel.class);
                userProfileViewModel.updatePost(post);

                finish();
            }
        });

        editPostViewModel.getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!Util.isConnectedToInternet(EditPostActivity.this))
                    Toast.makeText(EditPostActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
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

    private void updatePost() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonAction.setVisibility(View.GONE);

        post.setTitle(postTitle);
        post.setBody(postContent);

        editPostViewModel.updatePost(post);
    }

}
