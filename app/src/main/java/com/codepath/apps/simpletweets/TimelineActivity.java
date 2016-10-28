package com.codepath.apps.simpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

import static java.util.Collections.addAll;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient mTwitterClient;
    private ArrayList<Tweet> tweetList;
    private ListView lvTweets;
    private TweetsAdapter mTweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView)findViewById(R.id.lvTweets);
        /* Create the list adapter that will be our data source */
        tweetList = new ArrayList<>();
        mTweetsAdapter = new TweetsAdapter(TimelineActivity.this, tweetList);


        /* Singleton */
        mTwitterClient = TwitterApplication.getRestClient();
        populateTimeline();
        lvTweets.setAdapter(mTweetsAdapter);
    }

    /* Send an API request to get the timeline json
    *  Fill the listview by creating the tweet objects from the json */
    private void populateTimeline() {
        mTwitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
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
