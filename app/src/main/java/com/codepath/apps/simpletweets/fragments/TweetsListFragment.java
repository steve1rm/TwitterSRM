package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TweetsAdapter;
import com.codepath.apps.simpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by steve on 11/3/16.
 */

public class TweetsListFragment extends Fragment {
    private List<Tweet> tweetList;
    private ListView lvTweets;
    private TweetsAdapter mTweetsAdapter;
    private Unbinder mUnbinder;

    /* Inflation logic */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        lvTweets = (ListView)view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(mTweetsAdapter);

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
}
