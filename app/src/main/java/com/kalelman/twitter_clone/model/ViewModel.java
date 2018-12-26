package com.kalelman.twitter_clone.model;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ViewModel {

    private String user;

    public String getUser() {

        return getFollowers();
    }

    private String getFollowers() {
        /*ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        user.getUsername();
                    }
                }
            }
        });*/

        return user = ParseUser.getCurrentUser().getUsername();
    }

    public void setUser(String user) {
        this.user = user;
    }
}
