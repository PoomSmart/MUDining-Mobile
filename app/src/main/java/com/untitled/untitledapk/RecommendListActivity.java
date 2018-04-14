package com.untitled.untitledapk;

import android.app.ListActivity;
import android.os.Bundle;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

public class RecommendListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        List<Restaurant> restaurants = (List<Restaurant>) getIntent().getExtras().get("restaurants");
        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        this.setListAdapter(restaurantListAdapter);
    }
}
