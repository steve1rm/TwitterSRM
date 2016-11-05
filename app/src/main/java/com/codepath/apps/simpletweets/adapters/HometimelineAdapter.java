package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.ProfileActivity;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * Created by steve on 11/4/16.
 */

public class HometimelineAdapter extends RecyclerView.Adapter<HometimelineAdapter.HometimelineViewHolder> {

    private List<Tweet> mTweetList = Collections.emptyList();
    private WeakReference<Context> mContextRef;
    private String mMaxId;

    public HometimelineAdapter(Context context, List<Tweet> tweetList) {
        mContextRef = new WeakReference<>(context);
        mTweetList = tweetList;
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    @Override
    public HometimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_tweet_row, parent, false);

        return new HometimelineViewHolder(view);
    }

    public void addAll(List<Tweet> tweets) {
        mTweetList.addAll(tweets);
        notifyItemRangeInserted(0, tweets.size());
    }

    public Tweet getLastTweet() {
        if(!mTweetList.isEmpty()) {
            return mTweetList.get(mTweetList.size() - 1);
        }

        return null;
    }

    public Tweet getFirstTweet() {
        if(!mTweetList.isEmpty()) {
            return mTweetList.get(0);
        }

        return null;
    }

    public String getMaxId() {
        Tweet tweet = getLastTweet();

        if(tweet != null) {
            mMaxId = tweet.getStrId();
            Timber.d("getMaxId before subtraction [%s]", mMaxId);
            /* Minus 1 from the max id */
            mMaxId = Utilities.subtractOneFromID(mMaxId);
            Timber.d("getMaxId after subtraction [%s]", mMaxId);

            return mMaxId;
        }
        else {
            return "";
        }
    }

    @Override
    public void onBindViewHolder(HometimelineViewHolder holder, int position) {
        String formattedDate = Utilities.getRelativeTimeAgo(mTweetList.get(position).getCreatedAt());
        holder.mDate.setText(formattedDate);
        String screenName = "@" + mTweetList.get(position).getUser().getScreenName();
        holder.mTvUserName.setText(screenName);
        holder.mTvBody.setText(mTweetList.get(position).getBody());

        holder.mIvProfileImage.setImageResource(android.R.color.transparent);

        Glide.with(mContextRef.get())
                .load(mTweetList.get(position).getUser().getProfileImageUrl())
                .centerCrop()
                .crossFade()
                .into(holder.mIvProfileImage);
    }

    class HometimelineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfileImage) ImageView mIvProfileImage;
        @BindView(R.id.tvUserName) TextView mTvUserName;
        @BindView(R.id.date) TextView mDate;
        @BindView(R.id.tvBody) TextView mTvBody;

        public HometimelineViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(HometimelineViewHolder.this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Tweet tweet = mTweetList.get(getLayoutPosition());

                    Timber.d("onClick %d %s", getAdapterPosition(), tweet.getUser().getName());
                    Intent intent = new Intent(mContextRef.get(), ProfileActivity.class);
                    intent.putExtra("screen_name", tweet.getUser().getName());
                    mContextRef.get().startActivity(intent);
                }
            });
        }
    }
}
