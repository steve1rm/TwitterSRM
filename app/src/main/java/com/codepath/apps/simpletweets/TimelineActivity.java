package com.codepath.apps.simpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TimelineActivity extends AppCompatActivity implements HomeTimelineFragment.ProgressBarListener {

    private Unbinder mUnbinder;
    private MenuItem mMuActionProgressItem;

    @BindView(R.id.viewpager) ViewPager mViewpager;
    @BindView(R.id.tabs) PagerSlidingTabStrip mTabs;
    @BindView(R.id.tbTweeter) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mUnbinder = ButterKnife.bind(TimelineActivity.this);

        /** Setup tool bar */
        setupToolbar();

        /** Set the viewpager adapter for the pager */
        TweetPagerAdapter tweetPagerAdapter = new TweetPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(tweetPagerAdapter);

        /** Attach the pager tabs to the viewpager */
        mTabs.setViewPager(mViewpager);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onProgressBarHide() {
        mMuActionProgressItem.setVisible(false);
    }

    @Override
    public void onProgressBarShow() {
       mMuActionProgressItem.setVisible(true);
    }

    public void onProfileView(MenuItem menuItem) {
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   mMuActionProgressItem.setVisible(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMuActionProgressItem = menu.findItem(R.id.muActionProgress);

        ProgressBar progressBar = (ProgressBar) MenuItemCompat.getActionView(mMuActionProgressItem);

        return super.onPrepareOptionsMenu(menu);
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
