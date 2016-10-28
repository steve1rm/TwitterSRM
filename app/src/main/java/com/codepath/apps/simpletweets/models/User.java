package com.codepath.apps.simpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by steve on 10/28/16.
 */

public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
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
        } catch (JSONException e) {
            Timber.e(e, "Failed to deserialize for user");
        }

        return user;
    }
}
