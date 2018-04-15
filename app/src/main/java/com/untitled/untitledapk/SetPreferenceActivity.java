package com.untitled.untitledapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SetPreferenceActivity extends AppCompatActivity {

    private CheckBox[] cbFoodTypes;
    private int foodTypePref = 0;

    private CheckBox[] cbCategories;
    private int categoryPref = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preference);

        LinearLayout foodTypesLayout = findViewById(R.id.set_pref_food_types_layout);
        LinearLayout categoriesLayout = findViewById(R.id.set_pref_categories_layout);

        SharedPreferences sharedPref = getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        foodTypePref = sharedPref.getInt("FoodTypes", 0);
        categoryPref = sharedPref.getInt("CategoryTypes", 0);

        float dpf = getResources().getDisplayMetrics().density;

        cbFoodTypes = new CheckBox[RestaurantManager.foodTypes.length];
        for (int i = 0; i < cbFoodTypes.length; i++) {
            String foodType = RestaurantManager.foodTypes[i];
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(foodType);
            checkBox.setHeight((int) (48 * dpf));
            if ((foodTypePref & (1 << i)) != 0)
                checkBox.setChecked(true);
            foodTypesLayout.addView(cbFoodTypes[i] = checkBox);
        }
        cbCategories = new CheckBox[RestaurantManager.categoryTypes.length];
        for (int i = 0; i < cbCategories.length; i++) {
            String categoryType = RestaurantManager.categoryTypes[i];
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(categoryType);
            checkBox.setHeight((int) (48 * dpf));
            if ((categoryPref & (1 << i)) != 0)
                checkBox.setChecked(true);
            categoriesLayout.addView(cbCategories[i] = checkBox);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPreferenceCheckBox();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
    }

    public void calculatePrefValue() {
        foodTypePref = 0;
        categoryPref = 0;
        for (int i = 0; i < cbFoodTypes.length; i++) {
            if (cbFoodTypes[i].isChecked())
                foodTypePref |= 1 << i;
        }
        for (int i = 0; i < cbCategories.length; i++) {
            if (cbCategories[i].isChecked())
                categoryPref |= 1 << i;
        }
    }

    public void savePreference() {
        SharedPreferences sharedPref = getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        calculatePrefValue();
        prefEditor.putInt("FoodTypes", foodTypePref);
        prefEditor.putInt("CategoryTypes", categoryPref);
        prefEditor.apply();
        Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
    }

    public void setPreferenceCheckBox() {
        SharedPreferences sharedPref = getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        foodTypePref = sharedPref.getInt("FoodTypes", 0);
        for (int i = 0; i < cbFoodTypes.length; i++) {
            CheckBox checkBox = cbFoodTypes[i];
            if ((foodTypePref & (1 << i)) != 0)
                checkBox.setChecked(true);
        }
        categoryPref = sharedPref.getInt("CategoryTypes", 0);
        for (int i = 0; i < cbCategories.length; i++) {
            CheckBox checkBox = cbCategories[i];
            if ((categoryPref & (1 << i)) != 0)
                checkBox.setChecked(true);
        }
        Toast.makeText(this, "Preferences loaded!", Toast.LENGTH_SHORT).show();
    }
}
