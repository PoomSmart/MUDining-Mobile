package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.untitled.untitledapk.persistence.Restaurant;

public class EditRestaurantActivity extends AppCompatActivity {

    private static final int SET_LOCATION_REQUEST_CODE = 34;
    private static final int CAMERA_REQUEST = 16;

    private ImageView mRestaurantImageView;
    private FrameLayout mRestaurantImageLayout;
    private Button mChangeImageButton;
    private Button mSetLocationButton;
    private TextInputEditText mRestaurantNameField;
    private TextInputEditText mRestaurantDescriptionField;
    private LinearLayout mEditRestaurantLayout;
    private LinearLayout mRestaurantTypesLayout;
    private LinearLayout mCategoryTypesLayout;
    private CheckBox[] mcbRestaurantTypes;
    private CheckBox[] mcbCategoryTypes;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        Context context = getApplicationContext();

        mChangeImageButton = findViewById(R.id.change_image_button);
        mSetLocationButton = findViewById(R.id.set_location_button);
        mRestaurantImageLayout = findViewById(R.id.restaurant_image_layout);
        mRestaurantNameField = findViewById(R.id.restaurant_name_field);
        mRestaurantDescriptionField = findViewById(R.id.restaurant_description_field);
        mRestaurantTypesLayout = findViewById(R.id.restaurant_types_layout);
        mCategoryTypesLayout = findViewById(R.id.category_types_layout);
        mEditRestaurantLayout = findViewById(R.id.edit_restaurant_layout);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        configRestaurantImage(context);
        generateTypes(context);
        mSetLocationButton.setOnClickListener(v -> setLocationButtonClicked());
        modifyLocation();

        mRestaurantNameField.setText(restaurant.getName());
        mRestaurantDescriptionField.setText(restaurant.getDescription());
    }

    private void configRestaurantImage(Context context) {
        mRestaurantImageView = new ImageView(context);
        mRestaurantImageView.setImageBitmap(RestaurantImageManager.getImage(context, restaurant.getId()));
        mRestaurantImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mRestaurantImageView.setAdjustViewBounds(true);
        mChangeImageButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });
        mEditRestaurantLayout.addView(mRestaurantImageView, 0);
    }

    private void generateTypes(Context context) {
        mcbRestaurantTypes = new CheckBox[RestaurantManager.restaurantTypes.length];
        int i = 0;
        for (String restaurantType : RestaurantManager.restaurantTypes) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(restaurantType);
            mRestaurantTypesLayout.addView(mcbRestaurantTypes[i++] = checkBox);
        }
        mcbCategoryTypes = new CheckBox[RestaurantManager.categoryTypes.length];
        i = 0;
        for (String categoryType : RestaurantManager.categoryTypes) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(categoryType);
            mCategoryTypesLayout.addView(mcbCategoryTypes[i++] = checkBox);
        }
    }

    private void modifyLocation() {
        mSetLocationButton.setText(String.format("Location: (%f, %f)", restaurant.getLatitude(), restaurant.getLongitude()));
    }

    private void setLocationButtonClicked() {
        Intent intent = new Intent(this, RestaurantLocationActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivityForResult(intent, SET_LOCATION_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            Location updatedLocation = (Location) data.getExtras().get("restaurantLocation");
            mSetLocationButton.setText(String.format("Location: (%f, %f)", updatedLocation.getLatitude(), updatedLocation.getLongitude()));
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mRestaurantImageView.setImageBitmap(photo);
        }
    }
}
