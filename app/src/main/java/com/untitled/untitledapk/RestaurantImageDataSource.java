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
import com.untitled.untitledapk.persistence.RestaurantImage;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Access point for managing restaurant image data.
 */
public interface RestaurantImageDataSource {

    /**
     * Gets restaurant images from the data source.
     *
     * @return the restaurant images from the data source.
     */
    List<RestaurantImage> getRestaurantImages();

    /**
     * Gets the restaurant image with the specified id from the data source.
     *
     * @return the restaurant image from the data source.
     */
    Flowable<RestaurantImage> getRestaurantImage(Integer restaurantId);

    /**
     * Inserts the restaurant image into the data source, or, if this is an existing restaurant image, updates it.
     *
     * @param restaurantImage the restaurant image to be inserted or updated.
     */
    void insertOrUpdateRestaurantImage(RestaurantImage restaurantImage);

    /**
     * Deletes all restaurant images from the data source.
     */
    void deleteAllRestaurantImages();
}