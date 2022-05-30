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

    /*ImageView ivProfileImage2;
    TextView tvScreenName2;
    TextView tvBody2;
    TextView tvTime2;*/
    Tweet tweet;
    TwitterClient client;
    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail); //---> old way of setting up layout
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        old way of setting up layout
        ivProfileImage2 = findViewById(R.id.ivProfileImage2);
        tvScreenName2 = findViewById(R.id.tvScreenName2);
        tvBody2 = findViewById(R.id.tvBody2);
        tvTime2 = findViewById(R.id.tvTime2);
        */

        /*
        "rudimentary" method of getting tweet data
        String screen_name = getIntent().getStringExtra("screen_name");
        String body = getIntent().getStringExtra("body");
        String time = getIntent().getStringExtra("time");
        */

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        binding.tvScreenName2.setText(tweet.user.screenName);
        binding.tvBody2.setText(tweet.body);
        binding.tvTime2.setText(tweet.getFormattedTimestamp());
        Glide.with(this).load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(50))
                .into(binding.ivProfileImage2);
    }
}