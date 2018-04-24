package com.untitled.untitledapk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitled.untitledapk.database.RestaurantImageManager;
import com.untitled.untitledapk.database.RestaurantManager;
import com.untitled.untitledapk.persistence.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.untitled.untitledapk.ManageRestaurantFragment.EDIT_RESTAURANT_REQUEST;

/**
 * Created by User on 11/4/2561.
 */

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> implements Filterable {

    private final Activity context;
    private final boolean editable;
    private int foodTypes;
    private int categoryTypes;
    private ValueFilter valueFilter;
    private List<Restaurant> restaurants;
    private List<Restaurant> filteredRestaurants;

    RestaurantListAdapter(Activity context, List<Restaurant> restaurants, int foodTypes, int categoryTypes) {
        this(context, restaurants, foodTypes, categoryTypes, false);
    }

    RestaurantListAdapter(Activity context, List<Restaurant> restaurants, int foodTypes, int categoryTypes, boolean editable) {
        this(context, restaurants, foodTypes, categoryTypes, editable, false);
    }

    RestaurantListAdapter(Activity context, List<Restaurant> restaurants, int foodTypes, int categoryTypes, boolean editable, boolean recommend) {
        super(context, R.layout.listview_layout, restaurants);
        this.context = context;
        if (!recommend)
            Collections.sort(restaurants);
        this.restaurants = restaurants;
        this.filteredRestaurants = restaurants;
        this.foodTypes = foodTypes;
        this.categoryTypes = categoryTypes;
        this.editable = editable;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        // TODO: Set listview resInfo width and maxLength to match editable value, add ... to the end of resInfo if possible
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Restaurant restaurant = restaurants.get(position);
        RestaurantImageManager.loadImage(context, restaurant.getId(), viewHolder.resPhoto);
        viewHolder.resName.setText(restaurant.getName());
        viewHolder.restInfo.setText(restaurant.getDescription());
        if (editable) {
            viewHolder.resEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditRestaurantActivity.class);
                intent.putExtra("restaurant", restaurant);
                context.startActivityForResult(intent, EDIT_RESTAURANT_REQUEST);
            });
            viewHolder.resDelete.setOnClickListener(v -> new AlertDialog.Builder(context).setTitle(R.string.delete_confirmation).setMessage(String.format("Are you sure you want to remove %s?", restaurant.getName())).setIcon(R.drawable.ic_cancel).setPositiveButton(android.R.string.yes, (dialog, which) -> {
                restaurants.remove(position);
                RestaurantManager.deleteRestaurant(context, restaurant.getId());
                notifyDataSetChanged();
            }).setNegativeButton(android.R.string.no, null).show());
        } else {
            viewHolder.resEdit.setVisibility(View.INVISIBLE);
            viewHolder.resDelete.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(context, ViewRestaurantActivity.class);
            intent.putExtra("restaurant", restaurant);
            rowView.setOnClickListener(v -> context.startActivity(intent));
        }
        rowView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewRestaurantActivity.class);
            intent.putExtra("restaurant", restaurant);
            context.startActivity(intent);
        });
        return rowView;
    }

    public void setList(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void setFoodTypes(int foodTypes) {
        this.foodTypes = foodTypes;
    }

    public void setCategoryTypes(int categoryTypes) {
        this.categoryTypes = categoryTypes;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Restaurant getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private boolean restaurantMatchedFilters(final Restaurant restaurant) {
        return ((foodTypes & restaurant.getFoodTypes()) == foodTypes) && ((categoryTypes & restaurant.getCategoryTypes()) == categoryTypes);
    }

    private class ViewHolder {
        private TextView resName;
        private TextView restInfo;
        private ImageView resPhoto;
        private ImageButton resEdit;
        private ImageButton resDelete;

        ViewHolder(View v) {
            resName = v.findViewById(R.id.resName);
            restInfo = v.findViewById(R.id.resInfo);
            resPhoto = v.findViewById(R.id.resPhoto);
            resEdit = v.findViewById(R.id.resEdit);
            resDelete = v.findViewById(R.id.resDelete);
        }
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String query = constraint != null ? constraint.toString().toUpperCase() : "";
            List<Restaurant> filterList = new ArrayList<>();
            for (int i = 0; i < filteredRestaurants.size(); i++) {
                Restaurant restaurant = filteredRestaurants.get(i);
                if (!restaurantMatchedFilters(restaurant))
                    continue;
                boolean matched = restaurant.getName().toUpperCase().contains(query);
                if (!matched)
                    matched = restaurant.getDescription().toUpperCase().contains(query);
                if (matched)
                    filterList.add(restaurant);
            }
            results.count = filterList.size();
            results.values = filterList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurants = (List<Restaurant>) results.values;
            notifyDataSetChanged();
        }

    }
}
