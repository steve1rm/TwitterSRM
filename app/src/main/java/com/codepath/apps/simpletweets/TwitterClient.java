package com.codepath.apps.simpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static org.scribe.model.Verb.GET;

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

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
/*
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}
*/

/*  Home timeline
	GET statuses/user_timeline.jso
    count=25
	since_id=1
*/
    public void getUserTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        /* Specify the params */
        RequestParams params = new RequestParams();
        params.put("count", 25);
        /* Filtered list of the most recent tweets */
        params.put("since_id", 1);
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
    public void getHomeTimeline(int max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
  //      params.put("page", pageCount);
        params.put("since_id", 1);
  //      params.put("max_id", "792420991553208320");
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        getClient().get(apiUrl, params, handler);
    }


    /* Get new tweets */
    public void getHomeTimelineExt(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();

        params.put("count", 25);
/*
        params.put("since_id", 1);
        params.put("max_id", "792420991553208320");
*/
        getClient().get(apiUrl, params, handler);
    }

 /*   public void getMentionsTimeline(int pageCount, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        //      params.put("page", pageCount);
        //      params.put("since_id", 1);
     //   params.put("max_id", "792420991553208320");
        getClient().get(apiUrl, params, handler);
    }
*/

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}