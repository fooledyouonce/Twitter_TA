package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.helper.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="userId", childColumns="userId"))
public class Tweet {

    @PrimaryKey
    @ColumnInfo
    public long tweetId;
    @ColumnInfo
    public long userId;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String pic_url;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public boolean isRetweeted;
    @ColumnInfo
    public boolean isFavorited;
    @Ignore
    public User user;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //setting tweet body
        if (jsonObject.has("full_text")) { tweet.body = jsonObject.getString("full_text"); }
        else { tweet.body = jsonObject.getString("text"); }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.tweetId = jsonObject.getLong("id");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.userId = user.userId;
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        tweet.isFavorited =  jsonObject.getBoolean("favorited");

        //getting media
        if (!jsonObject.getJSONObject("entities").has("media")) {
            Log.d("Tweet", "No pictures!");
            tweet.pic_url = "none";
        }
        else {
            Log.d("Tweet", jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url")); //or _https
            tweet.pic_url = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url"); //or _https
        }
        /*
            * We need to fish for json data. How do we know we need to end up at "media_url"/"media_url_https"?
            * We do NOT want to keep running the app. Too slow and we may get blocked from making requests. We can use watches to resolve this issue.
        */
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
    public String getFormattedTimestamp() {
        return TimeFormatter.getTimeDifference(createdAt);
    }
}
