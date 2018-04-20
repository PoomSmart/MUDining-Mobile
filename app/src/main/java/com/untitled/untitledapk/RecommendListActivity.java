package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;
    ListView listView;
    private List<Restaurant> restaurants;

    private AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
        Restaurant restaurant = restaurants.get(position);
        Intent intent = new Intent(this, ViewRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.list);
        restaurants = RestaurantManager.getRestaurants();
        // TODO: retain only recommended restaurants
        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(this, restaurants, 0, 0);
        listView.setOnItemClickListener(listener);
        listView.setAdapter(restaurantListAdapter);
    }
}
