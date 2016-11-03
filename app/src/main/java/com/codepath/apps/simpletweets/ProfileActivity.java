package com.codepath.apps.simpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

import static java.util.Collections.addAll;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient mTwitterClient;
    private User mUser;

    private Unbinder mUnbinder;
    @BindView(R.id.tbTweeter) Toolbar mToolbar;

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

    public void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
