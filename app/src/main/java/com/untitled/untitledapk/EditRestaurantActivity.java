package com.untitled.untitledapk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.untitled.untitledapk.database.RestaurantImageManager;
import com.untitled.untitledapk.database.RestaurantManager;
import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.utilities.TextValidator;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditRestaurantActivity extends AppCompatActivity {

    private static final int SET_LOCATION_REQUEST = 34;
    private static final int CAMERA_REQUEST = 16;
    private static final int REQUEST_IMAGE_GET = 1;

    @BindView(R.id.toolbar)
    public Toolbar toolBar;

    Button mChangeImageButton;
    Button mSetLocationButton;
    Button mSaveRestaurantButton;
    Button mDiscardRestaurantButton;
    private ImageView mRestaurantImageView;
    private TextInputEditText mRestaurantNameField;
    private TextInputEditText mRestaurantDescriptionField;
    private FrameLayout mRestaurantImageLayout;
    private LinearLayout mFoodTypesLayout;
    private LinearLayout mCategoryTypesLayout;
    private CheckBox[] mcbFoodTypes;
    private CheckBox[] mcbCategoryTypes;

    private Restaurant restaurant;
    private String oldRestaurantId;
    private Location updatedLocation;

    private boolean createNew;
    private boolean imageChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        Intent intent = getIntent();
        createNew = intent.getBooleanExtra("create", false);
        if (!intent.hasExtra("restaurant") && !createNew) {
            // Editing such null restaurant is impossible
            finish();
        }
        restaurant = createNew ? new Restaurant() : (Restaurant) intent.getExtras().get("restaurant");
        if (!createNew)
            oldRestaurantId = restaurant.getId();

        // Bind toolbar
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mChangeImageButton = findViewById(R.id.change_image_button);
        mSetLocationButton = findViewById(R.id.set_location_button);
        mSaveRestaurantButton = findViewById(R.id.save_restaurant_button);
        mDiscardRestaurantButton = findViewById(R.id.discard_restaurant_button);
        mRestaurantNameField = findViewById(R.id.restaurant_name_field);
        mRestaurantDescriptionField = findViewById(R.id.restaurant_description_field);
        mFoodTypesLayout = findViewById(R.id.food_types_layout);
        mCategoryTypesLayout = findViewById(R.id.category_types_layout);
        mRestaurantImageLayout = findViewById(R.id.restaurant_image_layout);

        configRestaurantImage();
        generateTypes();
        mSetLocationButton.setOnClickListener(v -> setLocationButtonClicked());
        modifyLocation();

        mSaveRestaurantButton.setOnClickListener(v -> saveRestaurant());
        mDiscardRestaurantButton.setOnClickListener(v -> cancelButtonClicked());

        if (!createNew) {
            mRestaurantNameField.setText(restaurant.getName());
            mRestaurantDescriptionField.setText(restaurant.getDescription());
        }
        mRestaurantNameField.addTextChangedListener(new TextValidator(mRestaurantNameField) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError("This input is required.");
                    mSaveRestaurantButton.setEnabled(false);
                } else {
                    mSaveRestaurantButton.setEnabled(true);
                }
            }
        });
    }

    private void cancelButtonClicked() {
        new AlertDialog.Builder(this).setTitle(android.R.string.dialog_alert_title).setMessage("Do you want to discard all the changes?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> finish())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void saveRestaurant() {
        if (mRestaurantImageView.getDrawable() == null) {
            new AlertDialog.Builder(this).setTitle(android.R.string.dialog_alert_title).setMessage("An image is required.").show();
            return;
        }
        String restaurantName = mRestaurantNameField.getText().toString();
        String restaurantDescription = mRestaurantDescriptionField.getText().toString();
        if (restaurantName.isEmpty() || restaurantDescription.isEmpty() || (createNew && updatedLocation == null)) {
            new AlertDialog.Builder(this).setTitle(android.R.string.dialog_alert_title).setMessage("Please provide complete details of the restaurant.").show();
            return;
        }
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
        restaurant.setName(restaurantName);
        restaurant.setDescription(restaurantDescription);
        restaurant.setFoodTypes(foodTypes);
        restaurant.setCategoryTypes(categoryTypes);
        if (updatedLocation != null) {
            restaurant.setLatitude(updatedLocation.getLatitude());
            restaurant.setLongitude(updatedLocation.getLongitude());
        }
        if (createNew)
            RestaurantManager.insertRestaurant(this, restaurant);
        else
            RestaurantManager.updateRestaurant(this, restaurant);
        if (imageChanged)
            RestaurantImageManager.saveImage(this, restaurant.getId(), ((BitmapDrawable) mRestaurantImageView.getDrawable()).getBitmap());
        Intent intent = new Intent();
        intent.putExtra("restaurant", restaurant);
        if (oldRestaurantId != null)
            intent.putExtra("oldRestaurantId", oldRestaurantId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void configRestaurantImage() {
        mRestaurantImageView = new ImageView(this);
        if (!createNew)
            RestaurantImageManager.loadImage(this, restaurant.getId(), mRestaurantImageView);
        mRestaurantImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mRestaurantImageView.setMinimumHeight(600);
        mRestaurantImageView.setMaxHeight(600);
        mRestaurantImageView.setAdjustViewBounds(true);
        mChangeImageButton.setOnClickListener(v -> {
            String[] options = {"Choose from gallery", "Take a photo"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, REQUEST_IMAGE_GET);
                } else if (which == 1) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });
        mRestaurantImageLayout.addView(mRestaurantImageView, 0);
    }

    private void generateTypes() {
        mcbFoodTypes = new CheckBox[RestaurantManager.foodTypes.length];
        for (int i = 0; i < mcbFoodTypes.length; i++) {
            String foodType = RestaurantManager.foodTypes[i];
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(foodType);
            if (!createNew && (restaurant.getFoodTypes() & (1 << i)) != 0)
                checkBox.setChecked(true);
            mFoodTypesLayout.addView(mcbFoodTypes[i] = checkBox);
        }
        mcbCategoryTypes = new CheckBox[RestaurantManager.categoryTypes.length];
        for (int i = 0; i < mcbCategoryTypes.length; i++) {
            String categoryType = RestaurantManager.categoryTypes[i];
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(categoryType);
            if (!createNew && (restaurant.getCategoryTypes() & (1 << i)) != 0)
                checkBox.setChecked(true);
            mCategoryTypesLayout.addView(mcbCategoryTypes[i] = checkBox);
        }
    }

    private void modifyLocation() {
        mSetLocationButton.setText(createNew ? "Set Location" : String.format("Location: (%f, %f)", restaurant.getLatitude(), restaurant.getLongitude()));
    }

    private void setLocationButtonClicked() {
        Intent intent = new Intent(this, RestaurantLocationActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivityForResult(intent, SET_LOCATION_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_LOCATION_REQUEST && resultCode == RESULT_OK) {
            // Update the location set by the map
            updatedLocation = (Location) data.getExtras().get("restaurantLocation");
            mSetLocationButton.setText(String.format("Location: (%f, %f)", updatedLocation.getLatitude(), updatedLocation.getLongitude()));
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Update the restaurant image taken with the camera
            imageChanged = true;
            mRestaurantImageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
        } else if (requestCode == REQUEST_IMAGE_GET  && resultCode == RESULT_OK) {
            // Update the restaurant image chosen from the gallery
            imageChanged = true;
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRestaurantImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
