package com.untitled.untitledapk;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 11/4/2561.
 */

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

    private final List<Restaurant> restaurants;
    private final Activity context;
    private final boolean editable;

    RestaurantListAdapter(Activity context, List<Restaurant> restaurants) {
        this(context, restaurants, false);
    }

    RestaurantListAdapter(Activity context, List<Restaurant> restaurants, boolean editable) {
        super(context, R.layout.listview_layout, restaurants);
        this.context = context;
        this.restaurants = restaurants;
        this.editable = editable;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Restaurant restaurant = restaurants.get(position);
        viewHolder.resPhoto.setImageBitmap(RestaurantImageManager.getImage(context, restaurant.getId()));
        viewHolder.resName.setText(restaurant.getName());
        viewHolder.restInfo.setText(restaurant.getDescription());
        if (editable) {
            viewHolder.resEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditRestaurantActivity.class);
                intent.putExtra("restaurant", restaurant);
                context.startActivityForResult(intent, ManageRestaurantActivity.EDIT_RESTAURANT_REQUEST);
            });
        } else {
            viewHolder.resEdit.setVisibility(View.INVISIBLE);
            viewHolder.resDelete.setVisibility(View.INVISIBLE);
        }
        return rowView;
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
}
