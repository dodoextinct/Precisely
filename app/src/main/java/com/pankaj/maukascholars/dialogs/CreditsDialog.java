package com.pankaj.maukascholars.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.pankaj.maukascholars.activity.UserProfileActivity;
import com.pankaj.maukascholars.application.PreciselyApplication;
import com.pankaj.maukascholars.util.Constants;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 30/4/18.
 */

public class CreditsDialog extends Dialog {
    private SharedPreferences sp;
    RelativeLayout loading;
    ProgressView progress;

    public CreditsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_credits_dialog);
        init();
    }

    private void init() {
        sp = PreciselyApplication.getSharedPreferences();
        final EditText code_edit = findViewById(R.id.code_edittext);
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
                    verifyCouponCode(code);
                }
            }
        });

    }

    private void verifyCouponCode(final String code) {
        loading = findViewById(R.id.progress_rl);
        progress = findViewById(R.id.progress);
        progress.start();
        loading.setVisibility(View.VISIBLE);
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_apply_coupon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                progress.stop();
                if (status_code[0] == 200) {
                    if (response.contentEquals(code)){
                        makeToast("Coupon code applied!");
                        if (getOwnerActivity() != null) {
                            ((UserProfileActivity)getOwnerActivity()).credits = code;
                        }
                        dismiss();
                    }else{
                        makeToast("Please enter correct coupon code");
                    }
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id", Constants.user_id);
                params.put("coupon_code", code);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "coupon_verify");
    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}