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
@Entity(tableName = "restaurants")
public class Restaurant {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "restaurantid")
    private Integer mId;

    @ColumnInfo(name = "restaurantname")
    private String mName;

    @ColumnInfo(name = "latitude")
    private Double mLatitude;

    @ColumnInfo(name = "longitude")
    private Double mLongitude;

    @ColumnInfo(name = "foodtypes")
    private int mFoodTypes;

    @ColumnInfo(name = "categorytypes")
    private int mCategoryTypes;

    public Restaurant(String mName) {
        this.mName = mName;
        this.mLatitude = null;
        this.mLongitude = null;
        this.mFoodTypes = 0;
        this.mCategoryTypes = 0;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(@NonNull Integer mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public int getFoodTypes() {
        return mFoodTypes;
    }

    public void setFoodTypes(int mFoodTypes) {
        this.mFoodTypes = mFoodTypes;
    }

    public int getCategoryTypes() {
        return mCategoryTypes;
    }

    public void setCategoryTypes(int mCategoryTypes) {
        this.mCategoryTypes = mCategoryTypes;
    }
}
