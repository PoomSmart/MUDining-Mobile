package com.untitled.untitledapk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
    private Button mSaveRestaurantButton;
    private Button mDiscardRestaurantButton;
    private TextInputEditText mRestaurantNameField;
    private TextInputEditText mRestaurantDescriptionField;
    private LinearLayout mEditRestaurantLayout;
    private LinearLayout mFoodTypesLayout;
    private LinearLayout mCategoryTypesLayout;
    private CheckBox[] mcbFoodTypes;
    private CheckBox[] mcbCategoryTypes;

    private Restaurant restaurant;
    private Location updatedLocation;

    private boolean imageChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        Context context = getApplicationContext();

        mChangeImageButton = findViewById(R.id.change_image_button);
        mSetLocationButton = findViewById(R.id.set_location_button);
        mSaveRestaurantButton = findViewById(R.id.save_restaurant_button);
        mDiscardRestaurantButton = findViewById(R.id.discard_restaurant_button);
        mRestaurantImageLayout = findViewById(R.id.restaurant_image_layout);
        mRestaurantNameField = findViewById(R.id.restaurant_name_field);
        mRestaurantDescriptionField = findViewById(R.id.restaurant_description_field);
        mFoodTypesLayout = findViewById(R.id.food_types_layout);
        mCategoryTypesLayout = findViewById(R.id.category_types_layout);
        mEditRestaurantLayout = findViewById(R.id.edit_restaurant_layout);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        configRestaurantImage(context);
        generateTypes(context);
        mSetLocationButton.setOnClickListener(v -> setLocationButtonClicked());
        modifyLocation();

        mSaveRestaurantButton.setOnClickListener(v -> saveRestaurant());
        mDiscardRestaurantButton.setOnClickListener(v -> finish());

        mRestaurantNameField.setText(restaurant.getName());
        mRestaurantDescriptionField.setText(restaurant.getDescription());
    }

    private void saveRestaurant() {
        int foodTypes = 0;
        int categoryTypes = 0;
        for (int i = 0; i < mcbFoodTypes.length; i++) {
            if (mcbFoodTypes[i].isChecked())
                foodTypes |= 1 << i;
        }
        for (int i = 0; i < mcbCategoryTypes.length; i++) {
            if (mcbCategoryTypes[i].isChecked())
                categoryTypes |= 1 << i;
        }
        restaurant.setName(mRestaurantNameField.getText().toString());
        restaurant.setDescription(mRestaurantDescriptionField.getText().toString());
        restaurant.setFoodTypes(foodTypes);
        restaurant.setCategoryTypes(categoryTypes);
        if (updatedLocation != null) {
            restaurant.setLatitude(updatedLocation.getLatitude());
            restaurant.setLongitude(updatedLocation.getLongitude());
        }
        if (imageChanged)
            RestaurantImageManager.saveImage(getApplicationContext(), restaurant.getId(), ((BitmapDrawable)mRestaurantImageView.getDrawable()).getBitmap());
        RestaurantManager.insertRestaurant(getApplicationContext(), restaurant);
    }

    private void configRestaurantImage(Context context) {
        mRestaurantImageView = new ImageView(context);
        mRestaurantImageView.setImageBitmap(RestaurantImageManager.getImage(context, restaurant.getId()));
        mRestaurantImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mRestaurantImageView.setMaxHeight(600);
        mRestaurantImageView.setAdjustViewBounds(true);
        mChangeImageButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });
        mEditRestaurantLayout.addView(mRestaurantImageView, 0);
    }

    private void generateTypes(Context context) {
        mcbFoodTypes = new CheckBox[RestaurantManager.foodTypes.length];
        for (int i = 0; i < mcbFoodTypes.length; i++) {
            String foodType = RestaurantManager.foodTypes[i];
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(foodType);
            if ((restaurant.getFoodTypes() & (1 << i)) != 0)
                checkBox.setChecked(true);
            mFoodTypesLayout.addView(mcbFoodTypes[i] = checkBox);
        }
        mcbCategoryTypes = new CheckBox[RestaurantManager.categoryTypes.length];
        for (int i = 0; i < mcbCategoryTypes.length; i++) {
            String categoryType = RestaurantManager.categoryTypes[i];
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(categoryType);
            if ((restaurant.getCategoryTypes() & (1 << i)) != 0)
                checkBox.setChecked(true);
            mCategoryTypesLayout.addView(mcbCategoryTypes[i] = checkBox);
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
            updatedLocation = (Location) data.getExtras().get("restaurantLocation");
            mSetLocationButton.setText(String.format("Location: (%f, %f)", updatedLocation.getLatitude(), updatedLocation.getLongitude()));
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imageChanged = true;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mRestaurantImageView.setImageBitmap(photo);
        }
    }
}
