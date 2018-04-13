package com.untitled.untitledapk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImagesDatabase;
import com.untitled.untitledapk.persistence.RestaurantsDatabase;

public class MainActivity extends AppCompatActivity {

    private void populateRestaurants() {
        RestaurantsDatabase restaurantsDatabase = RestaurantsDatabase.getInstance(getApplicationContext());
        RestaurantDao restaurantDao = restaurantsDatabase.restaurantDao();
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Alpha", 13.1533, 105.2246, 3, 7, "Awesome Restaurant"));
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Beta", 13.1532, 105.2246, 1, 6, "Lovely Restaurant"));
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Gamma", 13.15315, 105.22405, 4, 4, "Wannabe Restaurant"));
    }

    private void populateRestaurantImages() {
        RestaurantImagesDatabase restaurantImagesDatabase = RestaurantImagesDatabase.getInstance(getApplicationContext());
        RestaurantImageDao restaurantImageDao = restaurantImagesDatabase.restaurantImageDao();
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(1, "1"));
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(2, "2"));
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(3, "3"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new PopulateDatabasesTask().execute();
    }

    private class PopulateDatabasesTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... voids) {
            populateRestaurants();
            populateRestaurantImages();
            return null;
        }
    }
}
