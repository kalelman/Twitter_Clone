package com.kalelman.twitter_clone.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kalelman.twitter_clone.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentFragmentFeed extends Fragment {

    @BindView(R.id.text_test)
    TextView textView;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_feed, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
