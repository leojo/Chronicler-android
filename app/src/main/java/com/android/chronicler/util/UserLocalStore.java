package com.android.chronicler.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by andrea on 16.7.2015.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase = null;

    public UserLocalStore(Context context) {
        this.userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(String usrName, String cookie) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        // Default tutor-web login info
        spEditor.putString("userName", usrName);
        spEditor.putString("sessionID", cookie);
        spEditor.commit();
    }



    public String[] getUserData() {
        String[] userData = new String[2];
        userData[0] = userLocalDatabase.getString("userName", "");

        return userData;
    }

    public String getUserCookie() {
        return userLocalDatabase.getString("sessionID", "session_expired");
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("userName", null);
        spEditor.putString("sessionID", null);
        spEditor.commit();
    }

    public void clearSession() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("sessionID", null);
        spEditor.commit();
    }

    public boolean userInSession() {
        return (userLocalDatabase.getString("sessionID", null) != null);
    }


    public boolean isKnownUser() {
        return (userLocalDatabase.getString("userName", null) != null);
    }
}
