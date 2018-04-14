package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ViewRestaurantActivity extends AppCompatActivity {

    private Restaurant restaurant;

    private ImageView mRestaurantImageView;
    private TextView mRestaurantNameTextView;
    private TextView mRestaurantDescriptionTextView;
    private TextView mRestaurantFoodTypesTextView;
    private TextView mRestaurantCategoriesTextView;
    private TextView mRestaurantLocationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");
        if (restaurant != null) {
            mRestaurantImageView = findViewById(R.id.view_restaurant_image);
            mRestaurantNameTextView = findViewById(R.id.view_restaurant_name);
            mRestaurantDescriptionTextView = findViewById(R.id.view_restaurant_description);
            mRestaurantFoodTypesTextView = findViewById(R.id.view_restaurant_food_types);
            mRestaurantCategoriesTextView = findViewById(R.id.view_restaurant_categories);
            mRestaurantLocationTextView = findViewById(R.id.view_restaurant_location);

            Context context = getApplicationContext();
            mRestaurantImageView.setImageBitmap(RestaurantImageManager.getImage(context, restaurant.getId()));
            mRestaurantNameTextView.setText(restaurant.getName());
            mRestaurantDescriptionTextView.setText(restaurant.getDescription());
            List<String> foodTypes = new ArrayList<>();
            for (int i = 0; i < RestaurantManager.foodTypes.length; i++) {
                if ((restaurant.getFoodTypes() & (1 << i)) != 0)
                    foodTypes.add(RestaurantManager.foodTypes[i]);
            }
            mRestaurantFoodTypesTextView.setText(foodTypes.toString());
            List<String> categories = new ArrayList<>();
            for (int i = 0; i < RestaurantManager.categoryTypes.length; i++) {
                if ((restaurant.getCategoryTypes() & (1 << i)) != 0)
                    categories.add(RestaurantManager.categoryTypes[i]);
            }
            mRestaurantCategoriesTextView.setText(categories.toString());
            mRestaurantLocationTextView.setText(String.format("Location: (%f, %f)", restaurant.getLatitude(), restaurant.getLongitude()));
            mRestaurantLocationTextView.setOnClickListener(v -> showRestaurantLocation());
        } else {
            // No point proceeding with the null restaurant
            finish();
        }
    }

    private void showRestaurantLocation() {
        Intent intent = new Intent(this, RestaurantLocationActivity.class);
        intent.putExtra("editable", false);
        intent.putExtra("restaurant", restaurant);
        Location location = new Location("");
        location.setLatitude(restaurant.getLatitude());
        location.setLongitude(restaurant.getLongitude());
        intent.putExtra("location", location);
        startActivity(intent);
    }
}
