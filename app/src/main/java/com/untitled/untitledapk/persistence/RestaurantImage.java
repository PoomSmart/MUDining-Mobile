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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Immutable model class for a Restaurant
 */
@Entity(tableName = "restaurantimages")
public class RestaurantImage {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "restaurantid")
    private Integer mRestaurantId;

    @ColumnInfo(name = "imageid")
    private Integer mImageId;

    public RestaurantImage(Integer mRestaurantId, Integer mImageId) {
        this.mRestaurantId = mRestaurantId;
        this.mImageId = mImageId;
    }

    public Integer getRestaurantId() {
        return mRestaurantId;
    }

    public void setRestaurantId(@NonNull Integer mRestaurantId) {
        this.mRestaurantId = mRestaurantId;
    }

    public Integer getImageId() {
        return mImageId;
    }

    public void setImageId(Integer mImageId) {
        this.mImageId = mImageId;
    }

}
