package com.android.chronicler.util;

import android.util.Log;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;

/**
 * Created by andrea on 9.2.2016.
 */
public class ChroniclerRestClient {
    private static final String BASE_URL = "https://chronicler-webapp.herokuapp.com";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.i("LOGIN", "this is the url " + BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}
