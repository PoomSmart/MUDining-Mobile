package com.untitled.untitledapk;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitled.untitledapk.persistence.Restaurant;

import java.util.List;

/**
 * Created by User on 11/4/2561.
 */

public class RestaurantListAdapter extends ArrayAdapter<String> {

    private final List<Restaurant> restaurants;
    private final Activity context;

    public RestaurantListAdapter(Activity context, List<Restaurant> restaurants) {
        super(context, R.layout.listview_layout);
        this.context = context;
        this.restaurants = restaurants;
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
        return rowView;
    }

    private class ViewHolder {
        private TextView resName;
        private TextView restInfo;
        private ImageView resPhoto;

        public ViewHolder(View v) {
            resName = v.findViewById(R.id.resName);
            restInfo = v.findViewById(R.id.resInfo);
            resPhoto = v.findViewById(R.id.resPhoto);
        }
    }
}
