package com.codepath.apps.simpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by steve on 10/28/16.
 */

/* Parse and store the data */
@Parcel
public class Tweet {
    String body;
    /* Database id for the tweet */
    long uid;
    String strId;
    User user;
    String createdAt;
    String date;

    public Tweet() {
    }

    public String getStrId() {
        return strId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*Deserialize the json */
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        /* Extract the value out of the json */
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.strId = jsonObject.getString("id_str");
        } catch (JSONException e) {
            Timber.e(e, "Failed to deserialize");
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);

                if(tweet != null) {
                    tweets.add(tweet);
                }
            }
            catch (JSONException e) {
                Timber.e(e, "fromJSONArray failure");
                /* if a single tweet fails just going processing the other tweets */
                continue;
            }
        }

        return tweets;
    }
}
