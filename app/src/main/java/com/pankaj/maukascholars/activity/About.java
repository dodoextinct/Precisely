package com.pankaj.maukascholars.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pankaj.maukascholars.R;

public class About extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar.setTitle("About Us");
    }
}
