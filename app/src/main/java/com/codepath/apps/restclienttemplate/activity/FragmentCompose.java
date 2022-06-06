package com.codepath.apps.restclienttemplate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Objects;

import okhttp3.Headers;

public class FragmentCompose extends DialogFragment {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    Tweet replyToTweet;

    public FragmentCompose() {}

    public static FragmentCompose newInstance(Tweet replyToTweet) {
        Log.d(TAG, "Accessed fragment");
        FragmentCompose frag = new FragmentCompose(); //DEBUG
        Bundle args = new Bundle();
        args.putParcelable("tweet", Parcels.wrap(replyToTweet));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_activity, container); //DEBUG
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCompose = (EditText) view.findViewById(R.id.etCompose); //DEBUG
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(getContext());

        replyToTweet = Parcels.unwrap(getArguments().getParcelable("tweet"));

        if(replyToTweet != null) {
            etCompose.setText("@" + replyToTweet.user.screenName);
        }

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(getActivity(), "Your tweet cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(getActivity(), "Your tweet must be less than 280 characters!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (replyToTweet != null) { //DEBUG
                    if (!tweetContent.substring(0, replyToTweet.user.screenName.length() + 1).equals("@" + replyToTweet.user.screenName)) { //DEBUG
                        Toast.makeText(getActivity(), "Include user!", Toast.LENGTH_LONG).show(); //DEBUG
                        etCompose.requestFocus();
                        return;
                    }
                    else {
                        client.replyToTweet(tweetContent, replyToTweet.tweetId, new JsonHttpResponseHandler() { //DEBUG
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                try {
                                    Tweet replyTweet = Tweet.fromJson(json.jsonObject);
                                    Log.i(TAG, "Published tweet: " + replyTweet);
                                    //***Why a listener? added layer of security. This way fragment has access to exactly one method and cannot mess up TimelineActivity
                                    FragmentComposeListener activity = ((FragmentComposeListener) getActivity());
                                    activity.onFinishComposingTweet(replyTweet);
                                    dismiss();
                                } catch (JSONException e) {
                                    Log.e(TAG, "Json exception");
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {}
                        });
                    }
                    return;

                }

                //Make api call to twitter to publish tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet newTweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet: " + newTweet);
                            //***Why a listener? added layer of security. This way fragment has access to exactly one method and cannot mess up TimelineActivity
                            FragmentComposeListener activity = ((FragmentComposeListener) getActivity());
                            activity.onFinishComposingTweet(newTweet);
                            dismiss();
                        } catch (JSONException e) {
                            Log.e(TAG, "Json exception");
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) { Log.e(TAG, "onFailure", throwable); }
                });
            }
        });
        etCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onStart() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Objects.requireNonNull(getDialog()).getWindow().setLayout((6 * width)/7, (3 * height)/5);
        super.onStart();
    }
}