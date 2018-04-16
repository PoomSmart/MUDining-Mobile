/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.untitled.untitledapk.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Data Access Object for the restaurants table.
 */
@Dao
public interface RestaurantDao {

    /**
     * Return all restaurants.
     *
     * @return
     */
    @Query("SELECT * FROM Restaurants")
    List<Restaurant> getRestaurants();

    /**
     * Return a restaurant.
     *
     * @return
     */
    @Query("SELECT * FROM Restaurants LIMIT 1")
    Flowable<Restaurant> getRestaurant();

    /**
     * Return the restaurant of the given restaurant id.
     *
     * @return
     */
    @Query("SELECT * FROM Restaurants WHERE restaurantid = :restaurantId")
    Flowable<Restaurant> getRestaurant(Integer restaurantId);

    /**
     * Insert a restaurant in the database. If the restaurant already exists, replace it.
     *
     * @param restaurant the restaurant to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRestaurant(Restaurant restaurant);

    /**
     * Delete all restaurants.
     *
     */
    @Query("DELETE FROM Restaurants")
    void deleteAllRestaurants();

    /**
     * Delete the restaurant with the given id.
     *
     * @param restaurantId
     */
    @Query("DELETE FROM Restaurants WHERE restaurantid = :restaurantId")
    void deleteRestaurant(Integer restaurantId);
}
