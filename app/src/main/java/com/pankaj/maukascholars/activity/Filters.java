package com.pankaj.maukascholars.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.adapters.FiltersAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import static com.pankaj.maukascholars.util.Constants.clickedFilters;
import static com.pankaj.maukascholars.util.Constants.filters;
import static com.pankaj.maukascholars.util.Constants.filters_image_urls;
import static com.pankaj.maukascholars.util.Constants.key;

public class Filters extends BaseNavigationActivity {

    ImageView proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        setupFilters();
        proceed = findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedFilters.size() > 0) {
                    Log.e("FILTER SELECTED SIZE", clickedFilters.size() + "");
                    loadActivity(VerticalViewPagerActivity.class);
                } else
                    Toast.makeText(Filters.this, "Please select at least one filter!", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new FiltersAdapter(this, filters, filters_image_urls);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFilters();
    }

    private void setupFilters() {
        if (sp.contains(key)) {
            try {
                JSONArray jO = new JSONArray(sp.getString(key, ""));
                Log.e("ClickedFilter", jO.toString());
                clickedFilters.clear();
                for (int i = 0; i < jO.length(); i++)
                    clickedFilters.add(jO.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveClickedToSharedPreferences();
    }

    private void saveClickedToSharedPreferences() {
        JSONArray jO = new JSONArray();
        Log.e("SaveClickedFilter", jO.toString());
        for (int i = 0; i < clickedFilters.size(); i++)
            jO.put(clickedFilters.get(i));
        editor.remove(key).apply();
        editor.putString(key, jO.toString());
        editor.apply();
    }
}
