package com.kalelman.twitter_clone.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kalelman.twitter_clone.R;

/**
 * @autor Erick Rojas Perez</br>
 * @date December/22/2018
 * @description SplashScreen.</br>
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
