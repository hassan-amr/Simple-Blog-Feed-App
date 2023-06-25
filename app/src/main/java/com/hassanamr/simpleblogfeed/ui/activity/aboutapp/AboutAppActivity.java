package com.hassanamr.simpleblogfeed.ui.activity.aboutapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hassanamr.simpleblogfeed.R;
import com.hassanamr.simpleblogfeed.databinding.ActivityAboutAppBinding;

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAboutAppBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        initUI();
        initListeners();
    }

    private void initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_app);
        binding.linearLayoutDetails.setVisibility(View.GONE);
    }

    private void initListeners() {
        binding.textViewShowDetails.setOnClickListener(this);
        binding.linearLayoutDetails.setOnClickListener(this);
        binding.imageViewLinkedIn.setOnClickListener(this);
        binding.imageViewGitHub.setOnClickListener(this);
        binding.imageViewHackerRank.setOnClickListener(this);
        binding.imageViewGmail.setOnClickListener(this);
        binding.textViewGmail.setOnClickListener(this);
        binding.textViewLinkedIn.setOnClickListener(this);
        binding.textViewGitHub.setOnClickListener(this);
        binding.textViewHackerRank.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.textViewShowDetails:
                if (binding.linearLayoutDetails.getVisibility() == View.GONE)
                    binding.linearLayoutDetails.setVisibility(View.VISIBLE);
                else binding.linearLayoutDetails.setVisibility(View.GONE);
                break;

            case R.id.imageViewLinkedIn:
            case R.id.textViewLinkedIn:
                openPage("https://linkedin.com/in/hassan-amr-684a851ba");
                break;

            case R.id.imageViewGitHub:
            case R.id.textViewGitHub:
                openPage("https://github.com/hassan-amr");
                break;

            case R.id.imageViewHackerRank:
            case R.id.textViewHackerRank:
                openPage("https://www.hackerrank.com/HassanAmrSoliman");
                break;

            case R.id.imageViewGmail:
            case R.id.textViewGmail:

                Intent intent = new Intent();
                intent.setType("message/rfc822");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Simple Blog Feed - Contact Me");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hassan.amr.soliman@gmail.com"});

                if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
                break;

        }

    }

    private void openPage(String pageLink) {
        Uri uri = Uri.parse(pageLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
