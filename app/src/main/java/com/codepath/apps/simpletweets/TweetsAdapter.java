package com.codepath.apps.simpletweets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 10/28/16.
 */

/* Taking the tweet object and turning them
   into views displayed in the list */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, 0, tweets);
    }

    /* Override and setup custom template */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Get the tweet */
        Tweet tweet = getItem(position);

        /* Inflate the tempate */
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tweet, parent, false);

            ImageView ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            TextView tvUsername = (TextView)convertView.findViewById(R.id.tvUserName);
            TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);

            tvUsername.setText(tweet.getUser().getScreenName());
            tvBody.setText(tweet.getBody());

            ivProfileImage.setImageResource(android.R.color.transparent);
            Picasso.with(parent.getContext())
                    .load(tweet.getUser().getProfileImageUrl())
                    .into(ivProfileImage);
        }

        return convertView;
    }
}
