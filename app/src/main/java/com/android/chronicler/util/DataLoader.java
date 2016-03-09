package com.android.chronicler.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.ui.WaitingActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by leo on 12.2.2016.
 */
public class DataLoader {
    public static void readySheetThenStart(final Context context, final Intent intent) {

        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                CharacterSheet character = new CharacterSheet("Bob", "Elf", "Barbarian", new String(responseBody));
                intent.putExtra("CharacterSheet", character);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody==null?"Empty response":new String(responseBody));
                Log.i("SKILLS", "Failure fetching skill data: " + response);
            }
        });
        goToWaitScreen(context);

    }

    public static void readyNewSheetThenStart(final Context context, final Intent intent, final String name, final String race, final String charClass){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Create a new character sheet object
                CharacterSheet character = new CharacterSheet(name, race, charClass, new String(responseBody));
                storeCharSheet(context, character);
                intent.putExtra("CharacterSheet", character);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody==null?"Empty response":new String(responseBody));
                Log.i("SKILLS", "Failure fetching skill data: " + response);
            }
        });
        goToWaitScreen(context);
    }

    public static void readyCreateCharThenStart(final Context context, final Intent intent){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        final ObjectMapper mapper = new ObjectMapper();
        cli.get("/raceList", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String raceListJSON = new String(responseBody);
                ArrayList<String> raceList;
                try {
                    raceList = mapper.readValue(raceListJSON, ArrayList.class);
                } catch (IOException e) {
                    onFailure(statusCode, headers, responseBody, new Error(e.getMessage()));
                    return;
                }
                intent.putStringArrayListExtra("raceList", raceList);
                cli.get("/classList", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String classListJSON = new String(responseBody);
                        ArrayList<String> classList;
                        try {
                            classList = mapper.readValue(classListJSON, ArrayList.class);
                        } catch (IOException e) {
                            onFailure(statusCode, headers, responseBody, new Error(e.getMessage()));
                            return;
                        }
                        intent.putStringArrayListExtra("classList", classList);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = (responseBody == null ? "Empty response" : new String(responseBody));
                        Log.e("CLASSLIST", "Failure fetching class list: " + response);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody==null?"Empty response":new String(responseBody));
                Log.e("RACELIST", "Failure fetching race list: " + response);
            }
        });
        goToWaitScreen(context);
    }

    public static void readyCharlistThenStart(final Context context, final Intent intent) {
        readyCharlistThenStart(context, intent, false, 0);
    }

    public static void readyCharlistThenStartForResult(final Context context, final Intent intent, int code) {
        readyCharlistThenStart(context, intent, true, code);
    }

    public static void readyCharlistThenStart(final Context context, final Intent intent, final boolean getResult, final int code) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.getUserData("/characters", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONresponse = new String(responseBody);
                ArrayList<String> content = new ArrayList<String>();

                try {

                    JSONObject jObject = new JSONObject(JSONresponse);
                    Iterator<?> keys = jObject.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        content.add(jObject.get(key).toString());
                    }
                } catch (JSONException e) {
                    Log.i("CHARLIST", "JSON EXCEPTION");
                }
                // Finally start the activity with 'content' as extra:
                intent.putExtra("CharacterList", content);
                if (getResult) {
                    ((Activity) context).startActivityForResult(intent, code);
                } else {
                    context.startActivity(intent);
                }
                Log.i("USERGET", new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("CHARLIST", "failed to send requesT???");
            }
        });
        goToWaitScreen(context);
    }

    public static void readyCampaignlistThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());
        cli.getUserData("/campaignData", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList<String> DMCampaigns = new ArrayList<>();
                ArrayList<String> PCCampaigns = new ArrayList<>();

                try {
                    JSONArray DMResponse = responseBody.getJSONObject(0).names();

                    for (int i = 0; i < DMResponse.length(); i++) {
                        DMCampaigns.add(responseBody.getJSONObject(0).getString(DMResponse.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray PCResponse = responseBody.getJSONObject(1).names();
                    for (int i = 0; i < PCResponse.length(); i++) {
                        PCCampaigns.add(responseBody.getJSONObject(1).getString(PCResponse.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("DMCampaignList", DMCampaigns);
                intent.putExtra("PCCampaignList", PCCampaigns);
                context.startActivity(intent);
            }
        });
        goToWaitScreen(context);
    }

    public static void postCampaignThenOpen(final Context context, final Intent intent, String campaignName) throws IOException {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);
        cli.postUserData("/campaignData", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("DataLoader", "Failed to post campaign");
            }
        });
        goToWaitScreen(context);
    }

    public static void storeCharSheet(Context context, CharacterSheet c){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        StringEntity charEntity = null;
        try {
            charEntity = new StringEntity(c.toJSON(), ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            Log.e("STORECHAR","Error converting character sheet to JSON");
            e.printStackTrace();
        }
        if(charEntity!= null) {
            cli.postUserData("/storeChar", charEntity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("STORECHAR", "Success: "+new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("STORECHAR", "Failure");
                    error.printStackTrace();
                }
            });
        }
    }

    public static void readyInvitesThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());
        cli.getUserData("/invites", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                Log.i("DataLoader", responseBody.toString());

                ArrayList<String> invites = new ArrayList<>();

                try {
                    JSONArray inviteJSON = responseBody.getJSONObject(0).names();

                    for (int i = 0; i < inviteJSON.length(); i++) {
                        invites.add(responseBody.getJSONObject(0).getString(inviteJSON.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("INVITES", invites);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject object) {
                Log.i("DataLoader", object.toString());
            }
        });
        goToWaitScreen(context);
    }

    private static void goToWaitScreen(Context context){
        Intent loadingScreenIntent = new Intent(context, WaitingActivity.class);
        context.startActivity(loadingScreenIntent);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static void showProgress(final boolean show, Context context) {
        // IMPORTANT: THIS COMPILES BUT DOESN'T WORK. DO NOT USE THIS YET
        // We may have to do a minor overhaul on our layouts to get this to work
        Activity activity = (Activity) context;
        final View rootView = ((Activity) context).getWindow().getDecorView().getRootView();
        final View progressView = activity.findViewById(R.id.login_progress);
        Log.i("Progress", activity.toString());
        Log.i("Progress", rootView.toString());
        Log.i("Progress", progressView.toString());
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

            rootView.setVisibility(show ? View.GONE : View.VISIBLE);
            rootView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            rootView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
