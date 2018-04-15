package com.untitled.untitledapk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        restaurantList = findViewById(R.id.liRestaurant);
        textQuery = findViewById(R.id.etSearch);

        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        DrawerUtils.getDrawer(this, toolBar);

        // Need restaurants list as intent extras from previous activity

        restaurants = (List<Restaurant>) getIntent().getExtras().get("restaurants");
        restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        restaurantList.setAdapter(restaurantListAdapter);
        new ReadDatabasesTask().execute();
        textQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchActivity.this).restaurantListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }
}
