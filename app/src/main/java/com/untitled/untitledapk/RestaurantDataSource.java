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

package com.untitled.untitledapk;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Access point for managing restaurant data.
 */
public interface RestaurantDataSource {

    /**
     * Gets restaurants from the data source.
     *
     * @return the restaurants from the data source.
     */
    List<Restaurant> getRestaurants();

    /**
     * Gets a restaurant from the data source.
     *
     * @return the restaurant from the data source.
     */
    Flowable<Restaurant> getRestaurant();

    /**
     * Inserts the restaurant into the data source, or, if this is an existing restaurant, updates it.
     *
     * @param restaurant the restaurant to be inserted or updated.
     */
    void insertOrUpdateRestaurant(Restaurant restaurant);

    /**
     * Deletes all restaurants from the data source.
     */
    void deleteAllRestaurants();
}