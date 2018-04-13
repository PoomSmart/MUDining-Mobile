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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * The Room database that contains the RestaurantImages table
 */
@Database(entities = {RestaurantImage.class}, version = 1)
public abstract class RestaurantImagesDatabase extends RoomDatabase {

    private static volatile RestaurantImagesDatabase INSTANCE;

    public abstract RestaurantImageDao restaurantImageDao();

    public static RestaurantImagesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantImagesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RestaurantImagesDatabase.class, "RestaurantImage.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
