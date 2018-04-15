package com.untitled.untitledapk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.untitled.untitledapk.persistence.Restaurant;

import java.io.Serializable;
import java.util.List;

public class DrawerUtils {

    public static int[] names = {R.string.nav_home, R.string.nav_search, R.string.nav_restaurants, R.string.nav_preferences, R.string.nav_contact};
    public static int[] icons = {R.drawable.ic_home, R.drawable.ic_search, R.drawable.ic_restaurant, R.drawable.ic_settings, R.drawable.ic_contacts};

    public static void getDrawer(final Activity activity, Toolbar toolbar, int name) {
        Drawer drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch ((int) drawerItem.getIdentifier()) {
                        case 0:
                            if (!(activity instanceof MainActivity))
                                view.getContext().startActivity(new Intent(activity, MainActivity.class));
                            return true;
                        case 1:
                            if (!(activity instanceof SearchActivity))
                                new ReadDatabaseTask().execute(view.getContext(), activity);
                            return true;
                        case 3:
                            if (!(activity instanceof SetPreferenceActivity))
                                view.getContext().startActivity(new Intent(activity, SetPreferenceActivity.class));
                            return true;
                        default:
                            return true;
                    }
                }).build();
        for (int i = 0; i < names.length; i++) {
            drawer.addItem(new PrimaryDrawerItem().withIdentifier(i)
                    .withName(names[i]).withIcon(icons[i]));
        }
        for (int i = 0; i < names.length; i++) {
            if (names[i] == name) {
                drawer.setSelection(i);
                break;
            }
        }
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
