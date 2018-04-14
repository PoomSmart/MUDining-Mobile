package com.untitled.untitledapk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

public class RecommendListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        listView = findViewById(R.id.list);
        List<Restaurant> restaurants = (List<Restaurant>) getIntent().getExtras().get("restaurants");
        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        listView.setAdapter(restaurantListAdapter);
    }
}
