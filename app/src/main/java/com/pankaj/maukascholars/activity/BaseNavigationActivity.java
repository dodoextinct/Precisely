package com.pankaj.maukascholars.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_filters_activity) {
            loadActivity(Filters.class);
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_saved_activity) {
            Constants.count_nav_order = 0;
            loadActivity(SavedActivity.class);
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_starred_activity) {
            Constants.count_nav_order = 0;
            loadActivity(StarredActivity.class);
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_about) {
            Constants.count_nav_order = 0;
            loadActivity(About.class);
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_share) {
            mDrawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_logout) {
            Constants.count_nav_order = 0;
            logout();
            mDrawerLayout.closeDrawers();
            return true;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                loadActivity(Filters.class);
            }else if (Constants.count_nav_order == 1){
                Constants.count_nav_order++;
                Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();
            }else{
                Constants.count_nav_order = 0;
                finish();
            }
        }
    }

    private void loadActivity(Class activity) {
        Utils.loadActivity(this, activity);
        finish();
    }
}
