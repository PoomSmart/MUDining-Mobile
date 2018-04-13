package com.untitled.untitledapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

public class SetPreferenceActivity extends AppCompatActivity {

    CheckBox cbFoodType1;
    CheckBox cbFoodType2;
    CheckBox cbFoodType3;
    CheckBox cbFoodType4;
    int foodTypePref = 0;

    CheckBox cbCategory1;
    CheckBox cbCategory2;
    CheckBox cbCategory3;
    CheckBox cbCategory4;
    int categoryPref = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preference);
        cbFoodType1 = findViewById(R.id.cbFoodType1);
        cbFoodType2 = findViewById(R.id.cbFoodType2);
        cbFoodType3 = findViewById(R.id.cbFoodType3);
        cbFoodType4 = findViewById(R.id.cbFoodType4);
        cbCategory1 = findViewById(R.id.cbCategory1);
        cbCategory2 = findViewById(R.id.cbCategory2);
        cbCategory3 = findViewById(R.id.cbCategory3);
        cbCategory4 = findViewById(R.id.cbCategory4);
        setPreferenceCheckBox();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
    }

    public void calculatePrefValue() {
        int temp = 0;
        if (cbFoodType1.isChecked()) temp += 1;
        if (cbFoodType2.isChecked()) temp += 2;
        if (cbFoodType3.isChecked()) temp += 4;
        if (cbFoodType4.isChecked()) temp += 8;
        foodTypePref = temp;

        temp = 0;
        if (cbCategory1.isChecked()) temp += 1;
        if (cbCategory2.isChecked()) temp += 2;
        if (cbCategory3.isChecked()) temp += 4;
        if (cbCategory4.isChecked()) temp += 8;
        categoryPref = temp;
    }

    public void savePreference() {
        SharedPreferences sharedPref = getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        calculatePrefValue();
        prefEditor.putInt("FoodTypes", foodTypePref);
        prefEditor.putInt("CategoryTypes", categoryPref);
        prefEditor.apply();
    }

    public void setPreferenceCheckBox() {
        SharedPreferences sharedPref = getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        foodTypePref = sharedPref.getInt("FoodTypes", 0);
        if (foodTypePref >= 8) {
            foodTypePref -= 8;
            cbFoodType4.setChecked(true);
        }
        if (foodTypePref >= 4) {
            foodTypePref -= 4;
            cbFoodType3.setChecked(true);
        }
        if (foodTypePref >= 2) {
            foodTypePref -= 2;
            cbFoodType2.setChecked(true);
        }
        if (foodTypePref >= 1) {
            foodTypePref -= 1;
            cbFoodType1.setChecked(true);
        }

        categoryPref = sharedPref.getInt("CategotyTypes", 0);
        if (categoryPref >= 8) {
            categoryPref -= 8;
            cbCategory4.setChecked(true);
        }
        if (categoryPref >= 4) {
            categoryPref -= 4;
            cbCategory3.setChecked(true);
        }
        if (categoryPref >= 2) {
            categoryPref -= 2;
            cbCategory2.setChecked(true);
        }
        if (categoryPref >= 1) {
            categoryPref -= 1;
            cbCategory1.setChecked(true);
        }
    }
}
