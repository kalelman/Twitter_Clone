package com.kalelman.twitter_clone.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.view.activity.ContainerMainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kalelman.twitter_clone.commons.utils.Constants.IS_FOLLOWING;
import static com.kalelman.twitter_clone.commons.utils.Constants.TWEET;
import static com.kalelman.twitter_clone.commons.utils.Constants.TWEET_FAILED;
import static com.kalelman.twitter_clone.commons.utils.Constants.TWEET_SEND;
import static com.kalelman.twitter_clone.commons.utils.Constants.USERNAME;

public class ContentFragmentFollowers extends Fragment {

    @BindView(R.id.listView_followers)
    ListView listView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private View view;
    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_followers, container, false);
        ButterKnife.bind(this, view);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_checked,users);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()) {
                    Log.i("Info","Checked!");
                    ParseUser.getCurrentUser().add(IS_FOLLOWING ,users.get(i));
                } else {
                    Log.i("Info", "NOT Checked!");
                    ParseUser.getCurrentUser().getList(IS_FOLLOWING).remove(users.get(i));
                    List tempUsers = ParseUser.getCurrentUser().getList(IS_FOLLOWING);
                    ParseUser.getCurrentUser().remove(IS_FOLLOWING);
                    ParseUser.getCurrentUser().put(IS_FOLLOWING,tempUsers);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereNotEqualTo(USERNAME,ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        users.add(user.getUsername());
                    }

                    adapter.notifyDataSetChanged();

                    for (String username : users) {
                        if (ParseUser.getCurrentUser().getList(IS_FOLLOWING).contains(username)) {
                            listView.setItemChecked(users.indexOf(username), true);
                        }
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
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
