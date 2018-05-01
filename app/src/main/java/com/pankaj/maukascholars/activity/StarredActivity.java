package com.pankaj.maukascholars.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.adapters.StarredEventsAdapter;
import com.pankaj.maukascholars.database.DBHandler;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.EventDetails;

import java.util.List;

/**
 * Created by pankaj on 28/3/18.
 */

public class StarredActivity extends BaseNavigationActivity {

    List<EventDetails> mItems;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.toolbar_title = "Saved Opportunities";
        setContentView(R.layout.activity_profile);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DBHandler db = new DBHandler(this);

        mItems = db.getAllStarredEvents();
        if (mItems.size() == 0) {
            RelativeLayout empty_layout = findViewById(R.id.empty_layout);
            empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            final RecyclerView.Adapter adapter = new StarredEventsAdapter(mItems, this);
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
        DBHandler db = new DBHandler(this);
        db.deleteEvent(mItems.get(position).getId());
        mItems.remove(position);
    }
}