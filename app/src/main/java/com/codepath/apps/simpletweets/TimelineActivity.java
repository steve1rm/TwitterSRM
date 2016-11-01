package com.codepath.apps.simpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.utils.Utilities;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

import static android.R.id.message;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient mTwitterClient;
    private List<Tweet> tweetList;
    private ListView lvTweets;
    private TweetsAdapter mTweetsAdapter;
    private Unbinder mUnbinder;

    @BindView(R.id.tbTweeter) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mUnbinder = ButterKnife.bind(TimelineActivity.this);

        setupToolbar();

        lvTweets = (ListView)findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Timber.d("onLoadMore page %d totalItemsCount %d", page, totalItemsCount);
                loadNextDataFromApi(page);
                return true;
            }
        });

        String createdAt = "Sun Oct 23 05:50:13 +0000 2016";
        String date = Utilities.getTimeDifference(createdAt);

        /* Create the list adapter that will be our data source */
        tweetList = new ArrayList<>();
        mTweetsAdapter = new TweetsAdapter(TimelineActivity.this, tweetList);
        lvTweets.setAdapter(mTweetsAdapter);

        /* Singleton */
        mTwitterClient = TwitterApplication.getRestClient();

        populateHomeTimeline(0);
   //     processIntent(getIntent());

        Timber.d("date a go: %s", date);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }


    private void loadNextDataFromApi(int pageCount) {
        Timber.d("loadNextDataFromApi %d", pageCount);
        mTwitterClient.getHomeTimeline(pageCount, new JsonHttpResponseHandler() {
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

    private void populateHomeTimeline(int pageCount) {
        mTwitterClient.getHomeTimeline(pageCount, new JsonHttpResponseHandler() {
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

    public void processIntent(Intent data) {
        /* Sanity check for corrupt data */
        if(data != null) {
            if(data.hasExtra(SendActivity.STATUSMSG_KEY)) {
                String message = data.getStringExtra(SendActivity.STATUSMSG_KEY);
                createTweet(message);
            }
        }
    }

    private void createTweet(String message) {
        Timber.d("onResume: load more tweets %d", tweetList.size());
        Tweet tweet = new Tweet();

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM HH:mm:ss Z yyyy", Locale.getDefault());

        tweet.setCreatedAt(dateFormat.format(Calendar.getInstance().getTime()));
        tweet.setBody(message);
        tweet.setDate(dateFormat.format(Calendar.getInstance().getTime()));

        User user = new User();
        user.setProfileImageUrl("http://a0.twimg.com/profile_images/2284174872/7df3h38zabcvjylnyfe3_normal.png");
        user.setScreenName("Steve mason");
        user.setName("Steve Mason");
        tweet.setUser(user);

        mTweetsAdapter.add(tweet);
        mTweetsAdapter.notifyDataSetChanged();
        lvTweets.invalidateViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuCompose:
                /* open sending tweet activity */
                Intent intent = new Intent(TimelineActivity.this, SendActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
