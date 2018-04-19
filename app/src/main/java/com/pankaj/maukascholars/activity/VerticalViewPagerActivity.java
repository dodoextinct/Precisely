package com.pankaj.maukascholars.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.adapters.VerticalPagerAdapter;
import com.pankaj.maukascholars.application.VolleyHandling;
import com.pankaj.maukascholars.database.DBHandler;
import com.pankaj.maukascholars.holders.VerticalViewPager;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.CustomTabHelper;
import com.pankaj.maukascholars.util.EventDetails;
import com.pankaj.maukascholars.util.alarm.ScheduleAlarm;
import com.pankaj.maukascholars.util.alarm.databasehandling.DBManipulation;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class VerticalViewPagerActivity extends BaseNavigationActivity implements View.OnClickListener {

    RelativeLayout loading;
    ProgressView progress;
    VerticalViewPager verticalViewPager;
    BroadcastReceiver receiver = null;
    private List<EventDetails> mItems = new ArrayList<>();
    private int page = 0;
    VerticalPagerAdapter adapter;
    boolean updating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Constants.toolbar_title = " ";

        setContentView(R.layout.inshorts);
        loading = findViewById(R.id.progress_rl);
        progress = findViewById(R.id.progress);
        progress.start();
        loading.setVisibility(View.VISIBLE);
        getData();
    }



    private void init() {

        verticalViewPager = findViewById(R.id.verticleViewPager);
        adapter = new VerticalPagerAdapter(this, mItems);
        verticalViewPager.setAdapter(adapter);
        verticalViewPager.singleEventDetail = mItems.get(0);
        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                verticalViewPager.singleEventDetail = mItems.get(position);
                if (position + 3> mItems.size() && !updating) {
                    updating = true;
                    page++;
                    getData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ImageView refresh, share, save;
        Button send;
        share = findViewById(R.id.share);
//        star = findViewById(R.id.star);
        save = findViewById(R.id.save);
        send = findViewById(R.id.stalk);
        share.setOnClickListener(this);
//        star.setOnClickListener(this);
        save.setOnClickListener(this);
        send.setOnClickListener(this);

        loading.setVisibility(View.GONE);
        progress.stop();
        if (receiver == null) {
            IntentFilter filter = new IntentFilter("PreciselyReceiver");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    getData();
                }
            };
            registerReceiver(receiver, filter);
        }
    }

    void getData() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_event_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    updating = false;
                    try {
                        if (response.contains("[[\""))
                            response = response.substring(response.indexOf("[[\""));
                        JSONArray jA = new JSONArray(response);
                        //    0, 1, 2, 7, 12, 8, 13, 9, 3, 4, 5
                        for (int i = 0; i < jA.length(); i++) {
                            mItems.add(new EventDetails(Integer.parseInt(jA.getJSONArray(i).get(0).toString()), jA.getJSONArray(i).get(1).toString(), jA.getJSONArray(i).get(2).toString(), jA.getJSONArray(i).get(7).toString(), jA.getJSONArray(i).get(12).toString(), jA.getJSONArray(i).get(8).toString(), jA.getJSONArray(i).get(13).toString(), jA.getJSONArray(i).get(9).toString(), jA.getJSONArray(i).get(3).toString(), jA.getJSONArray(i).get(4).toString(), jA.getJSONArray(i).get(5).toString()));
                        }
                        if (page == 0)
                            init();
                        else{
                            adapter.cards = mItems;
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        loading.setVisibility(View.GONE);
                        progress.stop();
                        Toast.makeText(VerticalViewPagerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.setVisibility(View.GONE);
                    progress.stop();
                    Toast.makeText(VerticalViewPagerActivity.this, "Server is no longer speaking to you", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerticalViewPagerActivity.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                getData();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jO = new JSONArray();
                for (int i = 0; i < Constants.clickedFilters.size(); i++) {
                    jO.put(Constants.filters.get(Constants.clickedFilters.get(i)));
                }
                params.put("id", Constants.user_id);
                params.put("page", String.valueOf(page));
                params.put("tags", jO.toString());
                return params;
            }
        };

        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
    }

    @Override
    public void onClick(View view) {
        int position = verticalViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.share:
                share(position);
                break;
//            case R.id.star:
//                starEvent(position);
//                break;
            case R.id.save:
                saveEvent(position);
                break;
            case R.id.stalk:
                Log.e("TAG","SEEMORE");
                Intent intent = new Intent(this, CardOpen.class);
                intent.putExtra("event", verticalViewPager.singleEventDetail);
                startActivity(intent);
                break;

        }
    }

    private void open(int position) {
        String url = mItems.get(position).getLink();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        CustomTabHelper mCustomTabHelper = new CustomTabHelper();
        if (mCustomTabHelper.getPackageName(this).size() != 0) {
            CustomTabsIntent customTabsIntent =
                    new CustomTabsIntent.Builder()
                            .build();
            customTabsIntent.intent.setPackage(mCustomTabHelper.getPackageName(this).get(0));
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private void share(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Found a great opportunity for you!\n" + mItems.get(position).getLink() + "\nThere are many more where this came from!\nVisit https://goo.gl/1RGidK now!");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void starEvent(int position) {
        DBHandler db = new DBHandler(this);
        if (db.getEvent(mItems.get(position).getId()) != null && db.getEvent(mItems.get(position).getId()).getStarred() == 1) {
            Toast.makeText(this, "Already starred!", Toast.LENGTH_SHORT).show();
        } else {
            if (db.getEvent(mItems.get(position).getId()) != null)
                mItems.get(position).setSaved(1);
            mItems.get(position).setStarred(1);
            db.addEvent(mItems.get(position));
            Toast.makeText(this, "Event Starred!", Toast.LENGTH_SHORT).show();
            scheduleReminder(mItems.get(position).getDeadline(), mItems.get(position).getTitle() + "\n" + mItems.get(position).getLink());
        }
    }

    void scheduleReminder(String str_date, String title) {
        str_date += " 18:00:00";
        try {
            DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            Date date = formatter.parse(str_date);

            long alarmOffset = date.getTime();

            TimeZone.setDefault(TimeZone.getDefault());
            Date d = new Date();
            long current = d.getTime();
            long alarmdiff = alarmOffset - current;
            if (alarmdiff < 0) {
                Toast.makeText(getApplicationContext(), "Cannot set alarm for a past time.", Toast.LENGTH_SHORT).show();
                return;
            }

            DBManipulation databaseAdapter = new DBManipulation(getApplicationContext());
            databaseAdapter.open();

            databaseAdapter.insertEntry(title, alarmOffset);
            databaseAdapter.close();

            ScheduleAlarm obj = new ScheduleAlarm(getApplicationContext());
            obj.schedulealarm();
            Toast.makeText(getApplicationContext(), "Reminder scheduled successfully. " + str_date, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Unable to schedule alarms", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEvent(int position) {
        DBHandler db = new DBHandler(this);
        if (db.getEvent(mItems.get(position).getId()) != null && db.getEvent(mItems.get(position).getId()).getSaved() == 1) {
            Toast.makeText(this, "Already saved!", Toast.LENGTH_SHORT).show();
        } else {
            if (db.getEvent(mItems.get(position).getId()) != null)
                mItems.get(position).setStarred(1);
            mItems.get(position).setSaved(1);
            db.addEvent(mItems.get(position));
            Toast.makeText(this, "Event Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Filters.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("PreciselyReceiver");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getData();
            }
        };
        registerReceiver(receiver, filter);

    }

    public void ViewAppBarLay(){
        AppBarLayout appBarLayout;
        appBarLayout = findViewById(R.id.appBar_Layout);
        if (appBarLayout.getVisibility() == View.GONE)
        {
            appBarLayout.setVisibility(View.VISIBLE);
        }
        else if(appBarLayout.getVisibility() == View.VISIBLE)
        {
            appBarLayout.setVisibility(View.GONE);
        }
    }

    public void SwipeUpViewAppBar(){
        AppBarLayout appBarLayout;
        appBarLayout = findViewById(R.id.appBar_Layout);
        if (appBarLayout.getVisibility() == View.VISIBLE)
        {
            appBarLayout.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null)
        unregisterReceiver(receiver);
    }
}