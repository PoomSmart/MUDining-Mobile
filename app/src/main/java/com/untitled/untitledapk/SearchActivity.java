package com.untitled.untitledapk;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;

    private ListView restaurantList;
    private List<Restaurant> restaurants;
    private EditText textQuery;
    private RestaurantListAdapter restaurantListAdapter;
    private String[] prefs;

    private CheckBox[] cbFoodTypes;
    private int foodTypePref = 0;

    private CheckBox[] cbCategories;
    private int categoryPref = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        restaurantList = findViewById(R.id.liRestaurant);

        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        DrawerUtils.getDrawer(this, toolBar);

        // Need restaurants list as intent extras from previous activity
        restaurants = (List<Restaurant>) getIntent().getExtras().get("restaurants");
        restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        restaurantList.setAdapter(restaurantListAdapter);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange (String newText) {
                        (SearchActivity.this).restaurantListAdapter.getFilter().filter(newText);
                        return false;
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Dialog
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View v = inflater.inflate(R.layout.activity_set_preference, null);
                builder.setView(v)
                        .setTitle("Filter")
                // 2. Chain together various setter methods to set the dialog characteristics
                        // Set the action buttons
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                // 3. Get the AlertDialog from create()
                populateCheckBoxDialog(v);
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void populateCheckBoxDialog(View v) {
        Context context = getApplicationContext();
        LinearLayout foodTypesLayout = v.findViewById(R.id.set_pref_food_types_layout);
        LinearLayout categoriesLayout = v.findViewById(R.id.set_pref_categories_layout);

        SharedPreferences sharedPref = getSharedPreferences("prefStore", Context.MODE_PRIVATE);
        foodTypePref = sharedPref.getInt("SearchFoodTypes", 0);
        categoryPref = sharedPref.getInt("SearchCategoryTypes", 0);

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
