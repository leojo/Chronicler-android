package com.android.chronicler.util;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.loopj.android.http.*;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by andrea on 9.2.2016.
 */
public class ChroniclerRestClient {
    private static final String BASE_URL = "https://chronicler-webapp.herokuapp.com";

    private static AsyncHttpClient client;
    private static PersistentCookieStore cookieStore;
    public ChroniclerRestClient() {
        client = new AsyncHttpClient();
    }

    public ChroniclerRestClient(Context context) {
        client = new AsyncHttpClient();
        // If the Rest Client is initialized with this constructor, a persistent cookie store is
        // created. This saves all cookies from all responses gotten for the get/post requests below.
        // Will be used for a user ID to create a persistent login, cookie is called 'user' and
        // contains a UUID created by server.
        cookieStore = new PersistentCookieStore(context);
        client.setCookieStore(cookieStore);
        Log.i("LOGINDEBUG", "Created the cookie store! ");
        List<Cookie> cookies = cookieStore.getCookies();
        for(Cookie c : cookies) {
            Log.i("LOGINDEBUG", "In the cookie store: "+c.toString());
        }
    }

    // Generic async get request
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        List<Cookie> cookies = cookieStore.getCookies();
        for(Cookie c : cookies) Log.i("LOGINDEBUG", "GENERIC GET REQUEST: Found cookie " + c);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Async get requests that needs to pass along user id cookie, i.e. when requesting user data
    // such as a character sheet.
    public static boolean getUserData(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        List<Cookie> cookies = cookieStore.getCookies();
        Cookie userCookie = new BasicClientCookie("user", null);
        Log.i("LOGINDEBUG", "GETUSERDATA: Checking if we have any cookies ......");
        for(Cookie c : cookies) {
            Log.i("LOGINDEBUG", "GETUSERDATA: "+c.toString());
            if(c.getName().equals("user")) userCookie = c;
        }
        Log.i("LOGINDEBUG", "GETUSERDATA userCookie name " + userCookie.getName());
        Log.i("LOGINDEBUG", "GETUSERDATA userCookie val " + userCookie.getValue());
        if(userCookie.getValue() == null) return false;
        client.addHeader(userCookie.getName(), userCookie.getValue());
        client.get(getAbsoluteUrl(url), params, responseHandler);
        return true;
    }

    // Needs a comment what this is for?
    public static void get(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Generic async post request.
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    // Async post requests that needs to pass along user id cookie, i.e. when updating user data
    // such as a character sheet.
    public static void postUserData(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        List<Cookie> cookies = cookieStore.getCookies();
        Cookie userCookie = new BasicClientCookie("user", "null");
        for(Cookie c : cookies) {
            if(c.getName().equals("user")) userCookie = c;
        }
        client.addHeader(userCookie.getName(), userCookie.getValue());
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.i("LOGIN", "this is the url " + BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}
