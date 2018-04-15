package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.untitled.untitledapk.persistence.Restaurant;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        DrawerUtils.getDrawer(this, toolBar);

        DatabaseWorker.work(getApplicationContext());

        final Button button = findViewById(R.id.recommend_button);
        button.setOnClickListener(v -> recommendButtonClicked());
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
