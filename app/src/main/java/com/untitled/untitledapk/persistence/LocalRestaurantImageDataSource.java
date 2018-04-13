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

import com.untitled.untitledapk.RestaurantImageDataSource;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Using the Room database as a data source.
 */
public class LocalRestaurantImageDataSource implements RestaurantImageDataSource {

    private final RestaurantImageDao mRestaurantImageDao;

    public LocalRestaurantImageDataSource(RestaurantImageDao restaurantImageDao) {
        mRestaurantImageDao = restaurantImageDao;
    }

    @Override
    public List<RestaurantImage> getRestaurantImages() {
        return mRestaurantImageDao.getRestaurantImages();
    }

    @Override
    public Flowable<RestaurantImage> getRestaurantImage(Integer restaurantId) {
        return mRestaurantImageDao.getRestaurantImage(restaurantId);
    }

    @Override
    public void insertOrUpdateRestaurantImage(RestaurantImage restaurantImage) {
        mRestaurantImageDao.insertRestaurantImage(restaurantImage);
    }

    @Override
    public void deleteAllRestaurantImages() {
        mRestaurantImageDao.deleteAllRestaurantImages();
    }
}
