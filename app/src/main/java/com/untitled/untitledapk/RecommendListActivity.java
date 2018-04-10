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

    TextView restaurantName;
    TextView restaurantInfo;
    ImageView restaurantPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        restaurantName = (TextView)findViewById(R.id.restaurant_name);
        restaurantInfo = (TextView)findViewById(R.id.restaurant_info);
        restaurantPhoto = (ImageView)findViewById(R.id.restaurant_photo);

        restaurantName.setText("ReadyMele");
        restaurantInfo.setText("Modern retaurant serves fusion food");
        restaurantPhoto.setImageResource(R.drawable.ic_launcher_background);

    }
}
