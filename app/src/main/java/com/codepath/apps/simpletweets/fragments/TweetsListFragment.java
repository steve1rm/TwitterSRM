package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpletweets.EndlessScrollListener;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TweetsAdapter;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by steve on 11/3/16.
 */

public class TweetsListFragment extends Fragment {
    private List<Tweet> tweetList;
//    private ListView lvTweets;
    private TweetsAdapter mTweetsAdapter;
    private Unbinder mUnbinder;
    private String mMaxId;

    @BindView(R.id.lvTweets) ListView lvTweets;

    /* Inflation logic */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        lvTweets.setAdapter(mTweetsAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Timber.d("onLoadMore page %d totalItemsCount %d", page, totalItemsCount);
                //               populateHomeTimeHistory(25, mMaxId);
                return true;
            }
        });

  //      setupToolbar();
        return view;
    }

    /* Creation lifecycle event */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     /*   lvTweets.setOnScrollListener(new EndlessScrollListener() {
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
        /* Create the list adapter that will be our data source */
        tweetList = new ArrayList<>();
        mTweetsAdapter = new TweetsAdapter(getActivity(), tweetList);

/*


        *//* Singleton *//*
        mTwitterClient = TwitterApplication.getRestClient();

        populateHomeTimeline(0);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHomeTimeline(0);
            }
        });*/
    }

    public void addAll(List<Tweet> tweets) {
        mTweetsAdapter.addAll(tweets);
    }

    public Tweet getLastTweet() {
        if(!tweetList.isEmpty()) {
            return tweetList.get(tweetList.size() - 1);
        }

        return null;
    }

    public Tweet getFirstTweet() {
        if(!tweetList.isEmpty()) {
            return tweetList.get(0);
        }

        return null;
    }

    public String getMaxId() {
        Tweet tweet = getLastTweet();

        if(tweet != null) {
            mMaxId = tweet.getStrId();
            Timber.d("getMaxId before subtraction [%s]", mMaxId);
            /* Minus 1 from the max id */
            mMaxId = Utilities.subtractOneFromID(mMaxId);
            Timber.d("getMaxId after subtraction [%s]", mMaxId);

            return mMaxId;
        }
        else {
            return "";
        }
    }
}
