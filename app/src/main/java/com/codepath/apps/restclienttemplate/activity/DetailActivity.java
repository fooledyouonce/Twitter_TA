package com.codepath.apps.restclienttemplate.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
//TODO: Fix data being passed
    ImageView ivProfileImage2;
    TextView tvScreenName2;
    TextView tvBody2;
    TextView tvTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //ivProfileImage2 = findViewById(R.id.ivProfileImage2);
        tvScreenName2 = findViewById(R.id.tvScreenName2);
        tvBody2 = findViewById(R.id.tvBody2);
        tvTime2 = findViewById(R.id.tvTime2);

        String screen_name = getIntent().getStringExtra("screen_name");
        String body = getIntent().getStringExtra("body");
        String time = getIntent().getStringExtra("time");

        tvScreenName2.setText(screen_name);
        tvBody2.setText(body);
        tvTime2.setText(time);
    }
}