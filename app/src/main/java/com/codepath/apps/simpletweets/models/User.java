package com.codepath.apps.simpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import timber.log.Timber;

/**
 * Created by steve on 10/28/16.
 */

@Parcel
public class User {
    String name;
    long uid;
    String screenName;
    String profileImageUrl;
    String tagline;
    int followingCount;
    int followersCount;
    String profileBackgroundImageUrl;
    String userDescripton;

    public User() {
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getUserDescripton() {
        return userDescripton;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getTagline() {
        return tagline;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    /* deserialize the user */
    public static User fromJSON(JSONObject json) {
        User user = new User();

        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagline = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.profileBackgroundImageUrl = json.getString("profile_background_image_url");
            user.userDescripton = json.getString("description");

        } catch (JSONException e) {
            Timber.e(e, "Failed to deserialize for user");
        }

        return user;
    }
}
