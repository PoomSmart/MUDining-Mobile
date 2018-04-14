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

/**
 * Created by User on 11/4/2561.
 */

public class CustomListView extends ArrayAdapter<String> {

    private String[] RestaurantName;
    private String[] RestaurantInfo;
    private Integer[] RestaurantPhoto;
    private Activity context;

    public CustomListView(Activity context, String[] RestaurantName, String[] RestaurantInfo, Integer[] RestaurantPhoto) {
        super(context, R.layout.listview_layout, RestaurantName);
        this.context = context;
        this.RestaurantName = RestaurantName;
        this.RestaurantInfo = RestaurantInfo;
        this.RestaurantPhoto = RestaurantPhoto;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.resPhoto.setImageResource(RestaurantPhoto[position]);
        viewHolder.resName.setText(RestaurantName[position]);
        viewHolder.restInfo.setText(RestaurantInfo[position]);
        return r;
    }

    class ViewHolder {
        TextView resName;
        TextView restInfo;
        ImageView resPhoto;

        ViewHolder(View v) {
            resName = v.findViewById(R.id.resName);
            restInfo = v.findViewById(R.id.resInfo);
            resPhoto = v.findViewById(R.id.resPhoto);
        }
    }
}
