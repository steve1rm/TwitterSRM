package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;

import org.parceler.Parcel;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileView extends Fragment {

    @BindView(R.id.ivUserProfileHeader) ImageView mIvUserProfileHeader;
    @BindView(R.id.ivUserProfile) ImageView mIvUserProfile;
    @BindView(R.id.tvTitle) TextView mTvTitle;
    @BindView(R.id.tvScreenName) TextView mTvScreenName;
    @BindView(R.id.tvDescription) TextView mTvDescription;
    @BindView(R.id.tvFollowingNumber) TextView mTvFollowingNumber;
    @BindView(R.id.tvFollowersNumber) TextView mTvFollowersNumber;

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

        Timber.d("oncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_profile, container, false);

        mUnbinder = ButterKnife.bind(UserProfileView.this, view);



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
