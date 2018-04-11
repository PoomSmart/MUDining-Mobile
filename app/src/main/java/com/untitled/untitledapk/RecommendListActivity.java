package com.untitled.untitledapk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendListActivity extends AppCompatActivity {

    ListView lv;
    String[] RestaurantName = {"Ready Mele", "Sumo Grill", "Just test"};
    String[] RestaurantInfo = {"test", "easy", "gg we lose"};
    Integer[] RestaurantPhoto = {R.drawable.readymele, R.drawable.zumo, R.drawable.wtf};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        lv = findViewById(R.id.lv);
            CustomListview customListview = new CustomListview(this,RestaurantName,RestaurantInfo,RestaurantPhoto);
            lv.setAdapter(customListview);
    }
}
