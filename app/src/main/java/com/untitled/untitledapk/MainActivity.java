package com.untitled.untitledapk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.untitled.untitledapk.database.DatabaseWorker;
import com.untitled.untitledapk.utilities.DrawerUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;
    Drawer drawer;
    final int REQUEST_FINE_LOCATION = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        drawer = DrawerUtils.getDrawer(this, toolBar, R.string.nav_recommend);
        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            selectDrawerItem((int) drawerItem.getIdentifier());
            return true;
        });
        startFragment(RecommendFragment.class, R.string.nav_recommend);
        drawer.setSelection(R.string.nav_recommend);

        DatabaseWorker.work(this);
        // Request the location permission right from the beginning
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    public void startFragment(Class fragmentClass, int itemId) {
        Fragment fragment = null;
        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle(itemId);
        drawer.closeDrawer();
    }

    public void selectDrawerItem(int itemId) {
        Class fragmentClass = null;
        switch (itemId) {
            case R.string.nav_recommend:
                fragmentClass = RecommendFragment.class;
                break;
            case R.string.nav_search:
                fragmentClass = SearchFragment.class;
                break;
            case R.string.nav_restaurants:
                fragmentClass = ManageRestaurantFragment.class;
                break;
            case R.string.nav_preferences:
                fragmentClass = SetPreferenceFragment.class;
                break;
            case R.string.nav_contact:
                fragmentClass = ContactUsFragment.class;
            default:
                break;
        }
        startFragment(fragmentClass, itemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getClass().equals(ManageRestaurantFragment.class)) {
                fragment.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

}
