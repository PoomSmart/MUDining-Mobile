package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ManageRestaurantFragment extends Fragment {

    public static final int EDIT_RESTAURANT_REQUEST = 4352;

    ListView restaurantList;
    FloatingActionButton addRestaurantButton;
    List<Restaurant> restaurants;
    RestaurantListAdapter restaurantListAdapter;

    public ManageRestaurantFragment() {
    }

    public static ManageRestaurantFragment newInstance() {
        return new ManageRestaurantFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_restaurant, container, false);
        restaurantList = view.findViewById(R.id.restaurantList);
        addRestaurantButton = view.findViewById(R.id.addRestaurantButton);
        addRestaurantButton.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), EditRestaurantActivity.class);
            intent.putExtra("create", true);
            startActivityForResult(intent, EDIT_RESTAURANT_REQUEST);
        });
        restaurants = RestaurantManager.getRestaurants();
        restaurantListAdapter = new RestaurantListAdapter(getActivity(), restaurants, 0, 0, true);
        restaurantList.setAdapter(restaurantListAdapter);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_RESTAURANT_REQUEST && resultCode == RESULT_OK) {
            if (data.hasExtra("restaurant")) {
                Restaurant updatedOrNewRestaurant = (Restaurant) data.getExtras().get("restaurant");
                for (int i = 0; i < restaurants.size(); i++) {
                    if (restaurants.get(i).getId().equals(updatedOrNewRestaurant.getId())) {
                        restaurants.set(i, updatedOrNewRestaurant);
                        break;
                    }
                }
            }
            restaurantListAdapter.sort(Comparator.naturalOrder());
            restaurantListAdapter.notifyDataSetChanged();
        }
    }
}
