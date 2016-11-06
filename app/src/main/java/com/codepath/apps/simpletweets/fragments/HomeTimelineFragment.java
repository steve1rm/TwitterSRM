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
import com.codepath.apps.simpletweets.TwitterClient;
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

public class HomeTimelineFragment extends Fragment {
    private TwitterClient mTwitterClient;
    private String mMaxId;
    private HometimelineAdapter mHometimeAdapter;

    @BindView(R.id.rvHometimeline) RecyclerView mRvHometimeline;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout mSwipeRefreshLayout;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwitterClient = TwitterApplication.getRestClient();

        populateHomeTimeline(15, 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_timeline, container, false);

        mUnbinder = ButterKnife.bind(HomeTimelineFragment.this, view);

        setupRecylcerView();

        setupSwipeRefresh();

        return view;
    }

    private void setupSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimelineRefresh(20, 1);
            }
        });
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

        final EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Timber.d("onLoadMore page %d [%s]", page, mMaxId);
                mMaxId = mHometimeAdapter.getMaxId();
                //    populateHomeTimeHistory(15, mMaxId);
            }
        };
        mRvHometimeline.addOnScrollListener(mEndlessRecyclerViewScrollListener);
    }

    private void populateHomeTimelineRefresh(int count, int since_id) {
        Timber.d("populateHomeTimeline");

        if(Utilities.isNetworkAvailable(getActivity())) {
            mTwitterClient.getHomeTimeline(count, since_id, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Timber.e(throwable, "onFailure statuscode %d throwable %s responseString %s", statusCode, throwable.getMessage(), responseString);
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

    private void populateHomeTimeline(int count, int since_id) {
        Timber.d("populateHomeTimeline");

        if(Utilities.isNetworkAvailable(getActivity())) {
            mTwitterClient.getHomeTimeline(count, since_id, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Timber.e(throwable, "onFailure statuscode %d throwable %s responseString %s", statusCode, throwable.getMessage(), responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    mHometimeAdapter.addAll(Tweet.fromJSONArray(response));
                    mMaxId = mHometimeAdapter.getMaxId();
                    Timber.d("onSuccess Home Timeline: [%s] %s", mMaxId, response.toString());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Network Unavailable - check valid connection", Toast.LENGTH_LONG).show();
        }
    }

    private void populateHomeTimeHistory(int count, final String maxId) {
        Timber.d("populateHomeTimeHistory");

        if(Utilities.isNetworkAvailable(getActivity())) {
            mTwitterClient.getHomeTimelineExt(count, maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    mHometimeAdapter.addAll(Tweet.fromJSONArray(response));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
