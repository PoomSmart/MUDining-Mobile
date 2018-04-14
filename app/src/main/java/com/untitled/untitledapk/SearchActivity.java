package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView restaurantList;
    private List<Restaurant> restaurants;
    private EditText textQuery;
    private RestaurantListAdapter restaurantListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        restaurantList = findViewById(R.id.liRestaurant);
        textQuery = findViewById(R.id.etSearch);

        // Need restaurants list as intent extras from previous activity
        restaurants = (List<Restaurant>) getIntent().getExtras().get("restaurants");
        restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        restaurantList.setAdapter(restaurantListAdapter);
        textQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchActivity.this).restaurantListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
