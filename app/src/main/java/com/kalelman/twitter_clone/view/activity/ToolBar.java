package com.kalelman.twitter_clone.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kalelman.twitter_clone.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Erick Rojas Perez
 * @date December/22/2018
 * @description Immplementation of a ToolBar properties using Abstract class for all project
 */
public abstract class ToolBar extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }
    }

    /**
     * Get resource from the Layout
     * @return the layout resource
     */
    public abstract int getLayoutResource();

    /**
     * Set the Title of ActionBar
     * @param titleActionBar
     */
    public void setTitleActionBar(String titleActionBar) {
        getSupportActionBar().setTitle(titleActionBar);
    }
}
