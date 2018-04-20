package com.untitled.untitledapk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;
    public int foodTypePref;
    public int categoryPref;
    public double currentLatitude;
    public double currentLongitude;
    ListView listView;
    private List<Restaurant> restaurants;
    LocationManager locationManager;
    Location currentLocation;
    final int REQUEST_FINE_LOCATION = 1234;

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null)
                continue;
            if (currentLocation == null || l.getAccuracy() < currentLocation.getAccuracy()) {
                currentLocation = l;
                currentLatitude = currentLocation.getLatitude();
                currentLongitude = currentLocation.getLongitude();
            }
        }

        // TODO: even more efficient calculation?
        // Sorting the restaurants with the distance
        restaurants.sort((r1, r2) -> {
            Double d1 = calculateDistanceFromCurrentLocation(r1);
            Double d2 = calculateDistanceFromCurrentLocation(r2);
            return d1.compareTo(d2);
        });

        restaurantListAdapter.setFoodTypes(foodTypePref);
        restaurantListAdapter.setCategoryTypes(categoryPref);
        restaurantListAdapter.getFilter().filter(null);

        restaurantListAdapter.notifyDataSetChanged();
    }

    public double calculateDistanceFromCurrentLocation(Restaurant r) {
        double latDist = r.getLatitude() - currentLatitude;
        double longDist = r.getLongitude() - currentLongitude;
        return Math.sqrt((latDist * latDist) + (longDist * longDist));
    }


}
