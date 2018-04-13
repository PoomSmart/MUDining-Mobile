package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.untitled.untitledapk.persistence.Restaurant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseWorker.work(getApplicationContext());

        // Intent test
        test();
    }

    private void test() {
        Intent testIntent = new Intent(this, EditRestaurantActivity.class);
        Context context = getApplicationContext();
        Integer restaurantId = 1;
        Restaurant restaurant = RestaurantManager.restaurantById(context, restaurantId);
        testIntent.putExtra("restaurantName", restaurant.getName());
        testIntent.putExtra("restaurantDescription", restaurant.getDescription());
        testIntent.putExtra("restaurantId", restaurantId);
        startActivity(testIntent);
    }
}
