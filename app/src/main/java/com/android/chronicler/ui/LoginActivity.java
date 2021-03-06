package com.android.chronicler.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.chronicler.MainActivity;
import com.android.chronicler.R;
import com.android.chronicler.util.ChroniclerRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A login screen that offers login via email/password.
 *
 * Please note: This is largely an auto-generated code for a login screen.
 * Our code is in performLogin and handleSuccess functions below.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button refuseRegisterBtn;
    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextInputLayout helpTextLayout1 = (TextInputLayout)findViewById(R.id.textinputlayout1);
        TextInputLayout helpTextLayout2 = (TextInputLayout)findViewById(R.id.textinputlayout2);
        helpTextLayout1.setHintTextAppearance(R.style.HelpText);
        helpTextLayout2.setHintTextAppearance(R.style.HelpText);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        refuseRegisterBtn = (Button)findViewById(R.id.refuseRegister);
        refuseRegisterBtn.setVisibility(View.GONE);
        refuseRegisterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(username)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            performLogin(username, password);
        }
    }

    // Function that performs login by posting a request to the server
    // using ChroniclerRestClient.
    // If the login is successful, it will respond with a cookie
    // generated by the server. This cookie is stored in a persistent
    // cookie store so that the user doesn't need to login everytime;
    // instead this cookie is sent with every request to identify the user
    // server-side until it expires (in which case, the user is redirected to the
    // login screen again).
    private void performLogin(String username, String password) {
        ChroniclerRestClient client = new ChroniclerRestClient(this);

        RequestParams user_data = new RequestParams();
        // Pass the parameters, TODO: Hash password ?
        user_data.put("username", username);
        user_data.put("password", password);
        client.post("/login", user_data, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.i("LOGIN", "This is the response: "+new String(response));
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject res = new JSONObject(new String(response));
                    String code = res.getString("code");
                    if(code.equals("success")) {
                        handleSuccess();
                    } else if(code.equals("nouser")) {
                        showMessage(res.getString("message"));
                        offerRegister();
                    } else if(code.equals("failure")) {
                        showMessage(res.getString("message"));
                    } else {
                        showMessage("This should never happen. Something went horribly wrong.");
                    }
                }catch(JSONException e) {
                        showMessage("Invalid JSON response from server");
                }

                /*Log.i("LOGIN", "this is the response "+new String(response));
                String cookie = "";
                // FIXME: DEPRECATED:  asynchttpclient automatically stores cookies in a persistent cookie store,
                //              we don't need to get the cookie anymore.
                // Get the cookie response so we can store it in a cookie store.
                for(Header h : headers) {
                    Log.i("LOGIN", h.toString());
                    if (h.getName().equals("Set-Cookie") && h.getValue().contains("user")) {
                        cookie = h.getValue().split(";")[0];
                    }
                }
                handleSuccess(cookie);*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.i("LOGIN", "Failed to send the http request");
                showMessage("Failed to send request to server. Please try again later");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void showMessage(String message) {
        showProgress(false);
        ((TextView)findViewById(R.id.messageView)).setText(message);
    }

    private void offerRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        refuseRegisterBtn.setVisibility(View.VISIBLE);


        Button signInButton = (Button) findViewById(R.id.email_sign_in_button);
        signInButton.setText("Register");
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();
                performRegister(username, password);
            }
        });
    }

    private void backToLogin() {
        refuseRegisterBtn.setVisibility(View.GONE);

        Button signInButton = (Button) findViewById(R.id.email_sign_in_button);
        signInButton.setText("Signin Or Register");
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                performLogin(username, password);
            }
        });

        showMessage("");
    }

    private void performRegister(String username, String password) {
        Log.i("REGISTER", "Is register really running???");
        ChroniclerRestClient client = new ChroniclerRestClient(this);

        RequestParams user_data = new RequestParams();
        // Pass the parameters, TODO: Hash password ?
        user_data.put("username", username);
        user_data.put("password", password);
        client.post("/androidRegister", user_data, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject res = new JSONObject(new String(response));
                    String code = res.getString("code");
                    if(code.equals("success")) {
                        handleSuccess();
                    } else if(code.equals("failure")) {
                        showMessage(res.getString("message"));
                    } else {
                        showMessage("This should never happen. Something went horribly wrong.");
                    }
                }catch(JSONException e) {
                    showMessage("Invalid JSON response from server");
                }

                /*Log.i("LOGIN", "this is the response "+new String(response));
                String cookie = "";
                // FIXME: DEPRECATED:  asynchttpclient automatically stores cookies in a persistent cookie store,
                //              we don't need to get the cookie anymore.
                // Get the cookie response so we can store it in a cookie store.
                for(Header h : headers) {
                    Log.i("LOGIN", h.toString());
                    if (h.getName().equals("Set-Cookie") && h.getValue().contains("user")) {
                        cookie = h.getValue().split(";")[0];
                    }
                }
                handleSuccess(cookie);*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.i("REGISTER", "Failed to send the http request");
                showMessage("Failed to send request to server. Please try again later");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    // Stores the cookie in the cookie store and starts the next activity.
    private void handleSuccess() {
        /*// FIXME: DEPRECATED: asynchttpclient automatically stores cookies in a persistent cookie store.
        Log.i("LOGIN_COOKIE", cookie);
        UserLocalStore store = new UserLocalStore(getApplicationContext());
        store.storeUserData(username, cookie);
*/
        // Redirect to main screen:
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    // TODO: Implement
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true; //email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Default progress function, should be replaced
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


    // Redirect to login screen whenever the cookie expires
    public void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    // Default function to autocomplete emails
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

