package com.codepath.apps.simpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

import static com.codepath.apps.simpletweets.R.id.ivProfileImage;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient mTwitterClient;
    private User mUser;

    private Unbinder mUnbinder;
    @BindView(R.id.tbTweeter) Toolbar mToolbar;
    @BindView(R.id.tvTagLine) TextView mTvTagline;
    @BindView(R.id.tvFollowers) TextView mTvFollowers;
    @BindView(R.id.tvFollowing) TextView mTvFollowing;
    @BindView(ivProfileImage) ImageView mIvProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTwitterClient = TwitterApplication.getRestClient();

        mUnbinder = ButterKnife.bind(ProfileActivity.this);

        setupToolbar();

        mTwitterClient.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Timber.d("onSuccess: %s", response.toString());
                mUser = User.fromJSON(response);

                getSupportActionBar().setTitle("@" + mUser.getScreenName());

                populateProfileHeader();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e(throwable, "onFailure");
            }
        });

        if(savedInstanceState == null) {
            final String screenName = getIntent().getStringExtra("screen_name");
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, userTimelineFragment, "usertimelinefragment");
            fragmentTransaction.commit();
        }
    }

    private void populateProfileHeader() {
        mTvTagline.setText(mUser.getTagline());

        mTvFollowers.setText(String.valueOf(mUser.getFollowersCount()));
        mTvFollowing.setText(String.valueOf(mUser.getFollowingCount()));
        Picasso.with(ProfileActivity.this).load(mUser.getProfileImageUrl()).into(mIvProfileImage);
    }

    public void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
