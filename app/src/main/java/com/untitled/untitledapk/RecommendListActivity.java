package com.untitled.untitledapk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;
    public int foodTypePref;
    public int categoryPref;
    public double currentLatitude;
    public double currentLongtitude;
    ListView listView;
    private List<Restaurant> restaurants;
    private LocationManager locationManager;
    private Location currentLocation;
    private final int REQUEST_FINE_LOCATION = 1234;

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
        SharedPreferences sharedPref = this.getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        foodTypePref = sharedPref.getInt("FoodTypes", 0);
        categoryPref = sharedPref.getInt("CategoryTypes", 0);
        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(this, restaurants, foodTypePref, categoryPref);
        listView.setOnItemClickListener(listener);
        listView.setAdapter(restaurantListAdapter);

        // Get current location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLatitude = currentLocation.getLatitude();
        currentLongtitude = currentLocation.getLongitude();

        // TODO: more efficient calculation
        // Sorting the restaurants with the distance
        for (int i = 0; i < restaurants.size(); i++) {
            for (int j = 0; j < restaurants.size() - (i + 1); j++) {
                if (calculateDistanceFromCurrentLocation(restaurants.get(j)) > calculateDistanceFromCurrentLocation(restaurants.get(j+1))) {
                    restaurants.add(j+1,restaurants.remove(j));
                }
            }
        }

        restaurantListAdapter.setFoodTypes(foodTypePref);
        restaurantListAdapter.setCategoryTypes(categoryPref);
        restaurantListAdapter.getFilter().filter(null);

        restaurantListAdapter.notifyDataSetChanged();
    }

    public double calculateDistanceFromCurrentLocation(Restaurant r) {
        double latDist = r.getLatitude() - currentLatitude;
        double longDist = r.getLongitude() - currentLongtitude;
        return Math.sqrt((latDist * latDist) + (longDist * longDist) );
    }


}
