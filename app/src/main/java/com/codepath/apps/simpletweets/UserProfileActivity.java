package com.codepath.apps.simpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.simpletweets.fragments.UserProfileView;
import com.codepath.apps.simpletweets.models.Tweet;

import org.parceler.Parcels;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweetextra"));

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.user_profile_container, UserProfileView.newInstance(tweet), "userprofileview");
            fragmentTransaction.commit();
        }
    }
}
