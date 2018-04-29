/**
 * RestaurantManager.java
 * Manage the reading and writing of restaurants from Restaurant database
 */

package com.untitled.untitledapk.database;

import android.content.Context;
import android.os.AsyncTask;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantDatabase;

import java.util.List;

public class RestaurantManager {

    public static String[] foodTypes = {"Thai", "Japanese", "Chinese", "European"};
    public static String[] categoryTypes = {"Single Dish", "Set Menu", "Buffet", "Other"};
    private static RestaurantDao restaurantDao = null;
    private static List<Restaurant> cachedRestaurants = null;

    private static RestaurantDao getRestaurantDao(Context context) {
        if (restaurantDao == null) {
            RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance(context);
            restaurantDao = restaurantDatabase.restaurantDao();
        }
        return restaurantDao;
    }

    public static void readRestaurants(Context context) {
        if (cachedRestaurants == null)
            cachedRestaurants = getRestaurantDao(context).getRestaurants();
    }

    public static List<Restaurant> getRestaurants() {
        return cachedRestaurants;
    }

    public static void deleteRestaurant(Context context, String restaurantId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getRestaurantDao(context).deleteRestaurant(restaurantId);
                RestaurantImageManager.removeImage(context, restaurantId);
                return null;
            }
        }.execute();
        if (cachedRestaurants != null)
            cachedRestaurants.removeIf(restaurant -> restaurant.getId().equals(restaurantId));
    }

    public static void insertRestaurant(Context context, Restaurant restaurant) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getRestaurantDao(context).insertRestaurant(restaurant);
                return null;
            }
        }.execute();
        if (cachedRestaurants != null) {
            cachedRestaurants.removeIf(restaurant1 -> restaurant1.getId().equals(restaurant.getId()));
            cachedRestaurants.add(restaurant);
        }
    }

    public static void updateRestaurant(Context context, Restaurant restaurant) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getRestaurantDao(context).updateRestaurant(restaurant);
                return null;
            }
        }.execute();
        if (cachedRestaurants != null) {
            cachedRestaurants.removeIf(restaurant1 -> restaurant1.getId().equals(restaurant.getId()));
            cachedRestaurants.add(restaurant);
        }
    }
}
