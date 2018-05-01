package com.pankaj.maukascholars.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.adapters.languageAdapter;
import com.pankaj.maukascholars.application.PreciselyApplication;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.LanguageDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hitesh on 21/04/18.
 */

public class LanguageDialog extends Dialog{

    private List<LanguageDetails> language_Details_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private languageAdapter mAdapter;

    public LanguageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        prepareLanguageData();
    }

    private void prepareLanguageData() {
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_get_languages, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200) {
                    try {
                        if (response.contains("[{"))
                            response = response.substring(response.indexOf("[{"));

                        JSONArray languages_array = new JSONArray(response);
                        for (int i = 0; i < languages_array.length(); i++){
                            JSONObject jsonObject = languages_array.getJSONObject(i);
                            language_Details_list.add(new LanguageDetails(jsonObject.getString("language"), jsonObject.getString("id")));
                        }
                        init();
                    } catch (JSONException e) {
                        makeToast("Couldn't retrieve content. Please try again!");
                    }
                }else
                    makeToast("Please Try Again after sometime");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("Couldn't connect to server");
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

        PreciselyApplication.getInstance().addToRequestQueue(request, "languages");
    }

    private void init() {
        recyclerView = findViewById(R.id.languages_rv);

        mAdapter = new languageAdapter(language_Details_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Button proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreciselyApplication.getSharedPreferences();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.sp_language_id, Constants.language_id);
                editor.apply();
                makeToast("Language preference SAVED!");
                updateLanguage(Constants.language_id);
            }
        });
    }

    private void updateLanguage(final String language_id){
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_my_language, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200 && response.contentEquals("SUCCESS")) {
                    dismiss();
                }else
                    makeToast("Please Try Again after sometime");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("Couldn't connect to server");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id", Constants.user_id);
                params.put("language_id", language_id);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "languages");
    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}