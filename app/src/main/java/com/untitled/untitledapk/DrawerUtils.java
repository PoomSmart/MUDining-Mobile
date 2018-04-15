package com.untitled.untitledapk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.untitled.untitledapk.persistence.Restaurant;

import java.io.Serializable;
import java.util.List;

public class DrawerUtils {

    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        PrimaryDrawerItem drawerItemHome = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.nav_home).withIcon(R.drawable.ic_home);
        PrimaryDrawerItem drawerItemSearch = new PrimaryDrawerItem()
                .withIdentifier(2).withName(R.string.nav_search).withIcon(R.drawable.ic_search);
        PrimaryDrawerItem drawerItemRestaurants = new PrimaryDrawerItem()
                .withIdentifier(3).withName(R.string.nav_restaurants).withIcon(R.drawable.ic_restaurant);
        PrimaryDrawerItem drawerItemPreferences = new PrimaryDrawerItem()
                .withIdentifier(4).withName(R.string.nav_preferences).withIcon(R.drawable.ic_settings);
        PrimaryDrawerItem drawerItemContact = new PrimaryDrawerItem()
                .withIdentifier(5).withName(R.string.nav_contact).withIcon(R.drawable.ic_contacts);

        new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(drawerItemHome, drawerItemSearch, drawerItemRestaurants, drawerItemPreferences, drawerItemContact)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch ((int) drawerItem.getIdentifier()) {
                        case 1:
                            if (!(activity instanceof MainActivity))
                                view.getContext().startActivity(new Intent(activity, MainActivity.class));
                            return true;
                        case 2:
                            new ReadDatabaseTask().execute(view.getContext(), activity);
                            return true;
                        case 4:
                            view.getContext().startActivity(new Intent(activity, SetPreferenceActivity.class));
                            return true;
                        default:
                            return true;
                    }
                }).build();
    }

    private static class ReadDatabaseTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Context context = (Context) params[0];
            Context from = (Context) params[1];
            Intent intent = new Intent(from, SearchActivity.class);
            List<Restaurant> restaurants = RestaurantManager.getRestaurants(context);
            intent.putExtra("restaurants", (Serializable) restaurants);
            context.startActivity(intent);
            return null;
        }
    }
}
