package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.HometimelineAdapter;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.utils.Utilities;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileView extends Fragment {

    @BindView(R.id.ivUserProfileHeader) ImageView mIvUserProfileHeader;
    @BindView(R.id.ivUserProfile) CircleImageView mIvUserProfile;
    @BindView(R.id.tvDate) TextView mTvDate;
    @BindView(R.id.tvScreenName) TextView mTvScreenName;
    @BindView(R.id.tvDescription) TextView mTvDescription;
    @BindView(R.id.tvFollowingNumber) TextView mTvFollowingNumber;
    @BindView(R.id.tvFollowersNumber) TextView mTvFollowersNumber;
    @BindView(R.id.rvUserProfile) RecyclerView mRvUserProfile;

    private HometimelineAdapter mHometimeAdapter;
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private String mMaxId;
    private Unbinder mUnbinder;
    private Tweet mTweet;

    public UserProfileView() {
        // Required empty public constructor
    }

    public static UserProfileView newInstance(Tweet tweet) {
        UserProfileView userProfileView = new UserProfileView();
        Bundle args = new Bundle();

        args.putParcelable("tweetextra", Parcels.wrap(tweet));
        userProfileView.setArguments(args);
        return userProfileView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mTweet = Parcels.unwrap(bundle.getParcelable("tweetextra"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_profile, container, false);

        mUnbinder = ButterKnife.bind(UserProfileView.this, view);

        setupRecylcerView();

        populateFields();

        populateHomeTimeline(20, 1);

        return view;
    }

    private void setupRecylcerView() {
        Timber.d("setupRecyclerView");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvUserProfile.setLayoutManager(linearLayoutManager);
        mHometimeAdapter = new HometimelineAdapter(getActivity(), new ArrayList<Tweet>());
        mRvUserProfile.setAdapter(mHometimeAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRvUserProfile.addItemDecoration(dividerItemDecoration);

        setupEndlessScrolling(linearLayoutManager);
    }

    private void setupEndlessScrolling(LinearLayoutManager linearLayoutManager) {
        Timber.d("setupEndlessScrolling");

        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Timber.d("onLoadMore page %d [%s]", page, mMaxId);
                mMaxId = mHometimeAdapter.getMaxId();
                //    populateHomeTimeHistory(15, mMaxId);
            }
        };
        mRvUserProfile.addOnScrollListener(mEndlessRecyclerViewScrollListener);
    }

    private void populateHomeTimeline(int count, int since_id) {
        Timber.d("populateHomeTimeline");

        TwitterApplication.getRestClient().getUserTimeline(mTweet.getUser().getScreenName(), new JsonHttpResponseHandler() {
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

    private void populateHomeTimeHistory(int count, final String maxId) {
        Timber.d("populateHomeTimeHistory");

        TwitterApplication.getRestClient().getHomeTimelineExt(count, maxId, new JsonHttpResponseHandler() {
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

    private void populateFields() {
        Glide.with(getActivity())
                .load(mTweet.getUser().getProfileBackgroundImageUrl())
                .into(mIvUserProfileHeader);

        Glide.with(getActivity())
                .load(mTweet.getUser().getProfileImageUrl())
                .into(mIvUserProfile);

        final String formattedDate = Utilities.getRelativeTimeAgo(mTweet.getCreatedAt());
        mTvDate.setText(formattedDate);
        final String screenName = "@" + mTweet.getUser().getScreenName();
        mTvScreenName.setText(screenName);
        mTvDescription.setText(mTweet.getBody());
        mTvFollowersNumber.setText(String.valueOf(mTweet.getUser().getFollowersCount()));
        mTvFollowingNumber.setText(String.valueOf(mTweet.getUser().getFollowingCount()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
