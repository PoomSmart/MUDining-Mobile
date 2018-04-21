package com.untitled.untitledapk;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.untitled.untitledapk.database.RestaurantManager;
import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

public class SearchFragment extends Fragment {

    ListView restaurantList;
    List<Restaurant> restaurants;
    private RestaurantListAdapter restaurantListAdapter;

    private CheckBox[] cbFoodTypes;
    private int foodTypePref = 0;

    private CheckBox[] cbCategories;
    private int categoryPref = 0;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        restaurantList = view.findViewById(R.id.restaurantList);
        restaurants = RestaurantManager.getRestaurants();
        restaurantListAdapter = new RestaurantListAdapter(getActivity(), restaurants, foodTypePref, categoryPref);
        restaurantList.setAdapter(restaurantListAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        restaurantListAdapter.getFilter().filter(newText);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                View v = inflater.inflate(R.layout.fragment_set_preference, null);
                builder.setView(v)
                        .setTitle(R.string.filter_text)
                        // Set the action buttons
                        .setPositiveButton(R.string.confirm_text, (dialog, id) -> calculatePrefValue())
                        .setNegativeButton(android.R.string.cancel, null)
                        .setNeutralButton(R.string.clear_text, null);
                // Add checkboxes to the dialog
                populateCheckBoxDialog(v);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
                    for (CheckBox foodType : cbFoodTypes) {
                        foodType.setChecked(false);
                    }
                    for (CheckBox category : cbCategories) {
                        category.setChecked(false);
                    }
                });
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
        restaurantListAdapter.setFoodTypes(foodTypePref);
        restaurantListAdapter.setCategoryTypes(categoryPref);
        restaurantListAdapter.getFilter().filter(null);
        restaurantListAdapter.notifyDataSetChanged();
    }

    public void populateCheckBoxDialog(View v) {
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        LinearLayout foodTypesLayout = v.findViewById(R.id.set_pref_food_types_layout);
        LinearLayout categoriesLayout = v.findViewById(R.id.set_pref_categories_layout);
        Context context = getActivity();

        float dpf = getResources().getDisplayMetrics().density;

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
