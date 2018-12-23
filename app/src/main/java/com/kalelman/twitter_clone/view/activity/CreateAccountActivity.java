package com.kalelman.twitter_clone.view.activity;

import android.os.Bundle;

import com.kalelman.twitter_clone.R;

import butterknife.ButterKnife;

public class CreateAccountActivity extends ToolBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_create_account;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
