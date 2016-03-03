package com.android.chronicler.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by andrea on 9.2.2016.
 * ChroniclerRestClient is the class that has direct communications to our server at
 * chronicler-webapp.herokuapp.com.
 *
 * It uses AsyncHttpClient (see details at http://loopj.com/android-async-http/) for asynchronous
 * get and post requests.
 *
 * This class also manages cookies with a PersistentCookieStore so if the request to be sent is user
 * specific, it checks first if the user has an unexpired cookie before sending the request.
 */
public class ChroniclerRestClient {
    private final String BASE_URL = "https://chronicler-webapp.herokuapp.com";

    private AsyncHttpClient client;
    private PersistentCookieStore cookieStore;
    private Context context;

    public ChroniclerRestClient(Context context) {
        client = new AsyncHttpClient();
        this.context = context;
        // If the Rest Client is initialized with this constructor, a persistent cookie store is
        // created. This saves all cookies from all responses gotten for the get/post requests below.
        // Will be used for a user ID to create a persistent login, cookie is called 'user' and
        // contains a UUID created by server.
        cookieStore = new PersistentCookieStore(context);
        client.setCookieStore(cookieStore);
    }

    // Generic async get request
    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Async get requests that needs to pass along user id cookie, i.e. when requesting user data
    // such as a character sheet.
    public boolean getUserData(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        List<Cookie> cookies = cookieStore.getCookies();
        Cookie userCookie = new BasicClientCookie("user", null);
        for(Cookie c : cookies) {
            if(c.getName().equals("user")) userCookie = c;
        }
        if(userCookie.getValue() == null) return false;
        client.addHeader(userCookie.getName(), userCookie.getValue());
        client.get(getAbsoluteUrl(url), params, responseHandler);
        return true;
    }

    // Needs a comment what this is for?
    public void get(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Generic async post request.
    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    // Async post requests that needs to pass along user id cookie, i.e. when updating user data
    // such as a character sheet.
    public void postUserData(String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        List<Cookie> cookies = cookieStore.getCookies();
        Cookie userCookie = new BasicClientCookie("user", "null");
        for(Cookie c : cookies) {
            if(c.getName().equals("user")) userCookie = c;
        }

        client.addHeader(userCookie.getName(), userCookie.getValue());
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
        /*try {
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String json;
            while((json = br.readLine()) != null){
                int i;
                for(i = 4000; i<json.length(); i+=4000) {
                    Log.d("STORECHAR_JSON", json.substring(i - 4000, i));
                }
                Log.d("STORECHAR_JSON", json.substring(i - 4000));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
