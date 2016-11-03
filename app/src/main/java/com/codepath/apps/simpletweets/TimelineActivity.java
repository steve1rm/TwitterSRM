package com.codepath.apps.simpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TimelineActivity extends AppCompatActivity {

/*
    private List<Tweet> tweetList;
    private ListView lvTweets;
    private TweetsAdapter mTweetsAdapter;

    private TweetsListFragment fragmentTweetsList;


    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
*/

    private Unbinder mUnbinder;
    @BindView(R.id.viewpager) ViewPager mViewpager;
    @BindView(R.id.tabs) PagerSlidingTabStrip mTabs;
    @BindView(R.id.tbTweeter) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mUnbinder = ButterKnife.bind(TimelineActivity.this);

        setupToolbar();

        /* Set the viewpager adapter for the pager */
        TweetPagerAdapter tweetPagerAdapter = new TweetPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(tweetPagerAdapter);

        /* Attach the pager tabs to the viewpager */
        mTabs.setViewPager(mViewpager);

/*

        mUnbinder = ButterKnife.bind(TimelineActivity.this);

        if(savedInstanceState == null) {
            */
/* Pull the fragment out of the layout *//*

            fragmentTweetsList =
                    (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }
*/

/*
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

        */
/* Create the list adapter that will be our data source *//*

        tweetList = new ArrayList<>();
        mTweetsAdapter = new TweetsAdapter(TimelineActivity.this, tweetList);
        lvTweets.setAdapter(mTweetsAdapter);

        */
/* Singleton *//*

        //mTwitterClient = TwitterApplication.getRestClient();

       //

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHomeTimeline(0);
            }
        });
*/
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }


  /*  private void refreshHomeTimeline(int page) {
        mTwitterClient.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Timber.e(throwable, "statusCode %d", statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mTweetsAdapter.clear();
                mTweetsAdapter.addAll(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
                Timber.d("onSuccess Home Timeline: %s", response.toString());
            }
        });
    }
*/
 /*   private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }
*/

  /*  private void loadNextDataFromApi(int pageCount) {
        Timber.d("loadNextDataFromApi %d", pageCount);
        mTwitterClient.getHomeTimeline(pageCount, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                *//* deserialize json *//*
                mTweetsAdapter.addAll(Tweet.fromJSONArray(response));
                Timber.d("loadNextDataFromAPI onSuccess %s %d", response.toString(), tweetList.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("loadNextDataFromAPI onFailure %s", errorResponse.toString());
            }
        });
    }

  */  /* Send an API request to get the timeline json
    *  Fill the listview by creating the tweet objects from the json */
  /*  private void populateTimeline() {
        mTwitterClient.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                *//* deserialize json *//*
                mTweetsAdapter.addAll(Tweet.fromJSONArray(response));
                Timber.d("onSuccess %s %d", response.toString(), tweetList.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("onFailure %s", errorResponse.toString());
            }
        });
    }
*/
 /*   public void processIntent(Intent data) {
        *//* Sanity check for corrupt data *//*
        if(data != null) {
            if(data.hasExtra(SendActivity.STATUSMSG_KEY)) {
                String message = data.getStringExtra(SendActivity.STATUSMSG_KEY);
                createTweet(message);
            }
        }
    }
*/
 /*   private void createTweet(String message) {
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
    }*/

    public void onProfileView(MenuItem menuItem) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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

    /** Return the order of the fragments in the view pager */
    public class TweetPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        /** Adapter gets the manager insert or remove fragments from the activity */
        public TweetPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /** The order and creation of fragments within the pager */
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            }
            else if(position == 1) {
                return new MentionsTimelineFragment();
            }
            else {
                return null;
            }
        }

        /** Return the tab title */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        /** How many tabs there are to swipe between */
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
