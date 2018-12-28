package com.kalelman.twitter_clone.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kalelman.twitter_clone.R;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_feed, container, false);
        ButterKnife.bind(this, view);

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
                // Metodo que detona la funcionalidad de Google
                //startAnalyze(textTweet.getText().toString());
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

}
