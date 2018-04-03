package com.pankaj.maukascholars.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.util.Constants;

public class About extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.toolbar_title = "About Us";
        setContentView(R.layout.activity_about);
        TextView gotowebsite = findViewById(R.id.button_website);
        gotowebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.precisely.co.in"));
                startActivity(browserIntent);
            }
        });
    }
}
