/*
 * Copyright (C) 2012 The Android Open Source Project
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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.untitled.untitledapk.persistence.Restaurant;

public class RestaurantLocationActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean editable;
    boolean route;
    Button mSetLocationButton;
    Button mCancelButton;

    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    Location mDestinationLocation;
    FusedLocationProviderClient mFusedLocationClient;
    Restaurant restaurant;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mCurrentLocation = locationResult.getLastLocation();
            if (mDestinationLocation != null) {
                animateToLocation(mDestinationLocation);
                if (route) {
                    GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                            .from(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                            .to(new LatLng(mDestinationLocation.getLatitude(), mDestinationLocation.getLongitude()))
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    Toast.makeText(getApplicationContext(), "Route created successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Cannot create route", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else
                animateToLocation(mCurrentLocation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_location);
        Intent intent = getIntent();
        editable = intent.getBooleanExtra("editable", true);
        route = intent.getBooleanExtra("route", false);
        if (intent.hasExtra("restaurant")) {
            restaurant = (Restaurant) intent.getExtras().get("restaurant");
            mDestinationLocation = new Location("");
            mDestinationLocation.setLatitude(restaurant.getLatitude());
            mDestinationLocation.setLongitude(restaurant.getLongitude());
        }
        mSetLocationButton = findViewById(R.id.map_set_location_button);
        mCancelButton = findViewById(R.id.map_cancel_button);
        if (editable) {
            mSetLocationButton.setOnClickListener(v -> setCurrentLocation());
            mCancelButton.setOnClickListener(v -> finish());
        } else {
            mSetLocationButton.setVisibility(View.INVISIBLE);
            mCancelButton.setVisibility(View.INVISIBLE);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void animateToLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        mMap.clear();
        MarkerOptions options = new MarkerOptions().position(latLng);
        if (restaurant != null)
            options.title(restaurant.getName());
        mMap.addMarker(options);
        mMap.animateCamera(cameraUpdate);
    }

    private void setCurrentLocation() {
        if (mCurrentLocation != null) {
            Intent intent = new Intent();
            intent.putExtra("restaurantLocation", mCurrentLocation);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (editable)
            mMap.setOnMapClickListener(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        else if (mMap != null)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE)
            return;
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION))
            enableMyLocation();
        else
            mPermissionDenied = true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        if (mCurrentLocation != null) {
            mCurrentLocation.setLatitude(latLng.latitude);
            mCurrentLocation.setLongitude(latLng.longitude);
        }
    }
}
