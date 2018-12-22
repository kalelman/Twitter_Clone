package com.kalelman.twitter_clone.commons.utils;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author Erick Rojas Perez</br>
 * @date December/22/2018</br>
 * @description Class used to manage the methods used throughout the App.</br>
 */
public class Tools {

    private static String className;

    /**
     * Class constructor.</br>
     */
    public Tools() {
        className = getClass().toString();
    }

    /**
     * Validate an email
     * @param email string with mail to validate.
     * @return True or False.
     */
    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }

    /**
     * Hides the current open keyboard
     *
     * @param activity the activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(className, e.getMessage());
        }
    }
}
