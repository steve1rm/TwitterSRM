package com.codepath.apps.simpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.widget.ListView;

import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

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
        String date = getTimeDifference(createdAt);

        /* Create the list adapter that will be our data source */
        tweetList = new ArrayList<>();
        mTweetsAdapter = new TweetsAdapter(TimelineActivity.this, tweetList);
        lvTweets.setAdapter(mTweetsAdapter);

        /* Singleton */
        mTwitterClient = TwitterApplication.getRestClient();
  //      sendTweet();
        //populateTimeline();


        populateHomeTimeline();



        Timber.d("date a go: %s", date);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    public static String getTimeDifference(String rawJsonDate) {
        String time = "";
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        format.setLenient(true);
        try {
            long diff = (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000;
            if (diff < 5)
                time = "Just now";
            else if (diff < 60)
                time = String.format(Locale.ENGLISH, "%ds",diff);
            else if (diff < 60 * 60)
                time = String.format(Locale.ENGLISH, "%dm", diff / 60);
            else if (diff < 60 * 60 * 24)
                time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
            else if (diff < 60 * 60 * 24 * 30)
                time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
            else {
                Calendar now = Calendar.getInstance();
                Calendar then = Calendar.getInstance();
                then.setTime(format.parse(rawJsonDate));
                if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                    time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                            + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
                } else {
                    time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                            + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                            + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
                }
            }
        }  catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";

        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    private void loadNextDataFromApi(int page) {
        Timber.d("loadNextDataFromApi %d", page);
        mTwitterClient.getHomeTimelineExt(new JsonHttpResponseHandler() {
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


    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
