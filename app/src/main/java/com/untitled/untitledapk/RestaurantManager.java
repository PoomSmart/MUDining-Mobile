package com.untitled.untitledapk;

import android.content.Context;
import android.os.AsyncTask;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantsDatabase;

import java.util.List;

public class RestaurantManager {

    public static String[] foodTypes = {"Type1", "Type2", "Type3"};
    public static String[] categoryTypes = {"Category1", "Category2", "Category3"};
    private static RestaurantDao restaurantDao = null;
    private static List<Restaurant> cachedRestaurants = null;

    private static RestaurantDao getRestaurantDao(Context context) {
        if (restaurantDao == null) {
            RestaurantsDatabase restaurantDatabase = RestaurantsDatabase.getInstance(context);
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
        };
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
        };
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
        };
        if (cachedRestaurants != null) {
            cachedRestaurants.removeIf(restaurant1 -> restaurant1.getId().equals(restaurant.getId()));
            cachedRestaurants.add(restaurant);
        }
    }
}
