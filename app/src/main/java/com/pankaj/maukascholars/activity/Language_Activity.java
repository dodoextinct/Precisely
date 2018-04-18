package com.pankaj.maukascholars.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.adapters.languageAdapter;
import com.pankaj.maukascholars.util.Language;

import java.util.ArrayList;
import java.util.List;

public class Language_Activity extends AppCompatActivity {
    private List<Language> LanguageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private languageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);


        recyclerView = findViewById(R.id.languages_rv);


        mAdapter = new languageAdapter(LanguageList) {
        };
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareLanguageData();


    }

    private void prepareLanguageData() {
        Language Language = new Language("English");
        LanguageList.add(Language);

        Language = new Language("Bangla");
        LanguageList.add(Language);

        Language = new Language("Burmese");
        LanguageList.add(Language);

        Language = new Language("Chinese (Simplified)");
        LanguageList.add(Language);

        Language = new Language("Filipino");
        LanguageList.add(Language);

        Language = new Language("Hindi");
        LanguageList.add(Language);

        Language = new Language("Indonesian");
        LanguageList.add(Language);

        Language = new Language("Khmer");
        LanguageList.add(Language);

        Language = new Language("Lao");
        LanguageList.add(Language);

        Language = new Language("Malay");
        LanguageList.add(Language);

        Language = new Language("Nepali");
        LanguageList.add(Language);

        Language = new Language("Pashto");
        LanguageList.add(Language);

        Language = new Language("Persian");
        LanguageList.add(Language);

        Language = new Language("Sinhala");
        LanguageList.add(Language);

        Language = new Language("Tamil");
        LanguageList.add(Language);

        Language = new Language("Thai");
        LanguageList.add(Language);

        Language = new Language("Urdu");
        LanguageList.add(Language);

        Language = new Language("Vietnamese");
        LanguageList.add(Language);



        mAdapter.notifyDataSetChanged();
    }
}