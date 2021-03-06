package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.activity.DetailActivity;
import com.codepath.apps.restclienttemplate.activity.FollowActivity;
import com.codepath.apps.restclienttemplate.activity.TimelineActivity;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private static final String TAG = "TweetsAdapter";
    Context context;
    TimelineActivity activity;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, TimelineActivity activity, List<Tweet> tweets) {
        this.context = context; //DEBUG
        this.activity = activity;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTweetBinding binding = ItemTweetBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTweet;
        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvTime;
        ImageButton ibtnReply;
        ImageButton ibtnRetweet;
        ImageButton ibtnLike;
        ItemTweetBinding binding;

        public ViewHolder(@NonNull ItemTweetBinding itemBind) {
            super(itemBind.getRoot());
            binding = itemBind;
            ivTweet = binding.ivTweet;
            ivProfileImage = binding.ivProfileImage;
            tvScreenName = binding.tvScreenName;
            tvBody = binding.tvBody;
            tvTime = binding.tvTime;
            ibtnReply = binding.ibtnReply;
            ibtnRetweet = binding.ibtnRetweet;
            ibtnLike = binding.ibtnLike;
        }

        public void bind(Tweet tweet) {
            tvScreenName.setText(tweet.user.screenName);
            tvBody.setText(tweet.body);
            Glide.with(context).load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(50))
                    .into(ivProfileImage);
            tvTime.setText(tweet.getFormattedTimestamp());

            if (!Objects.equals(tweet.pic_url, "none")) {
                ivTweet.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.pic_url).into(ivTweet);
            }
            else { ivTweet.setVisibility(View.GONE); }
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, FollowActivity.class);
                    i.putExtra("user", Parcels.wrap(tweet.user));
                    context.startActivity(i);
                }
            });

            tvBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });

            //This is the source, goes to FragmentCompose
            ibtnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showReplyDialog(tweet);
                } //DEBUG
            });

            ibtnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tweet.isRetweeted) {
                        setUnretweetColor();
                        TwitterApp.getRestClient(context).unretweet(tweet.tweetId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) { tweet.isRetweeted = false; }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {}
                        });
                    } else {
                        setRetweetColor();
                        TwitterApp.getRestClient(context).retweet(tweet.tweetId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) { tweet.isRetweeted = true; }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {}
                        });
                    }
                }
            });

            ibtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tweet.isFavorited) {
                        setUnfavoritedColor();
                        TwitterApp.getRestClient(context).unfavorite(tweet.tweetId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) { tweet.isFavorited = false; }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {}
                        });

                    } else {
                        setFavoritedColor();
                        TwitterApp.getRestClient(context).favorite(tweet.tweetId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) { tweet.isFavorited = true; }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            }
                        });
                    }
                }
            });
        }

        public void setRetweetColor() { binding.ibtnRetweet.setColorFilter(context.getResources().getColor(R.color.twitterRetweet)); }

        public void setUnretweetColor() { binding.ibtnRetweet.setColorFilter(context.getResources().getColor(R.color.twitterThemeGrey)); }

        public void setFavoritedColor() { binding.ibtnLike.setColorFilter(context.getResources().getColor(R.color.twitterLike)); }

        public void setUnfavoritedColor() { binding.ibtnLike.setColorFilter(context.getResources().getColor(R.color.twitterThemeGrey)); }
    }

    public void showReplyDialog(Tweet replyToTweet) {
        Log.d(TAG, "Accessed fragment"); //DEBUG
        //Tell activity to open the Compose Dialog fragment
        activity.goComposeFragment(replyToTweet); //DEBUG
    }
}
