package com.pankaj.maukascholars.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.application.PreciselyApplication;
import com.rey.material.widget.Button;

/**
 * Created by pankaj on 30/4/18.
 */

public class ConnectWithAlexaDialog extends Dialog {

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
        final SharedPreferences sp = PreciselyApplication.getSharedPreferences();
        final EditText code_edit = findViewById(R.id.code_edittext);
        if (sp.contains("alexa_code"))
            code_edit.setHint(sp.getString("alexa_code", ""));
        Button proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = code_edit.getText().toString();
                if (code.length()>0){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("alexa_code", code).apply();
                }
                makeToast("Connected with Alexa!");
                dismiss();
            }
        });

    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}