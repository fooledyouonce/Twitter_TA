package com.codepath.apps.restclienttemplate.activity;

import com.codepath.apps.restclienttemplate.models.Tweet;

public interface FragmentComposeListener {
    void onFinishComposingTweet(Tweet tweet);
}
