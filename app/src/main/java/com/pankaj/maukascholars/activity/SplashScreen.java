package com.pankaj.maukascholars.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.application.PreciselyApplication;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.pankaj.maukascholars.util.Constants.key;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreciselyApplication.getSharedPreferences();
        if (sp.contains("signed_in")){
            if (!sp.contains("isFirstTime") || sp.getBoolean("isFirstTime", true)){
                loadActivity(TutorialsActivity.class);
            }else {
                setContentView(R.layout.activity_splash_screen);
                if (sp.contains("quote")) {
                    TextView tv = findViewById(R.id.quote);
                    Constants.quote = sp.getString("quote", "If opportunities don't knock,\nBuild a door.").replace("\\", "");
                    tv.setText(Constants.quote);
                }
                Constants.user_id = sp.getString("user_id", null);
                Constants.user_name = sp.getString("user_name", null);
                checkIfExists();
            }
        }else {
            loadActivity(SignUp.class);
        }
    }

    private void checkIfExists() {
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_verify_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200) {
                    if (response.contentEquals(Constants.user_id)){
                        getNewQuote();
                    }else if(response.contains("UPDATE")){
                        new AlertDialog.Builder(SplashScreen.this)
                                .setTitle("Incompatible Version \n")
                                .setMessage("Your version of the app is too old.\n\nPlease update the app from playstore")
                                .setNeutralButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.wayneventures.precisely")));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wayneventures.precisely")));
                                            }
                                    }
                                })
                                .show();
                    }
                    else{
                        Toast.makeText(SplashScreen.this, "Couldn't verify ID. Please login again", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove("signed_in");
                        editor.apply();
                        loadActivity(SignUp.class);
                    }
                }else{
                    Toast.makeText(SplashScreen.this, "Couldn't verify ID. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashScreen.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                int version = 0;
                try {
                    PackageInfo pInfo = null;
                    pInfo = SplashScreen.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                params.put("versionCode", String.valueOf(version));
                params.put("id", Constants.user_id);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "signin");
    }

    private void getNewQuote() {
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.GET, Constants.url_get_quote, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constants.sp_quote, response);
                    editor.apply();
                    getFilters();
                }else{
                    Toast.makeText(SplashScreen.this, "Didn't get correct response!'", Toast.LENGTH_SHORT).show();
                    getFilters();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashScreen.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "signin");
    }

    private void getFilters() {
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.GET, Constants.url_get_filters, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200) {
                    try {
                        if (response.contains("[\""))
                            response = response.substring(response.indexOf("[\""));
                        String filter_text = response.substring(0, response.indexOf("]") + 1);
                        String filter_image_url = response.substring(response.indexOf("]") + 1);
                        Constants.filters.clear();
                        Constants.filters_image_urls.clear();
                        JSONArray jA_text = new JSONArray(filter_text);
                        JSONArray jA_url = new JSONArray(filter_image_url);
                        for (int i = 0; i < jA_text.length(); i++) {
                            Constants.filters.add(jA_text.getString(i));
                            Constants.filters_image_urls.add(jA_url.getString(i));
                        }
                        if (!sp.contains("language_id")) {
                            loadActivity(LanguageActivity.class);
                        } else {
                            if (sp.contains(key)) {
                                try {
                                    JSONArray jO = new JSONArray(sp.getString(key, ""));
                                    Constants.clickedFilters.clear();
                                    for (int i = 0; i < jO.length(); i++)
                                        Constants.clickedFilters.add(jO.getInt(i));
//                                    Log.e("KEY", Constants.clickedFilters.size()+"");
                                    loadActivity(VerticalViewPagerActivity.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                loadActivity(Filters.class);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(SplashScreen.this, "Couldn't retrieve content. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashScreen.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "filters");
    }

    private void loadActivity(Class activity){
        Utils.loadActivity(this, activity);
        finish();
    }
}