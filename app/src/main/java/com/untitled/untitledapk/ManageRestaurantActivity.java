package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageRestaurantActivity extends AppCompatActivity {

    public static final int EDIT_RESTAURANT_REQUEST = 4352;

    @BindView(R.id.toolbar)
    public Toolbar toolBar;

    ListView restaurantList;
    List<Restaurant> restaurants;
    RestaurantListAdapter restaurantListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_restaurant);

        restaurantList = findViewById(R.id.restaurantList);

        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        DrawerUtils.getDrawer(this, toolBar, R.string.nav_restaurants);
        getSupportActionBar().setTitle(R.string.nav_restaurants);

        restaurants = (List<Restaurant>) getIntent().getExtras().get("restaurants");
        restaurantListAdapter = new RestaurantListAdapter(this, restaurants, true);
        restaurantList.setAdapter(restaurantListAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_RESTAURANT_REQUEST && resultCode == RESULT_OK) {
            restaurantListAdapter.notifyDataSetChanged();
        }
    }
}
