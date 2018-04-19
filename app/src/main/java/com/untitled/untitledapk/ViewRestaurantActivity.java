package com.untitled.untitledapk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ViewRestaurantActivity extends AppCompatActivity {

    ImageView mRestaurantImageView;
    TextView mRestaurantNameTextView;
    TextView mRestaurantDescriptionTextView;
    TextView mRestaurantFoodTypesTextView;
    TextView mRestaurantCategoriesTextView;
    TextView mRestaurantLocationTextView;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        Intent intent = getIntent();
        if (!intent.hasExtra("restaurant")) {
            // No point proceeding with the null restaurant
            finish();
        }
        restaurant = (Restaurant) intent.getExtras().get("restaurant");
        mRestaurantImageView = findViewById(R.id.view_restaurant_image);
        mRestaurantNameTextView = findViewById(R.id.view_restaurant_name);
        mRestaurantDescriptionTextView = findViewById(R.id.view_restaurant_description);
        mRestaurantFoodTypesTextView = findViewById(R.id.view_restaurant_food_types);
        mRestaurantCategoriesTextView = findViewById(R.id.view_restaurant_categories);
        mRestaurantLocationTextView = findViewById(R.id.view_restaurant_location);

        RestaurantImageManager.loadImage(this, restaurant.getId(), mRestaurantImageView);
        mRestaurantNameTextView.setText(restaurant.getName());
        mRestaurantDescriptionTextView.setText(restaurant.getDescription());
        List<String> foodTypes = new ArrayList<>();
        for (int i = 0; i < RestaurantManager.foodTypes.length; i++) {
            if ((restaurant.getFoodTypes() & (1 << i)) != 0)
                foodTypes.add(RestaurantManager.foodTypes[i]);
        }
        mRestaurantFoodTypesTextView.setText("Types: " + TextUtils.join(", ", foodTypes));
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < RestaurantManager.categoryTypes.length; i++) {
            if ((restaurant.getCategoryTypes() & (1 << i)) != 0)
                categories.add(RestaurantManager.categoryTypes[i]);
        }
        mRestaurantCategoriesTextView.setText("Categories: " + TextUtils.join(", ", categories));
        mRestaurantLocationTextView.setText(R.string.location_from_here_text);
        mRestaurantLocationTextView.setBackgroundColor(Color.GRAY);
        mRestaurantLocationTextView.setTextColor(Color.WHITE);
        mRestaurantLocationTextView.setOnClickListener(v -> showRestaurantLocation());
    }

    private void showRestaurantLocation() {
        Intent intent = new Intent(this, RestaurantLocationActivity.class);
        intent.putExtra("editable", false);
        intent.putExtra("route", true);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }
}
