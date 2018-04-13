package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseWorker.work(getApplicationContext());

        startActivity(new Intent(this, EditRestaurantActivity.class));
    }
}
