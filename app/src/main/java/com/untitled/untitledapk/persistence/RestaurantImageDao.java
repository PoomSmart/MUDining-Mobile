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
 * Data Access Object for the restaurant images table.
 */
@Dao
public interface RestaurantImageDao {

    /**
     * Return all restaurant images.
     *
     * @return
     */
    @Query("SELECT * FROM RestaurantImages")
    List<RestaurantImage> getRestaurantImages();

    /**
     * Return the restaurant image of the given restaurant id.
     *
     * @return
     */
    @Query("SELECT * FROM RestaurantImages WHERE restaurantid = :restaurantId")
    Flowable<RestaurantImage> getRestaurantImage(Integer restaurantId);

    /**
     * Insert a restaurant image in the database. If the restaurant already exists, replace it.
     *
     * @param restaurantImage the restaurant to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurantImage(RestaurantImage restaurantImage);

    /**
     * Delete all restaurant images.
     *
     */
    @Query("DELETE FROM RestaurantImages")
    void deleteAllRestaurantImages();

    /**
     * Delete the restaurant image with the given restaurant id.
     *
     * @param restaurantId
     */
    @Query("DELETE FROM RestaurantImages WHERE restaurantid = :restaurantId")
    void deleteRestaurantImage(Integer restaurantId);
}
