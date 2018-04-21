package com.pankaj.maukascholars.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.Utils;

import java.util.logging.Filter;

/**
 * Created by pankaj on 28/3/18.
 */

@SuppressLint("Registered")
public class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    protected void onCreateDrawer() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.toolbar_title);

        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
    }

    @NonNull
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_opportunities_activity) {
            if (Constants.clickedFilters.size()>0) {
                Constants.count_nav_order = 1;
                loadActivity(VerticalViewPagerActivity.class);
                mDrawerLayout.closeDrawers();
            }else{
                Toast.makeText(this, "Please select at least one filter!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.nav_filters_activity) {
            Constants.count_nav_order = 0;
            loadActivity(Filters.class);
            mDrawerLayout.closeDrawers();
            return true;
        }else if (id == R.id.nav_saved_activity) {
            Constants.count_nav_order = 0;
            loadActivity(StarredActivity.class);
            mDrawerLayout.closeDrawers();
            return true;
        }  else if (id == R.id.nav_user_profile) {
            Constants.count_nav_order = 0;
            loadActivity(UserProfileActivity.class);
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_about) {
            Constants.count_nav_order = 0;
            loadActivity(About.class);
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_share) {
            share();
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_feedback) {
            openPlayStore();
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_logout) {
            Constants.count_nav_order = 0;
            logout();
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.website_url){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.precisely.co.in"));
            startActivity(browserIntent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.wayneventures.precisely")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wayneventures.precisely")));
        }
    }

    private void logout() {
        editor.clear();
        editor.apply();
        LoginManager.getInstance().logOut();
        loadActivity(SplashScreen.class);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (Constants.count_nav_order == 0){
                Constants.count_nav_order++;
                if (Constants.clickedFilters.size() > 0)
                    loadActivity(VerticalViewPagerActivity.class);
                else
                    finish();
            }else if (Constants.count_nav_order == 1){
                Constants.count_nav_order++;
                Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();
            }else{
                Constants.count_nav_order = 0;
                finish();
            }
        }
    }

    public void loadActivity(Class activity) {
        Utils.loadActivity(this, activity);
        finish();
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Download this cool opportunity curation app! \nIt is so much better than anything out there!\n\nhttps://goo.gl/59gpAa");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}