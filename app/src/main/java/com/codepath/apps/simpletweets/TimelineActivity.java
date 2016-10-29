package com.codepath.apps.simpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient mTwitterClient;
    private List<Tweet> tweetList;
    private ListView lvTweets;
    private TweetsAdapter mTweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView)findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Timber.d("onLoadMore page %d totalItemsCount %d", page, totalItemsCount);
                loadNextDataFromApi(page);
                return true;
            }
        });
        
        /* Create the list adapter that will be our data source */
        tweetList = new ArrayList<>();
        mTweetsAdapter = new TweetsAdapter(TimelineActivity.this, tweetList);
        lvTweets.setAdapter(mTweetsAdapter);

        /* Singleton */
        mTwitterClient = TwitterApplication.getRestClient();
  //      sendTweet();
        //populateTimeline();
        populateHomeTimeline();
    }

    private void loadNextDataFromApi(int page) {
        Timber.d("loadNextDataFromApi %d", page);
        mTwitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                /* deserialize json */
                mTweetsAdapter.addAll(Tweet.fromJSONArray(response));
                Timber.d("loadNextDataFromAPI onSuccess %s %d", response.toString(), tweetList.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("loadNextDataFromAPI onFailure %s", errorResponse.toString());
            }
        });
    }

    private void populateHomeTimeline() {
        mTwitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Timber.e(throwable, "statusCode %d", statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mTweetsAdapter.addAll(Tweet.fromJSONArray(response));
                Timber.d("onSuccess Home Timeline: %s", response.toString());
            }
        });
    }

    private void sendTweet() {
        mTwitterClient.sendTwitterMessage(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("statusCode %d %s", statusCode, errorResponse.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Timber.d("onSuccess Tweeting statuscode: %d responseBody %s", statusCode, response.toString());
            }
        });

    }



    /* Send an API request to get the timeline json
    *  Fill the listview by creating the tweet objects from the json */
    private void populateTimeline() {
        mTwitterClient.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                /* deserialize json */
                mTweetsAdapter.addAll(Tweet.fromJSONArray(response));
                Timber.d("onSuccess %s %d", response.toString(), tweetList.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("onFailure %s", errorResponse.toString());
            }
        });
    }
}
