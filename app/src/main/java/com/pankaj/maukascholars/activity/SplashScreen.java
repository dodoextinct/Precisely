package com.pankaj.maukascholars.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.application.VolleyHandling;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains("signed_in")){
            setContentView(R.layout.activity_splash_screen);
            if (sp.contains("quote")){
                TextView tv = findViewById(R.id.quote);
                Constants.quote = sp.getString("quote", "If opportunities don't knock,\nBuild a door.").replace("\\", "");
                tv.setText(Constants.quote);
            }
            Constants.user_id = sp.getString("user_id", null);
            Constants.user_name = sp.getString("user_name", null);
            checkIfExists();
        }else {
            Intent intent = new Intent(SplashScreen.this, SignUp.class);
            startActivity(intent);
            finish();
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
                    }else{
                        Toast.makeText(SplashScreen.this, "Couldn't verify ID. Please login again", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove("signed_in");
                        editor.apply();
                        Intent intent = new Intent(SplashScreen.this, SignUp.class);
                        startActivity(intent);
                        finish();
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
                params.put("id", Constants.user_id);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
    }

    private void getNewQuote() {
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.GET, Constants.url_get_quote, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("quote", response);
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

        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
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
                        Log.e("Array 1", response);
                        String filter_text = response.substring(0, response.indexOf("]")+1);
                        String filter_image_url = response.substring(response.indexOf("]")+1);
                        Log.e("Array 1", filter_text);
                        Log.e("Array 1", filter_image_url);
                        Constants.filters.clear();
                        Constants.filters_image_urls.clear();
                        JSONArray jA_text = new JSONArray(filter_text);
                        JSONArray jA_url = new JSONArray(filter_image_url);
                        for (int i = 0; i < jA_text.length(); i++){
                            Constants.filters.add(jA_text.getString(i));
                            Constants.filters_image_urls.add(jA_url.getString(i));
                        }
//                        if (sp.contains(key)) {
//                            try {
//                                JSONArray jO = new JSONArray(sp.getString(key, ""));
//                                Constants.clickedFilters.clear();
//                                for (int i = 0; i < jO.length(); i++)
//                                    Constants.clickedFilters.add(jO.getInt(i));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Intent intent = new Intent(SplashScreen.this, VerticalViewPagerActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }else{
                       loadActivity(Filters.class);
//                        }
                    } catch (JSONException e) {
                        Toast.makeText(SplashScreen.this, "Couldn't retrieve content. Please try again!", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR CONTENT", e.toString());
                    }
                }else
                    Toast.makeText(SplashScreen.this, "Please Try Again after sometime", Toast.LENGTH_SHORT).show();
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

        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
    }

    private void loadActivity(Class activity){
        Utils.loadActivity(this, activity);
        finish();
    }
}