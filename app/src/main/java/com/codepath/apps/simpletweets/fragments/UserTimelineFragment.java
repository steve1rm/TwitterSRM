package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.adapters.HometimelineAdapter;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.utils.Utilities;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

/**
 * Created by steve on 11/3/16.
 */

public class UserTimelineFragment extends Fragment {

    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private String mMaxId;
    private HometimelineAdapter mHometimeAdapter;
    private String mScreenName;

    @BindView(R.id.rvHometimeline) RecyclerView mRvHometimeline;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout mSwipeRefreshLayout;
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateUserTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_timeline, container, false);

        mUnbinder = ButterKnife.bind(UserTimelineFragment.this, view);
        setupRecylcerView();
        setupSwipeRefresh();

        mScreenName = getArguments().getString("screen_name");
        populateUserTimeline();

        return view;

    }

    private void setupRecylcerView() {
        Timber.d("setupRecyclerView");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvHometimeline.setLayoutManager(linearLayoutManager);
        mHometimeAdapter = new HometimelineAdapter(getActivity(), new ArrayList<Tweet>());
        mRvHometimeline.setAdapter(mHometimeAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRvHometimeline.addItemDecoration(dividerItemDecoration);

        setupEndlessScrolling(linearLayoutManager);
    }

    private void setupEndlessScrolling(LinearLayoutManager linearLayoutManager) {
        Timber.d("setupEndlessScrolling");

        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(page < 15) {
                    Timber.d("onLoadMore page %d [%s]", page, mMaxId);
                    mMaxId = mHometimeAdapter.getMaxId();
                    populateMentionsTimelineHistory(15, mMaxId);
                }
                else {
                    Timber.d("STOP onLoadMore page %d", page);
                }
            }
        };
        mRvHometimeline.addOnScrollListener(mEndlessRecyclerViewScrollListener);
    }

    private void populateMentionsTimelineHistory(int count, String mMaxid) {
        Timber.d("populateMentionsTimeHistory");

        if(Utilities.isNetworkAvailable(getActivity())) {
            TwitterApplication.getRestClient().getUserTimeline(mScreenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    mHometimeAdapter.clearAll();
                    mHometimeAdapter.addAll(Tweet.fromJSONArray(response));
                    mEndlessRecyclerViewScrollListener.resetState();

                    mMaxId = mHometimeAdapter.getMaxId();
                    Timber.d("onSuccess Home Timeline: [%s] %s", mMaxId, response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Timber.e(throwable, "onFailure statuscode %d throwable %s responseString %s", statusCode, throwable.getMessage(), responseString);
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Network Unavailable - check valid connection", Toast.LENGTH_LONG).show();
        }
    }

    private void setupSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateMentionsTimelineRefresh(20, 1);
            }
        });
    }

    private void populateMentionsTimelineRefresh(int count, int since_id) {
        Timber.d("populateHomeTimeline");

        if(Utilities.isNetworkAvailable(getActivity())) {
            mSwipeRefreshLayout.setRefreshing(true);
            TwitterApplication.getRestClient().getUserTimeline(mScreenName, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Timber.e(throwable, "onFailure statuscode %d throwable %s responseString %s", statusCode, throwable.getMessage(), responseString);
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    mHometimeAdapter.clearAll();
                    mHometimeAdapter.addAll(Tweet.fromJSONArray(response));

                    mSwipeRefreshLayout.setRefreshing(false);
                    mMaxId = mHometimeAdapter.getMaxId();

                    Timber.d("onSuccess Home Timeline: [%s] %s", mMaxId, response.toString());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Network Unavailable - check valid connection", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);

        return userTimelineFragment;
    }

    private void populateUserTimeline() {
        if (Utilities.isNetworkAvailable(getActivity())) {
            TwitterApplication.getRestClient().getUserTimeline(mScreenName, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Timber.e(throwable, "statusCode %d", statusCode);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    mHometimeAdapter.addAll(Tweet.fromJSONArray(response));
                    Timber.d("onSuccess Home Timeline: %s", response.toString());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Network Unavailable - check valid connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
