package com.untitled.untitledapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.untitled.untitledapk.database.RestaurantManager;

public class SetPreferenceFragment extends Fragment {

    private CheckBox[] cbFoodTypes;
    private int foodTypePref = 0;

    private CheckBox[] cbCategories;
    private int categoryPref = 0;

    public SetPreferenceFragment() {
    }

    public static SetPreferenceFragment newInstance() {
        return new SetPreferenceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_preference, container, false);
        populateCheckBoxList(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPreferenceCheckBox();
    }

    @Override
    public void onPause() {
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        calculatePrefValue();
        prefEditor.putInt("FoodTypes", foodTypePref);
        prefEditor.putInt("CategoryTypes", categoryPref);
        prefEditor.apply();
        Toast.makeText(getContext(), "Preferences saved!", Toast.LENGTH_SHORT).show();
    }

    public void setPreferenceCheckBox() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefStore", Context.MODE_PRIVATE);
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
    }

    public void populateCheckBoxList(View view) {
        Context context = getContext();
        LinearLayout foodTypesLayout = view.findViewById(R.id.set_pref_food_types_layout);
        LinearLayout categoriesLayout = view.findViewById(R.id.set_pref_categories_layout);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        foodTypePref = sharedPref.getInt("FoodTypes", 0);
        categoryPref = sharedPref.getInt("CategoryTypes", 0);

        float dpf = context.getResources().getDisplayMetrics().density;

        cbFoodTypes = new CheckBox[RestaurantManager.foodTypes.length];
        for (int i = 0; i < cbFoodTypes.length; i++) {
            String foodType = RestaurantManager.foodTypes[i];
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(foodType);
            checkBox.setHeight((int) (48 * dpf));
            if ((foodTypePref & (1 << i)) != 0)
                checkBox.setChecked(true);
            foodTypesLayout.addView(cbFoodTypes[i] = checkBox);
        }
        cbCategories = new CheckBox[RestaurantManager.categoryTypes.length];
        for (int i = 0; i < cbCategories.length; i++) {
            String categoryType = RestaurantManager.categoryTypes[i];
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(categoryType);
            checkBox.setHeight((int) (48 * dpf));
            if ((categoryPref & (1 << i)) != 0)
                checkBox.setChecked(true);
            categoriesLayout.addView(cbCategories[i] = checkBox);
        }
    }
}
