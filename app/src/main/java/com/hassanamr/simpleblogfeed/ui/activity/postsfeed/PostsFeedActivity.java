package com.hassanamr.simpleblogfeed.ui.activity.postsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hassanamr.simpleblogfeed.R;
import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.model.User;
import com.hassanamr.simpleblogfeed.databinding.ActivityPostsFeedBinding;
import com.hassanamr.simpleblogfeed.ui.activity.aboutapp.AboutAppActivity;
import com.hassanamr.simpleblogfeed.ui.activity.createpost.CreatePostActivity;
import com.hassanamr.simpleblogfeed.ui.activity.editpost.EditPostActivity;
import com.hassanamr.simpleblogfeed.ui.adapter.PostAdapter;
import com.hassanamr.simpleblogfeed.utils.Constants;
import com.hassanamr.simpleblogfeed.utils.Util;

import java.util.List;

public class PostsFeedActivity extends AppCompatActivity {

    private ActivityPostsFeedBinding binding;
    private PostsFeedViewModel postsFeedViewModel;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_feed);

        initUI();
        initListeners();
        initViewModel();
        initObservers();
    }

    private void initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_posts_feed);
        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    private void initListeners() {
        binding.floatingActionButton.setOnClickListener(view -> startActivity(new Intent(PostsFeedActivity.this, CreatePostActivity.class)));
    }

    private void initRecyclerView() {

        // Let the current user signed in is with userId = 1
        adapter = new PostAdapter(
                PostsFeedActivity.this,
                1,
                true,
                new PostAdapter.OnChoosePostDialogOptionListener() {
                    @Override
                    public void OnChoosePostDialogOption(Post post, String action) {

                        if (action.equals(Constants.ACTION_EDIT)) {

                            Intent intent = new Intent(PostsFeedActivity.this, EditPostActivity.class);
                            intent.putExtra(Constants.POST, post);
                            startActivity(intent);

                        } else if (action.equals(Constants.ACTION_DELETE)) {
                            postsFeedViewModel.deletePost(post);
                        }

                    }
                }
        );

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        postsFeedViewModel = ViewModelProviders.of(this).get(PostsFeedViewModel.class);
        postsFeedViewModel.getPosts();
    }

    private void initObservers() {

        postsFeedViewModel.getPostsLiveData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postList) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
                adapter.setPostList(postList);
            }
        });

        postsFeedViewModel.getUsersLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                adapter.setUserList(userList);
            }
        });

        postsFeedViewModel.getPostStatusLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(PostsFeedActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        postsFeedViewModel.getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.recyclerView.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (!Util.isConnectedToInternet(PostsFeedActivity.this))
                    Toast.makeText(PostsFeedActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuItemAboutApp) {
            startActivity(new Intent(PostsFeedActivity.this, AboutAppActivity.class));
            return true;
        }

        return super.onContextItemSelected(item);
    }

}