package com.untitled.untitledapk;

import android.content.Context;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantsDatabase;

import io.reactivex.Flowable;

public class RestaurantManager {

    public static String[] foodTypes = {"Type1", "Type2", "Type3"};
    public static String[] categoryTypes = {"Category1", "Category2", "Category3"};
    private static RestaurantDao restaurantDao = null;

    private static RestaurantDao getRestaurantDao(Context context) {
        if (restaurantDao == null) {
            RestaurantsDatabase restaurantDatabase = RestaurantsDatabase.getInstance(context);
            return restaurantDao = restaurantDatabase.restaurantDao();
        }
        return restaurantDao;
    }

    public static Restaurant restaurantById(Context context, Integer restaurantId) {
        Flowable<Restaurant> restaurant = getRestaurantDao(context).getRestaurant(restaurantId);
        return restaurant.blockingFirst();
    }

    public static void deleteRestaurant(Context context, Integer restaurantId) {
        getRestaurantDao(context).deleteRestaurant(restaurantId);
        RestaurantImageManager.removeImage(context, restaurantId);
    }

    public static void insertRestaurant(Context context, Restaurant restaurant) {
        getRestaurantDao(context).insertRestaurant(restaurant);
    }

}
