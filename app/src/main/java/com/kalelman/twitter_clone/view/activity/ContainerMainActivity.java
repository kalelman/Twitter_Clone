package com.kalelman.twitter_clone.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.view.fragment.ContentFragmentFeed;
import com.kalelman.twitter_clone.view.fragment.ContentFragmentFollowers;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContainerMainActivity extends ToolBar {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txv_toolbar)
    TextView txvToolBar;
    /*@BindView(R.id.txv_profile)
    TextView txvProfile;*/
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleActionBar("");
        setText();
        showMainScreen();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.text_openDrawer, R.string.text_closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.followers:
                        showMainScreen();
                        return true;

                    case R.id.user_feed:
                        showFeedUser();
                        return true;

                    case R.id.log_out:
                        userLogOut();
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    private void userLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_custom_alert_logout, null);
        final Button btnAccept = dialogView.findViewById(R.id.btn_alert_accept);
        final Button btnCancel = dialogView.findViewById(R.id.btn_alert_cancel);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                dialog.dismiss();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }); dialog.show();
    }

    /**
     * Inflate the menu of the ToolBar
     * @param menu
     * @return boolean true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main_container;
    }

    private void setText() {
        txvToolBar.setText(getResources().getText(R.string.text_followers));
        //txvProfile.setText(ParseUser.getCurrentUser().getUsername());
    }

    private String getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUsername().toString();
        }
        return "User";
    }

    private void showMainScreen() {
        ContentFragmentFollowers fragmentFollowers = new ContentFragmentFollowers();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragmentFollowers);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void showFeedUser() {
        ContentFragmentFeed fragmentFeed = new ContentFragmentFeed();
        FragmentTransaction ff = getSupportFragmentManager().beginTransaction();
        ff.replace(R.id.fragment, fragmentFeed);
        ff.addToBackStack(null);
        ff.commit();
    }

    @Override
    public void onBackPressed() {
        userLogOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
