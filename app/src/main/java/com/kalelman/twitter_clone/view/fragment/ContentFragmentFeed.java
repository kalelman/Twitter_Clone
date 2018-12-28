package com.kalelman.twitter_clone.view.fragment;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnalyzeSentimentRequest;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.kalelman.twitter_clone.R;

import com.kalelman.twitter_clone.view.activity.ContainerMainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kalelman.twitter_clone.commons.utils.Constants.CONTENT;
import static com.kalelman.twitter_clone.commons.utils.Constants.CREATED_AT;
import static com.kalelman.twitter_clone.commons.utils.Constants.IS_FOLLOWING;
import static com.kalelman.twitter_clone.commons.utils.Constants.TWEET;
import static com.kalelman.twitter_clone.commons.utils.Constants.TWEET_FAILED;
import static com.kalelman.twitter_clone.commons.utils.Constants.TWEET_SEND;
import static com.kalelman.twitter_clone.commons.utils.Constants.USERNAME;

public class ContentFragmentFeed extends Fragment {

    @BindView(R.id.listView_feed)
    ListView listViewFeed;

    private View view;
    private LanguageServiceClient mLanguageClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_feed, container, false);
        ButterKnife.bind(this, view);

        prepareGoogleApi();

        final List<Map<String,String>> tweetData = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TWEET);
        query.whereContainedIn(USERNAME, ParseUser.getCurrentUser().getList(IS_FOLLOWING));
        query.orderByDescending(CREATED_AT);
        query.setLimit(20);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    for (ParseObject tweet : objects) {
                        Map<String, String> tweetInfo = new HashMap<>();
                        tweetInfo.put(CONTENT, tweet.getString(TWEET));
                        tweetInfo.put(USERNAME, tweet.getString(USERNAME));
                        tweetData.add(tweetInfo);
                    }

                    SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
                            tweetData,
                            android.R.layout.simple_list_item_2,
                            new String[]{CONTENT, USERNAME},
                            new int[]{android.R.id.text1, android.R.id.text2});

                    listViewFeed.setAdapter(simpleAdapter);
                }
            }
        });

        listViewFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView textTweet = view.findViewById(android.R.id.text1);
                analyzeSentiment(textTweet.getText().toString());
            }
        });

        return view;
    }

    @OnClick(R.id.fab)
    public void writeTweet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_custom_alert_teet, null);
        final EditText edtSendTweet = dialogView.findViewById(R.id.edt_tweet);
        final Button btnSendTweet = dialogView.findViewById(R.id.btn_send_tweet);
        final Button btnCancelTweet = dialogView.findViewById(R.id.btn_cancel_tweet);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject tweet = new ParseObject(TWEET);
                tweet.put(TWEET, edtSendTweet.getText().toString());
                tweet.put(USERNAME, ParseUser.getCurrentUser().getUsername());

                tweet.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), TWEET_SEND, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), TWEET_FAILED,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnCancelTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }); dialog.show();
    }

    private void prepareGoogleApi() {
        // create the language client
        try {
            // NOTE: The line below uses an embedded credential (res/raw/credential.json).
            //       You should not package a credential with real application.
            //       Instead, you should get a credential securely from a server.
            mLanguageClient = LanguageServiceClient.create(
                    LanguageServiceSettings.newBuilder()
                            .setCredentialsProvider(() ->
                                    GoogleCredentials.fromStream(getActivity()
                                            .getResources()
                                            .openRawResource(R.raw.credential)))
                            .build());
        } catch (IOException e) {
            Log.e("ERROR-->", e.toString());
            throw new IllegalStateException("Unable to create a language client", e);
        }
    }

    private void analyzeSentiment(String texts) {
        Document doc = Document.newBuilder().setContent(texts).setType(Document.Type.PLAIN_TEXT).build();
        // Detects the sentiment of the text
        Sentiment sentiment = mLanguageClient.analyzeSentiment(doc).getDocumentSentiment();
        showAlertSentiment(sentiment);
    }

    private void showAlertSentiment(Sentiment sentiment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_custom_alert_sentiment, null);
        final ConstraintLayout layout = dialogView.findViewById(R.id.background_alert);
        final ImageView imvSentiment = dialogView.findViewById(R.id.imv_emoticon);
        final TextView txvSentiment = dialogView.findViewById(R.id.txv_sentiment_message);
        final Button btnSentiment = dialogView.findViewById(R.id.btn_sentiment);

        if (sentiment.getScore() > 0.25) {
            //show Alert Sentiment Positive
            layout.setBackgroundColor(getResources().getColor(R.color.score_positive));
            imvSentiment.setImageResource(R.drawable.ic_emoji_positive);
            txvSentiment.setText("Tweet Positive");
        } else if (sentiment.getScore() > -0.75) {
            //show Alert Sentiment Neutral
            layout.setBackgroundColor(getResources().getColor(R.color.score_neutral));
            imvSentiment.setImageResource(R.drawable.ic_emoji_neutral);
            txvSentiment.setText("Tweet Neutral");
        } else {
            //Show Alert Sentiment Negative
            layout.setBackgroundColor(getResources().getColor(R.color.score_negative));
            imvSentiment.setImageResource(R.drawable.ic_emoji_negative);
            txvSentiment.setText("Tweet Negative");
        }

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        btnSentiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }); dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // shutdown the connection
        mLanguageClient.shutdown();
    }

}
