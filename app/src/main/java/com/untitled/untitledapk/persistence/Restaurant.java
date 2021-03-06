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
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Immutable model class for a Restaurant
 */
@Entity(tableName = "restaurants")
public class Restaurant implements Serializable, Comparable<Restaurant> {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "restaurantid")
    private final String mId;

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

    @ColumnInfo(name = "description")
    private String mDescription;

    @Ignore
    public Restaurant() {
        this(null, null, null, 0, 0, null);
    }

    @Ignore
    public Restaurant(String mName, Double mLatitude, Double mLongitude, int mFoodTypes, int mCategoryTypes, String mDescription) {
        this(UUID.randomUUID().toString(), mName, mLatitude, mLongitude, mFoodTypes, mCategoryTypes, mDescription);
    }

    public Restaurant(String mId, String mName, Double mLatitude, Double mLongitude, int mFoodTypes, int mCategoryTypes, String mDescription) {
        this.mId = mId;
        this.mName = mName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mFoodTypes = mFoodTypes;
        this.mCategoryTypes = mCategoryTypes;
        this.mDescription = mDescription;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @Override
    public int compareTo(@NonNull Restaurant o) {
        return mName.compareTo(o.mName);
    }
}
