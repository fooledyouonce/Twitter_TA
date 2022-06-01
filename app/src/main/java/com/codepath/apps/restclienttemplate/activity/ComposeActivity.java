package com.codepath.apps.restclienttemplate.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    ActivityComposeBinding binding;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_compose);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(this);

        if(getIntent().hasExtra("replyToTweet")) {
            tweet = Parcels.unwrap(getIntent().getParcelableExtra("replyToTweet"));
            etCompose.setText("@" + tweet.user.screenName);
        }

        if(getIntent().hasExtra("body")) {
            tweet = Parcels.unwrap(getIntent().getParcelableExtra("body"));
            etCompose.setText(tweet.body);
        }

        //set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Your tweet cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Your tweet must be less than 280 characters!", Toast.LENGTH_LONG).show();
                    return;
                }
                //TODO: reply
                /*if (replyTo != null) {
                    if (tweetContent !=  "@" + replyTo.user.screenName) {
                        Toast.makeText(ComposeActivity.this, "Include user!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }*/

                //make api call to twitter to publish tweet
                //Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show(); //debugging
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            //closes activity, passes data to parent
                            finish();
                        } catch (JSONException e) {
                            Log.e(TAG, "Json exception");
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure", throwable);
                    }
                });
            }
        });
    }
}