package com.codepath.apps.simpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import timber.log.Timber;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "EjyCGGC5hxHqJOXvxk4inVqf0";       // Change this
	public static final String REST_CONSUMER_SECRET = "m0Ni7GMoiuOjAcvpaVBsPBkdVPVeR4ExHWFqIf4oK2c31ePOjv"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://TwitterSRM"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

/*  Home timeline
	GET statuses/user_timeline.json
    count=25
	since_id=1
*/
    public void getUserTimeline(final String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        /* Specify the params */
        RequestParams params = new RequestParams();
        params.put("count", 20);
        params.put("screen_name", screenName);
        /* Filtered list of the most recent tweets */
        //params.put("since_id", 1);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimelineHistory(final String screenName, final String max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        /* Specify the params */
        RequestParams params = new RequestParams();
        params.put("count", 20);
        params.put("max_id", max_id);
        params.put("screen_name", screenName);
        /* Filtered list of the most recent tweets */
        //params.put("since_id", 1);
        getClient().get(apiUrl, params, handler);
    }
    /* Send status tweet
       POST statuses/update.json
     */

	public void sendTwitterMessage(String message, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", message);
        getClient().post(apiUrl, params, handler);
    }

    /* User timeline
    statuses/home_timeline.json
    */
    public void getHomeTimeline(int count, int since_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", since_id);
        getClient().get(apiUrl, params, handler);
    }

    /* Get new tweets */
    public void getHomeTimelineHistory(int count, String max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();

        params.put("count", count);
        params.put("max_id", max_id);
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(int count, int since_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", since_id);
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimelineHistory(int count, String max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("max_id", max_id);
        getClient().get(apiUrl, params, handler);
    }


    public void getUserInfo(AsyncHttpResponseHandler handler) {
        Timber.d("getUserInfo");
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }
}