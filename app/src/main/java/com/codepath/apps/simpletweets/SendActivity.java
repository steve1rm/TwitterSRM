package com.codepath.apps.simpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

public class SendActivity extends AppCompatActivity {

    @BindView(R.id.etComposeTweet) EditText mEtComposeTweet;

    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        mUnbinder = ButterKnife.bind(SendActivity.this);
    }

    private void sendTweet(String message) {
        TwitterApplication.getRestClient().sendTwitterMessage(message, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("statusCode %d %s", statusCode, errorResponse.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Timber.d("onSuccess Tweeting statuscode: %d responseBody %s", statusCode, response.toString());
            }
        });
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnSend)
    public void btnSendTweet() {
        /* Send tweet */
        final String message = mEtComposeTweet.getText().toString();
        sendTweet(message);

/*
        Intent intent = new Intent(SendActivity.this, TimelineActivity.class);
        startActivity(intent);
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
