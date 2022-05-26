package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.activity.DetailActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    //pass in context and list of tweets
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //for each row, inflate layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //bind values based on position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data at position
        Tweet tweet = tweets.get(position);
        //bind tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //for swiperefresh
    //clear elements of recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    //add list of items
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    //define viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlTweet;
        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvTime;
        Tweet tweetForItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlTweet = itemView.findViewById(R.id.rlTweet);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
        public void bind(Tweet tweet) {
            tvScreenName.setText(tweet.user.screenName);
            tvBody.setText(tweet.body);
            int radius = 30;
            int margin = 10;
            //TODO: Rounded pfp
            Glide.with(context).load(tweet.user.profileImageUrl)
                    //.transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivProfileImage);
            tvTime.setText(tweet.getFormattedTimestamp());
            tweetForItem = tweet;
            rlTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String screen_name = tweetForItem.user.screenName;
                    String body = tweetForItem.body;
                    String time = tweetForItem.createdAt;
                    //TODO: Pass pfp in

                    //Toast.makeText(context, "clicked!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("screen_name", screen_name);
                    i.putExtra("body", body);
                    i.putExtra("time", time);
                    context.startActivity(i);
                }
            });
        }

    }
}
