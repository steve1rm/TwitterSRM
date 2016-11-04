package com.codepath.apps.simpletweets;

import android.content.Context;
import android.net.ParseException;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.codepath.apps.simpletweets.R.id.tvBody;

/**
 * Created by steve on 10/28/16.
 */

/* Taking the tweet object and turning them
   into views displayed in the list */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    /* Override and setup custom template */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Get the tweet */
        Tweet tweet = getItem(position);

        /* Inflate the template */
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tweet, parent, false);

            ImageView ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            TextView tvUsername = (TextView)convertView.findViewById(R.id.tvUserName);
            TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            TextView tvCreatedAt = (TextView)convertView.findViewById(R.id.date);
            String formattedDate = Utilities.getRelativeTimeAgo(tweet.getCreatedAt());

            tvCreatedAt.setText(formattedDate);
            String screenName = "@" + tweet.getUser().getScreenName();
            tvUsername.setText(screenName);
            tvBody.setText(tweet.getBody());

            ivProfileImage.setImageResource(android.R.color.transparent);

            Glide.with(parent.getContext())
                    .load(tweet.getUser().getProfileImageUrl())
                    .centerCrop()
                    .crossFade()
                    .into(ivProfileImage);
        }

        return convertView;
    }
}
