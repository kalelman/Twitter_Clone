package com.kalelman.twitter_clone.view.activity;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * @author Erick Rojas Perez</br><br>erick_rojas_perez@hotmail.com</br>
 * @date December/24/2018
 * @description Class for Request access to the Parse Server by Bitnami
 */
public class StarterConexion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("ec7b7d1393e4be74b268f19ac4f11f037c153371") // appId
                .clientKey("1f84f675133f172a06255e53817131342eaf696e") // masterKey
                .server("http://3.17.128.118:80/parse/") // serverURL
                .build()
        );

        //ParseUser.logOut();

        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
