package com.hassanamr.simpleblogfeed.ui.activity.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hassanamr.simpleblogfeed.R;
import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.model.User;
import com.hassanamr.simpleblogfeed.databinding.ActivityUserProfileBinding;
import com.hassanamr.simpleblogfeed.ui.activity.editpost.EditPostActivity;
import com.hassanamr.simpleblogfeed.ui.activity.postsfeed.PostsFeedActivity;
import com.hassanamr.simpleblogfeed.ui.adapter.PostAdapter;
import com.hassanamr.simpleblogfeed.utils.Constants;
import com.hassanamr.simpleblogfeed.utils.Util;

import java.util.Collections;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    private UserProfileViewModel userProfileViewModel;
    private PostAdapter adapter;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initUI();
        handleReceivedIntent();
        initViewModel();
        initObserver();
    }

    private void initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        initRecyclerView();
    }

    private void initRecyclerView() {

        adapter = new PostAdapter(
                UserProfileActivity.this,
                1,
                false,
                new PostAdapter.OnChoosePostDialogOptionListener() {
                    @Override
                    public void OnChoosePostDialogOption(Post post, String action) {

                        if (action.equals(Constants.ACTION_EDIT)) {

                            Intent intent = new Intent(UserProfileActivity.this, EditPostActivity.class);
                            intent.putExtra(Constants.POST, post);
                            startActivity(intent);

                        } else if (action.equals(Constants.ACTION_DELETE)) {
                            userProfileViewModel.deletePost(post);
                        }

                    }
                }
        );

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void handleReceivedIntent() {

        // Receive the User object sent from the posts adapter
        user = (User) getIntent().getSerializableExtra(Constants.USER);

        binding.imageViewUserProfileImage.setImageResource(user.getProfileImageDrawable());
        binding.textViewUserName.setText(user.getName());
        binding.textViewUserEmail.setText(user.getEmail());
        binding.textViewUserWebsite.setText(user.getWebsite());
        binding.textViewUserPhone.setText(user.getPhone());
    }

    private void initViewModel() {
        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        userProfileViewModel.getUserPosts(user.getId());
    }

    private void initObserver() {
        userProfileViewModel.getPostsLiveData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postList) {
                adapter.setPostList(postList);
                adapter.setUserList(Collections.singletonList(user));
            }
        });

        userProfileViewModel.getPostStatusLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(UserProfileActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        userProfileViewModel.getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!Util.isConnectedToInternet(UserProfileActivity.this))
                    Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        });
    }

}
