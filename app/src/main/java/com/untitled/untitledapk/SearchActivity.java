package com.untitled.untitledapk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = findViewById(R.id.liRestaurant);
        List<Restaurant> restaurants = RestaurantManager.getRestaurants(getApplicationContext());
        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        listView.setAdapter(restaurantListAdapter);
    }
}
