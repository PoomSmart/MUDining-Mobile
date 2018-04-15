package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.untitled.untitledapk.persistence.Restaurant;

import java.io.Serializable;
import java.util.List;

import static android.support.v4.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            mDrawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    return true;
                case R.id.nav_preferences:
                    startActivity(new Intent(this, SetPreferenceActivity.class));
                    return true;
                default:
                    return true;
            }
        });

        DatabaseWorker.work(getApplicationContext());

        final Button button = findViewById(R.id.recommend_button);
        button.setOnClickListener(v -> recommendButtonClicked());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                mDrawerLayout.openDrawer(START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void recommendButtonClicked() {
        new ReadDatabasesTask().execute();
    }

    private class ReadDatabasesTask extends AsyncTask<Void, Void, Void> {
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
