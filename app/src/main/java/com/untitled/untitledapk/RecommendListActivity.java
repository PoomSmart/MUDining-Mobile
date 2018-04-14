package com.untitled.untitledapk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class RecommendListActivity extends AppCompatActivity {

    String[] RestaurantName = {"Ready Mele", "Sumo Grill", "Just test"};
    String[] RestaurantInfo = {"test", "easy", "gg we lose"};
    Integer[] RestaurantPhoto = {R.drawable.readymele, R.drawable.zumo, R.drawable.wtf};
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        lv = findViewById(R.id.lv);
        CustomListView customListview = new CustomListView(this, RestaurantName, RestaurantInfo, RestaurantPhoto);
        lv.setAdapter(customListview);
    }
}
