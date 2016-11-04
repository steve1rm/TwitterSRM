package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

/**
 * Created by steve on 11/3/16.
 */

public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient mTwitterClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwitterClient = TwitterApplication.getRestClient();

        populateMentionsTimeline(25, 1);
    }

    private void populateMentionsTimeline(int count, int since_id) {
        mTwitterClient.getMentionsTimeline(count, since_id, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Timber.e(throwable, "statusCode %d", statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
                Timber.d("onSuccess Home Timeline: %s", response.toString());
            }
        });
    }

}
