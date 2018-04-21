package com.untitled.untitledapk.utilities;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.untitled.untitledapk.R;

public class DrawerUtils {

    public static int[] names = {R.string.nav_recommend, R.string.nav_search, R.string.nav_restaurants, R.string.nav_preferences, R.string.nav_contact};
    public static int[] icons = {R.drawable.ic_recommend, R.drawable.ic_search, R.drawable.ic_restaurant, R.drawable.ic_settings, R.drawable.ic_contacts};

    public static Drawer getDrawer(final Activity activity, Toolbar toolbar, int name) {
        Drawer drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .build();
        for (int i = 0; i < names.length; i++) {
            drawer.addItem(new PrimaryDrawerItem().withIdentifier(names[i])
                    .withName(names[i]).withIcon(icons[i]));
        }
        for (int i = 0; i < names.length; i++) {
            if (names[i] == name) {
                drawer.setSelection(i);
                break;
            }
        }
        return drawer;
    }

}
