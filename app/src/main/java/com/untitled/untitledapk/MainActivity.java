package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.untitled.untitledapk.persistence.Restaurant;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mDrawerLayout = findViewById(R.id.sidebar_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.sidebar_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseWorker.work(getApplicationContext());

        final Button button = findViewById(R.id.recommendButton);
        button.setOnClickListener(v -> recommendButtonClicked());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.sidebar_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void recommendButtonClicked() {
        new ReadDatabaseTask().execute();
    }

    private class ReadDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, RecommendListActivity.class);
            List<Restaurant> restaurants = RestaurantManager.getRestaurants(context);
            // TODO: retain only recommended restaurants
            intent.putExtra("restaurants", (Serializable) restaurants);
            startActivity(intent);
            return null;
        }
    }
}
