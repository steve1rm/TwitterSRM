package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.simpletweets.EndlessScrollListener;
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

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient mTwitterClient;
    private String mMaxId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwitterClient = TwitterApplication.getRestClient();

        populateHomeTimeline(25, 1);

 /*       lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Timber.d("onLoadMore page %d totalItemsCount %d", page, totalItemsCount);
                populateHomeTimeHistory(25, mMaxId);
                return true;
            }
        });
 */
    }

    private void populateHomeTimeline(int count, int since_id) {
        mTwitterClient.getHomeTimeline(count, since_id, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Timber.e(throwable, "statusCode %d", statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
                mMaxId = getMaxId();
                Timber.d("onSuccess Home Timeline: [%s] %s", mMaxId, response.toString());
            }
        });
    }

    private void populateHomeTimeHistory(int count, final String maxId) {
        mTwitterClient.getHomeTimelineExt(count, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
                mMaxId = getMaxId();
                Timber.d("onSuccess Home Timeline: [%s] %s", mMaxId, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Timber.e(throwable, "onFailure");
            }
        });
    }


}
