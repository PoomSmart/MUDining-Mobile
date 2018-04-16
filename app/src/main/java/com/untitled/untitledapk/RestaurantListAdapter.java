package com.untitled.untitledapk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

/**
 * Created by User on 11/4/2561.
 */

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

    private final Activity context;
    private final boolean editable;
    private List<Restaurant> restaurants;

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
        RestaurantImageManager.loadImage(context, restaurant.getId(), viewHolder.resPhoto);
        viewHolder.resName.setText(restaurant.getName());
        viewHolder.restInfo.setText(restaurant.getDescription());
        if (editable) {
            viewHolder.resEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditRestaurantActivity.class);
                intent.putExtra("restaurant", restaurant);
                context.startActivityForResult(intent, ManageRestaurantActivity.EDIT_RESTAURANT_REQUEST);
            });
            viewHolder.resDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context).setTitle("Delete Confirmation").setMessage(String.format("Are you sure you want to remove %s?", restaurant.getName())).setIcon(R.drawable.ic_cancel).setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    restaurants.remove(position);
                    new RemoveRestaurantTask().execute(context, restaurant);
                }).setNegativeButton(android.R.string.no, null).show();
            });
        } else {
            viewHolder.resEdit.setVisibility(View.INVISIBLE);
            viewHolder.resDelete.setVisibility(View.INVISIBLE);
        }
        return rowView;
    }

    public void setList(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    private class RemoveRestaurantTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Context context = (Context) params[0];
            Restaurant restaurant = (Restaurant) params[1];
            RestaurantManager.deleteRestaurant(context, restaurant.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            notifyDataSetChanged();
        }
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
