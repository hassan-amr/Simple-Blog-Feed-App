package com.hassanamr.simpleblogfeed.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hassanamr.simpleblogfeed.R;
import com.hassanamr.simpleblogfeed.data.model.Post;
import com.hassanamr.simpleblogfeed.data.model.User;
import com.hassanamr.simpleblogfeed.ui.activity.userprofile.UserProfileActivity;
import com.hassanamr.simpleblogfeed.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnChoosePostDialogOptionListener {
        void OnChoosePostDialogOption(Post post, String action);
    }

    private List<Post> postList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    private final Context context;
    private final int currentUserId;
    private final boolean clickablePost;
    private final OnChoosePostDialogOptionListener postDialogOptionListener;


    public PostAdapter(Context context, int currentUserId, boolean clickablePost, OnChoosePostDialogOptionListener postDialogOptionListener) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.postDialogOptionListener = postDialogOptionListener;
        this.clickablePost = clickablePost;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        // Find the user by id and set it in the post card
        User postOwner = null;
        for (User user : userList)
            if (user.getId() == postList.get(holder.getAdapterPosition()).getUserId())
                postOwner = user;

        if (postOwner != null) {
            holder.imageViewUserProfileImage.setImageResource(postOwner.getProfileImageDrawable());
            holder.textViewUserName.setText(postOwner.getName());
        }

        holder.textViewPostTitle.setText(postList.get(holder.getAdapterPosition()).getTitle());
        holder.textViewPostBody.setText(postList.get(holder.getAdapterPosition()).getBody());

        holder.itemView.setOnLongClickListener(view -> {

            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(50);

            if (currentUserId == postList.get(holder.getAdapterPosition()).getUserId())
                showPostOptionsDialog(postList.get(holder.getAdapterPosition()));

            return true;
        });
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    public void showPostOptionsDialog(Post post) {

        String[] choices = {Constants.ACTION_EDIT, Constants.ACTION_DELETE};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true)
                .setTitle(post.getTitle())
                .setItems(choices,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String action = "";
                                if (which == 0) action = Constants.ACTION_EDIT;
                                else if (which == 1) action = Constants.ACTION_DELETE;

                                postDialogOptionListener.OnChoosePostDialogOption(post, action);
                            }
                        }
                )
                .show();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewUserProfileImage;
        private final TextView textViewUserName, textViewPostTitle, textViewPostBody;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserProfileImage = itemView.findViewById(R.id.imageViewUserProfileImage);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewPostTitle = itemView.findViewById(R.id.textViewPostTitle);
            textViewPostBody = itemView.findViewById(R.id.textViewPostBody);

            if (clickablePost) initListeners();
        }

        private void initListeners() {
            itemView.setOnClickListener(view -> openUserProfile());
        }

        private void openUserProfile() {

            int position = getAdapterPosition();
            Post post = postList.get(position);

            User postOwner = null;
            for (User user : userList)
                if (user.getId() == post.getUserId())
                    postOwner = user;

            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra(Constants.USER, postOwner);
            context.startActivity(intent);
        }


    }
}
