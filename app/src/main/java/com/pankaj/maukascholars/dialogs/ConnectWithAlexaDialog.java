package com.pankaj.maukascholars.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.activity.SignUp;
import com.pankaj.maukascholars.activity.SplashScreen;
import com.pankaj.maukascholars.application.PreciselyApplication;
import com.pankaj.maukascholars.util.Constants;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 30/4/18.
 */

public class ConnectWithAlexaDialog extends Dialog {
    private SharedPreferences sp;
    RelativeLayout loading;
    ProgressView progress;

    public ConnectWithAlexaDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alexa_dialog);
        init();
    }

    private void init() {
        sp = PreciselyApplication.getSharedPreferences();
        final EditText code_edit = findViewById(R.id.code_edittext);
        if (sp.contains("alexa_code"))
            code_edit.setHint(sp.getString("alexa_code", ""));
        Button proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = code_edit.getText().toString();
                if (code.length() > 0) {
                    loading = findViewById(R.id.progress_rl);
                    progress = findViewById(R.id.progress);
                    progress.start();
                    loading.setVisibility(View.VISIBLE);
                    verifyAlexa(code);
                }
            }
        });

    }

    private void verifyAlexa(final String code) {
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_verify_alexa_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                progress.stop();
                if (status_code[0] == 200) {
                    if (response.contains("SUCCESS")) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Constants.sp_alexacode, code).apply();
                        makeToast("Connected with Alexa!");
                        dismiss();
                    }
                    else
                        makeToast("Couldn't verify your Alexa code");
                }else{
                    makeToast("Couldn't verify ID. Please try again!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                progress.stop();
                makeToast("Couldn't connect to server");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("alexa_code", code);
                params.put("id", Constants.user_id);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "signin_alexa");
    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}