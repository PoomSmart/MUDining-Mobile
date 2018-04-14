package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.untitled.untitledapk.persistence.Restaurant;

public class EditRestaurantActivity extends AppCompatActivity {

    private Button mSetLocationButton;
    private TextInputEditText mRestaurantNameField;
    private TextInputEditText mRestaurantDescriptionField;
    private LinearLayout mEditRestaurant;
    private LinearLayout mRestaurantTypes;
    private LinearLayout mCategoryTypes;
    private CheckBox[] mcbRestaurantTypes;
    private CheckBox[] mcbCategoryTypes;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        Context context = getApplicationContext();

        mSetLocationButton = findViewById(R.id.set_location_button);
        mRestaurantNameField = findViewById(R.id.restaurant_name_field);
        mRestaurantDescriptionField = findViewById(R.id.restaurant_description_field);
        mRestaurantTypes = findViewById(R.id.restaurant_types_layout);
        mCategoryTypes = findViewById(R.id.category_types_layout);
        mEditRestaurant = findViewById(R.id.edit_restaurant_layout);

        restaurant = (Restaurant)getIntent().getExtras().get("restaurant");

        addRestaurantImage(context);
        generateTypes(context);
        mSetLocationButton.setOnClickListener(v -> setLocation());

        mRestaurantNameField.setText(restaurant.getName());
        mRestaurantDescriptionField.setText(restaurant.getDescription());
    }

    @Override
    protected void onResume() {
        super.onResume();
        modifyLocation();
    }

    private void addRestaurantImage(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(RestaurantImageManager.getImage(context, restaurant.getId()));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        mEditRestaurant.addView(imageView, 0);
    }

    private void generateTypes(Context context){
        mcbRestaurantTypes = new CheckBox[RestaurantManager.restaurantTypes.length];
        int i = 0;
        for (String restaurantType : RestaurantManager.restaurantTypes) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(restaurantType);
            mRestaurantTypes.addView(mcbRestaurantTypes[i++] = checkBox);
        }
        mcbCategoryTypes = new CheckBox[RestaurantManager.categoryTypes.length];
        i = 0;
        for (String categoryType : RestaurantManager.categoryTypes) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(categoryType);
            mCategoryTypes.addView(mcbCategoryTypes[i++] = checkBox);
        }
    }

    private void modifyLocation() {
        mSetLocationButton.setText(String.format("Location: (%f, %f)", restaurant.getLatitude(), restaurant.getLongitude()));
    }

    private void setLocation() {
        Intent intent = new Intent(this, RestaurantLocationActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }
}
