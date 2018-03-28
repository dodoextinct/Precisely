package com.pankaj.maukascholars.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.adapters.StarredEventsAdapter;
import com.pankaj.maukascholars.database.DBHandler;

/**
 * Created by pankaj on 28/3/18.
 */

public class StarredActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DBHandler db = new DBHandler(this);
        if (db.getAllStarredEvents().size() == 0) {
            RelativeLayout empty_layout = findViewById(R.id.empty_layout);
            empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            final RecyclerView.Adapter adapter = new StarredEventsAdapter(db.getAllStarredEvents());
            recyclerView.setAdapter(adapter);
            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(recyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipeLeft(int position) {
                                    return true;
                                }

                                @Override
                                public boolean canSwipeRight(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        deleteData(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(StarredActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        deleteData(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(StarredActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                }
                            });

            recyclerView.addOnItemTouchListener(swipeTouchListener);
        }
    }

    void deleteData(final int position) {

    }
}