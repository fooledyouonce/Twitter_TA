package com.codepath.apps.restclienttemplate.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    Tweet tweet;
    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        binding.tvScreenName2.setText(tweet.user.screenName);
        binding.tvBody2.setText(tweet.body);
        binding.tvTime2.setText(tweet.getFormattedTimestamp());
        Glide.with(this).load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(90))
                .into(binding.ivProfileImage2);
    }
}