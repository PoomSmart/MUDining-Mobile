package com.untitled.untitledapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolBar;
    Drawer drawer;

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
            default:
                break;
        }
        startFragment(fragmentClass, itemId);
    }

}
