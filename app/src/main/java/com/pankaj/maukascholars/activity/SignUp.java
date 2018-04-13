package com.pankaj.maukascholars.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.application.VolleyHandling;
import com.pankaj.maukascholars.util.Constants;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 11/7/16.
 */
public class SignUp extends AppCompatActivity {

    int GOOGLE_RESULT = 200;
    LoginButton fb_login;
    SignInButton google_login;
    CallbackManager callbackManager;
    RelativeLayout loading;
    ProgressView progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        setUpFacebook();
        setupGoogle();
    }

    private void init() {
        final EditText coupon_text = findViewById(R.id.coupon_edit);
        Button apply = findViewById(R.id.apply_button);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coupon_text.getText().toString().length() == 0){
                    Toast.makeText(SignUp.this, "Please enter a coupon code!", Toast.LENGTH_SHORT).show();
                }else{
                    verifyCouponCode(coupon_text.getText().toString());
                }
            }
        });
    }

    private void verifyCouponCode(final String text) {
        loading = findViewById(R.id.progress_rl);
        progress = findViewById(R.id.progress);
        progress.start();
        loading.setVisibility(View.VISIBLE);
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_verify_coupon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                progress.stop();
                if (status_code[0] == 200) {
                    if (response.contentEquals(text)){
                        Toast.makeText(SignUp.this, "Coupon code applied!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SignUp.this, "Please enter correct coupon code", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUp.this, "Couldn't verify ID. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                progress.stop();
                Toast.makeText(SignUp.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("coupon_code", text);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        VolleyHandling.getInstance().addToRequestQueue(request, "coupon_verify");
    }

    // setupGoogle()
    private void setupGoogle() {
        google_login = findViewById(R.id.google_login);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("6171481669-ppqo8knnml7qj2nl1auo8vvfaqecchek.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if (account != null){
//
//        }

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 200);
            }
        });
    }

    //setUpFacebook
    private void setUpFacebook() {

        fb_login = findViewById(R.id.fb_login);
        fb_login.setReadPermissions(Arrays.asList("public_profile", "email"));
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        fb_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignUp.this, "Request Cancelled? TRY AGAIN!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(SignUp.this, "ERR, Someth#$% We@# WRONG!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // FB Response Handling
    private void getUserProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response != null && response.getError() == null){
                            try {
                                String user_id;
                                if (response.getJSONObject().has("name")){
                                    Constants.user_name = response.getJSONObject().getString("name");
                                }else{
                                    Constants.user_name = "Anonymous User";
                                }
                                if (response.getJSONObject().has("email")) {
                                    user_id = response.getJSONObject().getString("email");
                                }
                                else {
                                    user_id = response.getJSONObject().getString("id");
                                }
                                submitData(user_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(SignUp.this, "Couldn't submit response", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_RESULT) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Constants.user_name = account.getDisplayName();
            submitData(account.getId());
        } catch (ApiException e) {
            Toast.makeText(this, "Google behaving weirdly!", Toast.LENGTH_SHORT).show();
        }
    }

    void submitData(final String user_id){
        loading = findViewById(R.id.progress_rl);
        progress = findViewById(R.id.progress);
        progress.start();
        loading.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0){
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                    final SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("signed_in", true);
                    editor.putString("user_id", response);
                    editor.putString("user_name", Constants.user_name);
                    editor.apply();
                    Constants.user_id = response;
                    loading.setVisibility(View.GONE);
                    progress.stop();
                    Intent intent = new Intent(SignUp.this, SplashScreen.class);
                    startActivity(intent);
                    finish();
                }else{
                    loading.setVisibility(View.GONE);
                    progress.stop();
                    Toast.makeText(SignUp.this, "Server is no longer speaking to you", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                progress.stop();
                Toast.makeText(SignUp.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("user_name", Constants.user_name);
                params.put("image", "Coming soon!");
                return params;
            }
        };

        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
