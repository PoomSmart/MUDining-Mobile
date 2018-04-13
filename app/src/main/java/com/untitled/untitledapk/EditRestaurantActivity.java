package com.untitled.untitledapk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class EditRestaurantActivity extends AppCompatActivity {

    private Button mSetLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        mSetLocationButton = findViewById(R.id.set_location_button);

        mSetLocationButton.setOnClickListener(v -> setLocation());
    }

    private void setLocation() {
        startActivity(new Intent(this, RestaurantLocationActivity.class));
    }
}
