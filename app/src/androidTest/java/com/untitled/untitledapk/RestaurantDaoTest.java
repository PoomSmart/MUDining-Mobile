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

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantsDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test the implementation of {@link RestaurantDao}
 */
@RunWith(AndroidJUnit4.class)
public class RestaurantDaoTest {

    private static final Restaurant RESTAURANT = new Restaurant("restaurantname");
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private RestaurantsDatabase mDatabase;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                RestaurantsDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getRestaurantsWhenNoRestaurantInserted() {
        mDatabase.restaurantDao().getRestaurant()
                .test()
                .assertNoValues();
    }

    @Test
    public void insertAndGetRestaurant() {
        // When inserting a new restaurant in the data source
        mDatabase.restaurantDao().insertRestaurant(RESTAURANT);

        // When subscribing to the emissions of the restaurant
        mDatabase.restaurantDao().getRestaurant()
                .test()
                // assertValue asserts that there was only one emission of the restaurant
                .assertValue(restaurant -> {
                    // The emitted restaurant is the expected one
                    return restaurant != null && restaurant.getId().equals(RESTAURANT.getId()) &&
                            restaurant.getName().equals(RESTAURANT.getName());
                });
    }

    @Test
    public void updateAndGetRestaurant() {
        // Given that we have a restaurant in the data source
        mDatabase.restaurantDao().insertRestaurant(RESTAURANT);

        // When we are updating the name of the restaurant
        Restaurant updatedRestaurant = new Restaurant("new restaurantname");
        mDatabase.restaurantDao().insertRestaurant(updatedRestaurant);

        // When subscribing to the emissions of the restaurant
        mDatabase.restaurantDao().getRestaurant()
                .test()
                // assertValue asserts that there was only one emission of the restaurant
                .assertValue(restaurant -> {
                    // The emitted restaurant is the expected one
                    return restaurant != null && restaurant.getId().equals(RESTAURANT.getId()) &&
                            restaurant.getName().equals("new restaurantname");
                });
    }

    @Test
    public void deleteAndGetRestaurant() {
        // Given that we have a restaurant in the data source
        mDatabase.restaurantDao().insertRestaurant(RESTAURANT);

        //When we are deleting all restaurants
        mDatabase.restaurantDao().deleteAllRestaurants();
        // When subscribing to the emissions of the restaurant
        mDatabase.restaurantDao().getRestaurant()
                .test()
                // check that there's no restaurant emitted
                .assertNoValues();
    }
}
