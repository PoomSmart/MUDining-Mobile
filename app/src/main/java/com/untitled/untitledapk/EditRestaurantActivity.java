package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class EditRestaurantActivity extends AppCompatActivity {

    private Button mSetLocationButton;
    private TextInputEditText mRestaurantNameField;
    private TextInputEditText mRestaurantDescriptionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        mSetLocationButton = findViewById(R.id.set_location_button);

        mSetLocationButton.setOnClickListener(v -> setLocation());

        mRestaurantNameField = findViewById(R.id.restaurant_name_field);
        mRestaurantDescriptionField = findViewById(R.id.restaurant_description_field);
        mRestaurantNameField.setText(getIntent().getStringExtra("restaurantName"));
        mRestaurantDescriptionField.setText(getIntent().getStringExtra("restaurantDescription"));

    }

    private void setLocation() {
        startActivity(new Intent(this, RestaurantLocationActivity.class));
    }
}
