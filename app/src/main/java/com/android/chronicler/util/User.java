package com.android.chronicler.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by andrea on 16.7.2015.
 */
public class User {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase = null;

    public User(Context context) {
        this.userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }
    // userLocalDatabase has:
    // "using_box" : true / false
    // "0_constants" : user's name, password and spinner position for Tutor web main http
    // "0_sessionVars" : user's cookie, user's balance for Tutor web main http
    // "1_constants": user's name, password and spinner position for Education in a Suitcase (box.tutor_web)
    // "1_sessionVars": user's cookie, user's balance for Education in a Suitcase (box.tutor_web)

    public void storeUserData(String usrName, String cookie, String selectedPos) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        if (selectedPos.equals("1")) { // Box tutor-web login-info
            spEditor.putString("1_user",usrName);
            spEditor.putInt("1_balance", 0);
            spEditor.putString("1_cookie", cookie);

        } else { // Default tutor-web login info
            spEditor.putString("0_user", usrName);
            spEditor.putInt("0_balance", 0);
            spEditor.putString("0_cookie", cookie);
        }

        spEditor.putBoolean("using_box", (selectedPos.equals("1")));
        spEditor.commit();
    }

    public void setUserBalance(int balance) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        if(this.usingBox()) spEditor.putInt("1_balance", balance);
        else spEditor.putInt("0_balance", balance);
        spEditor.commit();
    }

    public int getUserBalance() {
        if(this.usingBox()) return userLocalDatabase.getInt("1_balance", 0);
        else return userLocalDatabase.getInt("0_balance", 0);
    }

    public void setUserCookie(String cookie) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        if(this.usingBox()) spEditor.putString("1_cookie", cookie);
        else spEditor.putString("0_cookie", cookie);
        spEditor.commit();
    }

    public String getUserCookie() {
        if(this.usingBox()) return userLocalDatabase.getString("1_cookie", "__ac_deleted");
        else return userLocalDatabase.getString("0_cookie", "__ac_deleted");
    }

    public String[] getUserData() {
        String[] userData = new String[2];
        if(this.usingBox()) {
            userData[0] = userLocalDatabase.getString("1_user", "");
        }
        else {
            userData[0] = userLocalDatabase.getString("0_user", "");
        }

        return userData;

    }

    public boolean usingBox() {
        return userLocalDatabase.getBoolean("using_box", false);
    }

    public void setBox(boolean using_box) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("using_box", using_box);
        spEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        if (this.usingBox()) { // Box tutor-web login-info
            spEditor.putString("1_user",null);
            spEditor.putInt("1_balance", 0);
            spEditor.putString("1_cookie", null);

        } else { // Default tutor-web login info
            spEditor.putString("0_user", null);
            spEditor.putInt("0_balance", 0);
            spEditor.putString("0_cookie", null);
        }

        spEditor.commit();
    }

    public void clearSessionData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        if (this.usingBox()) { // Box tutor-web login-info
            spEditor.putInt("1_balance", 0);
            spEditor.putString("1_cookie", null);

        } else { // Default tutor-web login info
            spEditor.putInt("0_balance", 0);
            spEditor.putString("0_cookie", null);
        }

        spEditor.commit();
    }

    public boolean userInSession() {
        if (this.usingBox()) return (userLocalDatabase.getString("1_cookie", null) != null);
        else return (userLocalDatabase.getString("0_cookie", null) != null);
    }

    public boolean isKnownUser() {
        if(this.usingBox()) return (userLocalDatabase.getString("1_user", null) != null);
        else return (userLocalDatabase.getString("0_user", null) != null);
    }
}
