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
import com.pankaj.maukascholars.util.Constants;
import com.rey.material.widget.Button;

/**
 * Created by pankaj on 23/4/18.
 */

public class ChangeEmailDialog extends Dialog {

    public ChangeEmailDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_email_change_dialog);
        init();
    }

    private void init() {
        final SharedPreferences sp = PreciselyApplication.getSharedPreferences();
        final EditText email_edit = findViewById(R.id.email_edittext);
        if (sp.contains("user_email"))
            email_edit.setHint(sp.getString("user_email", ""));
        Button proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_edit.getText().toString();
                if (email.length()>0 && email.contains(".com") && email.contains("@")){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constants.sp_user_email, email).apply();
                }
                makeToast("E-mail address saved!");
                dismiss();

            }
        });

    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}